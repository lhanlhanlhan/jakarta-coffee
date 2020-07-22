package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.cart.CartHandlers;
import com.jakarta.jakartaback.token.ClientLoginToken;
import com.jakarta.jakartaback.token.Tokens;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("carts-api")
public class CartAPIControllers {

    // 用户加入购物车接口【可】
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/add-cart")
    public JSONObject addCart(@RequestParam(name = "item_id") String itemID,
                              @RequestParam(name = "count", required = false) Integer count) {
        String username = Tokens.getTokenUserId();
        return CartHandlers.addCart(username, itemID, count == null ? 1 : count);
    }

    // 用户更改购物车接口【可】
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/edit-cart")
    public JSONObject editCart(@RequestParam(name = "item_id") String itemID,
                               @RequestParam(name = "count") Integer count) {
        String username = Tokens.getTokenUserId();
        return CartHandlers.editCart(username, itemID, count);
    }

    // 用户删除购物车项目接口【可】
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/del-from-cart")
    public JSONObject delFromCart(@RequestParam(name = "item_id") String itemID) {
        String username = Tokens.getTokenUserId();
        return CartHandlers.delFromCart(username, itemID);
    }

    // 用户获取购物车接口 【可】
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-cart")
    public JSONObject getCart() {
        String username = Tokens.getTokenUserId();
        return CartHandlers.getCart(username);
    }
}
