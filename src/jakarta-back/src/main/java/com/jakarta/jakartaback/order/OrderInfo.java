package com.jakarta.jakartaback.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private String order_id;
    private Integer create_time;
    // 存放商品信息的数列，包含item_id，item_count，t_price
    private List<Map<String, Object>> items;
    private String customer_name;
    private Float total_price;

    public float getTotal_price() {
        items.forEach(item -> {
            total_price = total_price + (float) item.get("item_price") * (float) item.get("item_count");
        });
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
