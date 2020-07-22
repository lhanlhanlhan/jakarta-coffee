package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.login.LoginHandlers;
import com.jakarta.jakartaback.login.LoginInfo;
import com.jakarta.jakartaback.register.RegisterHandlers;
import com.jakarta.jakartaback.register.RegisterInfo;
import com.jakarta.jakartaback.token.AdminLoginToken;
import com.jakarta.jakartaback.token.ClientLoginToken;
import com.jakarta.jakartaback.token.PassToken;
import com.jakarta.jakartaback.token.Tokens;
import com.jakarta.jakartaback.user.Admin;
import com.jakarta.jakartaback.user.Client;
import com.jakarta.jakartaback.user.UpdateInfoRequest;
import com.jakarta.jakartaback.user.UserHandlers;
import com.jakarta.jakartaback.utils.Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users-api")
public class UsersAPIControllers { // 掌管和用户相关的API

    // 管理员登录接口【可】
    @CrossOrigin
    @PassToken
    @PostMapping("/admin-login")
    public JSONObject adminLogin(@RequestBody LoginInfo info) {
        Admin theUser = new Admin(info.getUsername(), info.getPassword());
        return LoginHandlers.checkLoginInfo(theUser);
    }

    // 用户登录接口【可】
    @CrossOrigin
    @PassToken
    @PostMapping("/client-login")
    public JSONObject clientLogin(@RequestBody LoginInfo info) {
        Client theUser = new Client(info.getUsername(), info.getPassword());
        return LoginHandlers.checkLoginInfo(theUser);
    }

    // 注册接口
    @CrossOrigin
    @PassToken
    @PostMapping("/register")
    public JSONObject register(@RequestBody RegisterInfo info) {
        return RegisterHandlers.submitRegisterInfo(info);
    }

    // 管理员登出接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/logout")
    public JSONObject adminLogout() {
        return UserHandlers.logout(Tokens.getTokenUserId());
    }

    // 客户登出接口【可】
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/logout")
    public JSONObject clientLogout() {
        return UserHandlers.logout(Tokens.getTokenUserId());
    }

    // 管理员获取所有管理员信息的接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-admin-users")
    public JSONObject getAdminUsers(@RequestParam(value = "sort", defaultValue = "aname") String sorting, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "email", required = false) String email) {
        switch (sorting.charAt(0)) {
            case 'a': // Ascending, e. g. aid
                return UserHandlers.getAdmins(sorting.substring(1), true, name, phone, email);
            case 'd': // Descending, e. g. did
                return UserHandlers.getAdmins(sorting.substring(1), false, name, phone, email);
            default:
                // 默认：根据名字、升序排列
                return UserHandlers.getAdmins(name, phone, email);
        }
    }

    // 管理员获取所有客户信息的接口【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-client-users")
    public JSONObject getClientUsers(@RequestParam(value = "sort", defaultValue = "aname") String sorting, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "email", required = false) String email) {
        switch (sorting.charAt(0)) {
            case 'a': // Ascending, e. g. aid
                return UserHandlers.getClients(sorting.substring(1), true, name, phone, email);
            case 'd': // Descending, e. g. did
                return UserHandlers.getClients(sorting.substring(1), false, name, phone, email);
            default:
                // 默认：根据名字、升序排列
                return UserHandlers.getClients(name, phone, email);
        }
    }

    // 管理员获取客户信息的接口，返回除了密码外的信息【？好像没用】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-client-info")
    public JSONObject getClientInfo(@RequestBody String clientName) {
        return UserHandlers.getClientInfo(clientName);
    }

    // 管理员修改客户信息接口，修改除了密码外的信息【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/set-client-info")
    public JSONObject setClientInfo(@RequestBody UpdateInfoRequest request) {
        return UserHandlers.setClientInfo(request);
    }

    // 管理员获取自己信息接口，返回除了密码外的信息【可】
    @CrossOrigin
    @AdminLoginToken
    @GetMapping("/admin/get-my-info")
    public JSONObject getAdminSelfInfo() {
        // 管理员用户名
        String userId = Tokens.getTokenUserId();
        return UserHandlers.getAdminInfo(userId);
    }

    // 管理员修改自己信息接口，提供旧密码、新密码【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/set-my-info")
    public JSONObject setAdminSelfInfo(@RequestBody UpdateInfoRequest info) {
        // 管理员用户名
        String userId = Tokens.getTokenUserId();
        return UserHandlers.setAdminInfo(userId, info);
    }

    // 客户提取自己信息接口，提供除密码以外的信息
    @CrossOrigin
    @ClientLoginToken
    @GetMapping("/client/get-my-info")
    public JSONObject getClientSelfInfo() {
        // 客户用户名
        String userId = Tokens.getTokenUserId();
        return UserHandlers.getClientInfo(userId);
    }

    // 客户修改自己信息接口，提供旧密码、新密码
    @CrossOrigin
    @ClientLoginToken
    @PostMapping("/client/set-my-info")
    public JSONObject setClientSelfInfo(@RequestBody UpdateInfoRequest info) {
        // 客户用户名
        String userId = Tokens.getTokenUserId();
        return UserHandlers.setClientInfo(userId, info);
    }

    // 管理员删除客户接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/remove-client")
    public JSONObject removeClient(@RequestParam(value = "name") String userId) {
        return UserHandlers.removeClient(userId);
    }

    // 管理员添加客户接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/add-client")
    public JSONObject addClient(@RequestBody RegisterInfo info) {
        return UserHandlers.addUser(info);
    }

    // 管理员添加管理员接口【可】
    @CrossOrigin
    @AdminLoginToken
    @PostMapping("/admin/add-admin")
    public JSONObject addAdmin(@RequestBody RegisterInfo info) {
        return UserHandlers.addUser(info);
    }

    // TODO - 用户提取自己的地址接口
    @CrossOrigin
    @PassToken
    @PostMapping("/client/get-my-addr")
    public JSONObject getAddress() {
        String userId = Tokens.getTokenUserId();

        JSONArray addr = new JSONArray();
        JSONObject defaultAddr = new JSONObject();
        defaultAddr.put("receiver", "李涵");
        defaultAddr.put("street", "香港半山區干德道36號慧明苑A-23/F");
        defaultAddr.put("tel", "2577 3595");

        addr.add(defaultAddr);
        return Utils.succeededReturn(addr, "Query Succeeded.");
    }
}
