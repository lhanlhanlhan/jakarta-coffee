package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.menu.MenuHandlers;
import com.jakarta.jakartaback.menu.MenuItem;
import com.jakarta.jakartaback.token.AdminLoginToken;
import com.jakarta.jakartaback.token.ClientLoginToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("menus-api")
public class MenuAPIControllers { // 掌管和菜单相关的API

    // 管理员获取菜单接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-menu")
    public JSONObject getMenuAdmin(@RequestParam(value = "sort", defaultValue = "aname") String sorting, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "cat", required = false) String cat) {
        return MenuHandlers.getMenu(false, sorting, name, cat);
    }

    // 客户获取菜单接口
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-menu")
    public JSONObject getMenuClient(@RequestParam(value = "sort", defaultValue = "aname") String sorting, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "cat", required = false) String cat) {
        return MenuHandlers.getMenu(true, sorting, name, cat);
    }

    // 管理员获取菜单项目更多信息的接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-item")
    public JSONObject getMenu(@RequestParam(value = "id") String id) {
        return MenuHandlers.getMenuItem(id);
    }

    // 管理员修改菜单项目接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/set-item")
    public JSONObject setMenu(@RequestBody MenuItem item) {
        return MenuHandlers.setMenuItem(item);
    }

    // 管理员添加菜单项目接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/add-item")
    public JSONObject addMenu(@RequestBody MenuItem item) {
        return MenuHandlers.addMenuItem(item);
    }

    // 管理员删除菜单项目接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/remove-item")
    public JSONObject addMenu(@RequestParam(value = "id") String itemId) {
        return MenuHandlers.removeMenuItem(itemId);
    }

}
