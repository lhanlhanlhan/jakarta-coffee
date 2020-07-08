package com.jakarta.jakartaback.menu;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.utils.Databases;
import com.jakarta.jakartaback.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuHandlers {

    // 获得整个菜单（区别客户端还是管理员）【管理员可】
    public static JSONObject getMenu(boolean isClient, String sort, String name, String cat) {
        List<Map<String, Object>> results;
        // 构造where语句
        StringBuilder sbWhere = new StringBuilder();
        if (name != null && name.length() > 0) {
            sbWhere.append("WHERE ");
            sbWhere.append(String.format("name LIKE '%%%s%%' ", name));
        }
        if (cat != null && cat.length() > 0) {
            if (name != null && name.length() > 0) {
                sbWhere.append("AND ");
            } else {
                sbWhere.append("WHERE ");
            }
            sbWhere.append(String.format("category = '%s' ", cat));
        }
        // 构造排序语句
        sbWhere.append(String.format("ORDER BY %s ", sort == null ? "name ASC" : sort.substring(1)));
        if (sort != null) {
            switch (sort.charAt(0)) {
                case 'd': // Ascending, e. g.
                    sbWhere.append("DESC ");
                    break;
                case 'a': // Descending, e. g. did
                default:
                    // 默认：根据名字、升序排列
                    sbWhere.append("ASC ");
            }
        }

        // 选择信息
        if (isClient) {
            // 客户端，返回所有信息
            if (sbWhere.length() > 0) {
                results = Databases.select("SELECT item_id, name, category, in_stock, stars, price, recent_sold, description, photo_uri FROM menu_item " + sbWhere.toString());
            } else {
                results = Databases.select("SELECT item_id, name, category, in_stock, stars, price, recent_sold, description, photo_uri FROM menu_item");
            }
        } else {
            // 管理员，只返回 [号码、名字、种类、在库数量、单价、最近销量]
            if (sbWhere.length() > 0) {
                results = Databases.select("SELECT item_id, name, category, in_stock, price, recent_sold FROM menu_item " + sbWhere.toString());
            } else {
                results = Databases.select("SELECT item_id, name, category, in_stock, price, recent_sold FROM menu_item");
            }
        }
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        return Utils.succeededReturn(results, "Query Succeeded");
    }

    // 管理员获取某个菜单项【可】
    public static JSONObject getMenuItem(String id) {
        // 执行语句
        List<Map<String, Object>> results = Databases.select(
                String.format("SELECT description, photo_uri, stars FROM menu_item WHERE item_id = '%s';", id)
        );
        // 返货
        if (results == null || results.size() == 0) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        return Utils.succeededReturn(results.get(0), "Query Succeeded.");
    }

    // 管理员修改某个菜单项【可】
    public static JSONObject setMenuItem(MenuItem newItem) {
        // 执行语句
        boolean result = Databases.execute(
                String.format("UPDATE menu_item SET name='%s', category='%s', price=%.2f, in_stock=%d, stars=%d, recent_sold=%d, description='%s' WHERE item_id = '%s';",
                        newItem.name, newItem.category, newItem.price, newItem.in_stock, newItem.stars, newItem.recent_sold, newItem.description, newItem.number)
        );
        // 返货
        if (!result) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 设置照片（如有）
        result = Databases.execute(
                String.format("UPDATE menu_item SET photo_uri='%s' WHERE item_id = '%s';",
                        newItem.photo_uri, newItem.number)
        );
        if (!result) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }

        return Utils.succeededReturn("Execute Succeeded.");
    }

    // 管理员创建一个菜单项。请注意：图片地址可以为空，其他位置不准为空。【可】
    public static JSONObject addMenuItem(MenuItem newItem) {
        // 生成唯一的id
        String id = genNewID();

        // 执行语句
        boolean result = Databases.execute(
                String.format("INSERT INTO menu_item(item_id, name, category, price, in_stock, stars, recent_sold, description) VALUES('%s', '%s', '%s', %.2f, %d, %d, %d, '%s');",
                        id, newItem.name, newItem.category, newItem.price, newItem.in_stock, newItem.stars, newItem.recent_sold, newItem.description)
        );
        // 返货
        if (!result) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 设置照片（如有）
        result = Databases.execute(
                String.format("UPDATE menu_item SET photo_uri='%s' WHERE item_id = '%s';",
                        newItem.photo_uri, id)
        );
        if (!result) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }

        return Utils.succeededReturn("Execute Succeeded.");
    }

    // 管理员删掉一个菜单项【可】
    public static JSONObject removeMenuItem(String itemId) {
        // 执行语句
        boolean result = Databases.execute(
                String.format("DELETE FROM menu_item WHERE item_id = '%s';", itemId)
        );
        if (!result) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }

        return Utils.succeededReturn("Execute Succeeded.");
    }

    // 生成一个10位的ID
    private static String genNewID() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] charset = str.toCharArray();
        Random random = new Random();
        char[] id = new char[11];
        for(int i = 0; i < 10; i++) {
            id[i] = charset[random.nextInt(62)];
        }
        return String.valueOf(id, 0, 10);
    }
}
