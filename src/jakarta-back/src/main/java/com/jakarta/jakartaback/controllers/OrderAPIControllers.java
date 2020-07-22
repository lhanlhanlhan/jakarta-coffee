package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.order.OrderHandlers;
import com.jakarta.jakartaback.order.OrderInfo;
import com.jakarta.jakartaback.token.AdminLoginToken;
import com.jakarta.jakartaback.token.ClientLoginToken;
import com.jakarta.jakartaback.token.Tokens;
import com.jakarta.jakartaback.utils.Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders-api")
public class OrderAPIControllers {

    // 客户新建订单【可】
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/new-order")
    public JSONObject newOrder(@RequestBody OrderInfo info) {
        String customerName = Tokens.getTokenUserId();
        if (info == null || !info.getCustomer_name().equals(customerName)) {
            return Utils.failedReturn(ExceptionType.INVALID_USER, "This order was not created by you!");
        }
        return OrderHandlers.addOrder(info);
    }

    // 管理员删掉订单【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/remove-order")
    public JSONObject removeOrder(@RequestParam(value = "order_id") String orderId) {
        return OrderHandlers.deleteOrder(orderId);
    }

    // 客户根据他的名字查询它的所有订单（返回所有信息） 【可】
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-all-orders")
    public JSONObject clientGetAllOrders() {
        String customerName = Tokens.getTokenUserId();
        return OrderHandlers.getOrders(customerName);
    }

    // 管理员根据某个或某些客户参数（名字、邮箱、手机号，或combined）查询其名下所有订单（返回所有订单号、完成时间戳、总价）【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-all-orders")
    public JSONObject adminGetAllOrders(@RequestParam(value = "customer_name", required = false) String customerName,
                                        @RequestParam(value = "email", required = false) String email,
                                        @RequestParam(value = "phone", required = false) String phone) {
        return OrderHandlers.getOrders(customerName, email, phone);
    }

    // 客户根据某个订单号（要求是其名下的才行）查询某一个订单（返回订单信息） 【可】
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-order")
    public JSONObject clientGetOrder(@RequestParam(value = "order_id") String orderId) {
        // 获取用户名
        String userId = Tokens.getTokenUserId();
        return OrderHandlers.getOneOrder(orderId, userId);
    }

    // 管理员根据某个订单号查询某一个订单（返回订单信息）【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-order")
    public JSONObject adminGetOrder(@RequestParam(value = "order_id") String orderId) {
        return OrderHandlers.getOneOrder(orderId, null);
    }
}
