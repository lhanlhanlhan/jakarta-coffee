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

    // TODO 客户获取热门菜单接口
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-hot")
    public JSONObject getHotMenu(@RequestParam(value = "limit", required = false) Integer limit) {
        return MenuHandlers.getHotMenu(limit == null ? 20 : limit);
    }

    // 管理员获取菜单接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-menu")
    public JSONObject getMenuAdmin(@RequestParam(value = "sort", defaultValue = "aname") String sorting, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "cat", required = false) String cat) {
        return MenuHandlers.getMenu(false, sorting, name, cat, null, null);
    }

    // 客户获取菜单接口
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-menu")
    public JSONObject getMenuClient(@RequestParam(value = "sort", defaultValue = "aname") String sorting,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "cat", required = false) String cat,
                                    @RequestParam(value = "min", required = false) Float minPrice,
                                    @RequestParam(value = "max", required = false) Float maxPrice) {
        return MenuHandlers.getMenu(true, sorting, name, cat, minPrice, maxPrice);
    }

    // 管理员获取菜单项目更多信息的接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-item")
    public JSONObject getMenuAdmin(@RequestParam(value = "id") String id) {
        return MenuHandlers.getMenuItemAdmin(id);
    }

    // 客户获取菜单项目更多信息的接口【可】
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-item")
    public JSONObject getMenuClient(@RequestParam(value = "id") String id) {
        return MenuHandlers.getMenuItemClient(id);
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
