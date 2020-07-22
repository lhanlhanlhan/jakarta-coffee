package com.jakarta.jakartaback.cart;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.utils.Databases;
import com.jakarta.jakartaback.utils.Utils;

import java.util.List;
import java.util.Map;

public class CartHandlers {

    // 获取某个用户的购物车 【可】
    public static JSONObject getCart(String username) {
        // 执行语句
        List<Map<String, Object>> results = Databases.select(
                String.format("SELECT cart_record.item_id item_id, name, item_count, photo_uri, price FROM cart_record, menu_item " + "WHERE customer_name = '%s' AND cart_record.item_id = menu_item.item_id;",
                        username)
        );
        // 返货
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        return Utils.succeededReturn(results, "Query Succeeded.");
    }

    // 更改某个用户的购物项目【可】
    public static JSONObject editCart(String username, String itemID, Integer count) {
        if (Databases.execute(
                String.format("UPDATE cart_record SET item_count = %d WHERE customer_name = '%s' AND item_id = '%s';",
                        count, username, itemID)
        )) {
            return Utils.succeededReturn("Execute succeeded.");
        } else {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
    }

    // 删除某个购物车项目【可】
    public static JSONObject delFromCart(String username, String itemID) {
        if (Databases.execute(
                String.format("DELETE FROM cart_record WHERE customer_name = '%s' AND item_id = '%s';",
                        username, itemID)
        )) {
            return Utils.succeededReturn("Execute succeeded.");
        } else {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute succeeded.");
        }
    }

    // 加入购物车项目【可】
    public static JSONObject addCart(String username, String itemID, Integer count) {
        // 搜索有没有该项目
        List<Map<String, Object>> results = Databases.select(
                String.format("SELECT item_id FROM cart_record WHERE customer_name = '%s' AND item_id = '%s';",
                        username, itemID)
        );
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute Failed");
        }
        if (results.size() == 0) {
            // 无原项目，则新建一个项目
            if (Databases.execute(
                    String.format("INSERT INTO cart_record (customer_name, item_id, item_count) VALUES('%s', '%s', %d);",
                            username, itemID, count)
            )) {
                return Utils.succeededReturn("Execute succeeded.");
            } else {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
        } else {
            // 有原项目，则更新这个项目
            if (Databases.execute(
                    String.format("UPDATE cart_record SET item_count = item_count + %d WHERE customer_name = '%s' AND item_id = '%s';",
                            count, username, itemID)
            )) {
                return Utils.succeededReturn("Execute succeeded.");
            } else {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
        }
    }
}
