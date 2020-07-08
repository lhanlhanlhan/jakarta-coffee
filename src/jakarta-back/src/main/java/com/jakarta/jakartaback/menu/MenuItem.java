package com.jakarta.jakartaback.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem { // 一个菜单项目
    String number;      // 菜单项目编号
    String name;        // 名字
    String category;    // 类型，可以是"drink" "snack" "staple"
    int in_stock;       // 库存量
    int stars;          // 星星量
    String description; // 描述
    int recent_sold;     // 最近销量
    String photo_uri;   // 照片

    float price;        // 单价

    public float getPrice() {
        return price;
    }

    public int getIn_stock() {
        return in_stock;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIn_stock(int in_stock) {
        this.in_stock = in_stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
