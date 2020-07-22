package com.jakarta.jakartaback.order;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.utils.Databases;
import com.jakarta.jakartaback.utils.Utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OrderHandlers {

    // 添加订单【可】
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");

    public static JSONObject addOrder(OrderInfo orderinfo) {
        // 生成一个order ID
        Date now = new Date();
        String orderID = df.format(now) + Utils.genNewID(8);
        // 获取时间戳
        long timestamp = now.getTime() / 1000;
        Date d = new Date(timestamp * 1000);

        if (!Databases.execute(
                String.format("INSERT INTO ordinary_order (order_id, customer_name, create_time, total_price) VALUES ('%s', '%s', %d, 0);",
                        orderID, orderinfo.getCustomer_name(), timestamp)
        )) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed. Perhaps duplicate order id.");
        }

        // 检查商品
        AtomicBoolean hasError = new AtomicBoolean(false);
        AtomicReference<Float> totalPrice = new AtomicReference<>((float) 0);
        orderinfo.getItems().forEach(item -> {
            // 获取商品目前信息
            List<Map<String, Object>> product = Databases.select(
                    "SELECT price FROM menu_item WHERE item_id = '" + item.get("item_id") + "';"
            );
            if (product == null) {
                hasError.set(true);
                return;
            }
            // 如果有获取到，再添加进订单
            int count = (Integer) item.get("item_count");
            float subTotal = ((BigDecimal) product.get(0).get("price")).floatValue() * count;
            boolean hasErrorInExe = !Databases.execute(
                    String.format("INSERT INTO order_item (order_id, item_id, item_count, t_price) VALUES ('%s', '%s', %d, %f);",
                            orderID,
                            item.get("item_id"),
                            count,
                            subTotal) // t_price是某个item的小计！orderInfo中没有t_price！
            );
            if (hasErrorInExe) {
                hasError.set(true);
                return;
            }
            // 记录总价
            totalPrice.updateAndGet(v -> (v + subTotal));
        });

        // 更新订单的总价
        if (!Databases.execute(
                String.format("UPDATE ordinary_order SET total_price = %.2f WHERE order_id = '%s';",
                        totalPrice.get(), orderID)
        )) {
            hasError.set(true);
        }

        // 返回
        if (hasError.get()) {
            return Utils.succeededReturn("ERR OCCURRED");
        } else {
            return Utils.succeededReturn("NO ERR");
        }
    }

    // 删除订单 【可】
    public static JSONObject deleteOrder(String order_id) {
        if (!Databases.execute(
                String.format("DELETE FROM ordinary_order WHERE order_id='%s';",
                        order_id)
        ) || !Databases.execute(
                String.format("DELETE FROM order_item WHERE order_id='%s';",
                        order_id)
        ))
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed. Perhaps invalid order id.");
        else
            return Utils.succeededReturn("Operation done.");
    }

    // 根据客户名查找所有信息（客户用）【可】
    public static JSONObject getOrders(String customerId) {
        if (customerId == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 查找所有订单的头信息
        List<Map<String, Object>> results = Databases.select(
                "SELECT order_id, create_time, total_price FROM ordinary_order WHERE customer_name = '"
                        + customerId + "';"
        );
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 查找所有订单的所有items，并放入result中
        results.forEach(order -> {
            List<Map<String, Object>> orderDetails = Databases.select(
                    "SELECT item_count, t_price, name, photo_uri FROM order_item, menu_item WHERE order_id = '" +
                            order.get("order_id") + "' AND order_item.item_id = menu_item.item_id;"
            );
            order.put("items", orderDetails);
        });
        // 返回result
        return Utils.succeededReturn(results, "Query Succeeded.");
    }

    // 根据客户名、邮箱、手机号（或混合）查找所有订单号、完成时间戳、总价（管理员用）checked 【可】
    public static JSONObject getOrders(String customerId, String email, String phone) {
        // 1. 构造SQL的Where语句
        StringBuilder sb = new StringBuilder();
        boolean hasFormerTokens = false;
        if (customerId != null && customerId.length() > 0) {
            sb.append(String.format("customer_name LIKE '%%%s%%' ", customerId));
            hasFormerTokens = true;
        }
        if (phone != null && phone.length() > 0) {
            if (hasFormerTokens) {
                sb.append("AND ");
            }
            sb.append(String.format("phone LIKE '%%%s%%' ", phone));
            hasFormerTokens = true;
        }
        if (email != null && email.length() > 0) {
            if (hasFormerTokens) {
                sb.append("AND ");
            }
            sb.append(String.format("email LIKE '%%%s%%' ", email));
            hasFormerTokens = true;
        }
        // 2. 构造SQL语句
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT customer_name, order_id, create_time, total_price FROM ordinary_order, client_user WHERE customer_name = name ");
        if (hasFormerTokens) {
            sbSQL.append("AND ");
            sbSQL.append(sb);
        }
        // 3. 执行查询
        List<Map<String, Object>> results = Databases.select(sbSQL.toString());
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 4. 返回结果
        return Utils.succeededReturn(results, "Query Succeeded.");
    }

    // 管理员根据订单号查找某一个订单（可以指定一个用户名，对比订单是否符合该用户名）【可】
    public static JSONObject getOneOrder(String orderId, String customerName) {
        if (customerName == null) {   // 给管理员用的，无差别搜索
            List<Map<String, Object>> orderDetails = Databases.select(
                    "SELECT item_count, t_price, name, category, order_item.item_id item_id FROM order_item, menu_item WHERE order_id = '" +
                            orderId + "' AND order_item.item_id = menu_item.item_id;"
            );
            if (orderDetails == null) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute Failed.");
            }
            // 返回result
            return Utils.succeededReturn(orderDetails, "Query Succeeded.");
        } else {                    // 给用户用的，只准搜索他自己的
            List<Map<String, Object>> results = Databases.select(
                    String.format("SELECT * FROM ordinary_order WHERE order_id = '%s' AND customer_name = '%s'",
                            orderId, customerName)
            );
            if (results == null) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
            // 4. 返回结果
            return Utils.succeededReturn(results, "Query Succeeded.");
        }
    }

}
