package com.jakarta.jakartaback.user;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.register.RegisterInfo;
import com.jakarta.jakartaback.utils.Databases;
import com.jakarta.jakartaback.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHandlers {

    // 获取所有管理员列表，返回他们的姓名、身份、邮箱、电话号，并按照指定方式排序【可】
    public static JSONObject getAdmins(String sorting, boolean isAscending, String name, String phone, String email) {
        // 1. 构造SQL的Where语句
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(String.format("name LIKE '%%%s%%' ", name));
        }
        if (phone != null) {
            if (name != null) {
                sb.append("AND ");
            }
            sb.append(String.format("phone LIKE '%%%s%%' ", phone));
        }
        if (email != null) {
            if (name != null || phone != null) {
                sb.append("AND ");
            }
            sb.append(String.format("email LIKE '%%%s%%' ", email));
        }
        // 2. 构造SQL语句
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT name, phone, email FROM admin_user ");
        if (sb.length() > 0) {
            sbSQL.append("WHERE ");
            sbSQL.append(sb);
        }
        if (sorting != null) {
            sbSQL.append("ORDER BY ");
            sbSQL.append(sorting);
            sbSQL.append(isAscending ? " ASC" : " DESC");
        }
        // 3. 执行查询
        List<Map<String, Object>> results = Databases.select(sbSQL.toString());
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 4. 返回结果
        return Utils.succeededReturn(results, "Query Succeeded.");
    }

    // 获取所有管理员列表，返回他们的姓名、身份、邮箱、电话号，并按照默认方式排序【可】
    public static JSONObject getAdmins(String name, String phone, String email) {
        return getAdmins("name", true, name, phone, email);
    }

    // 获取所有客户列表，返回他们的姓名、身份、邮箱、电话号，并按照指定方式排序【可】
    public static JSONObject getClients(String sorting, boolean isAscending, String name, String phone, String email) {
        // 1. 构造SQL的Where语句
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(String.format("name LIKE '%%%s%%' ", name));
        }
        if (phone != null) {
            if (name != null) {
                sb.append("AND ");
            }
            sb.append(String.format("phone LIKE '%%%s%%' ", phone));
        }
        if (email != null) {
            if (name != null || phone != null) {
                sb.append("AND ");
            }
            sb.append(String.format("email LIKE '%%%s%%' ", email));
        }
        // 2. 构造SQL语句
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT name, phone, email FROM client_user ");
        if (sb.length() > 0) {
            sbSQL.append("WHERE ");
            sbSQL.append(sb);
        }
        if (sorting != null) {
            sbSQL.append("ORDER BY ");
            sbSQL.append(sorting);
            sbSQL.append(isAscending ? " ASC" : " DESC");
        }
        // 3. 执行查询
        List<Map<String, Object>> results = Databases.select(sbSQL.toString());
        if (results == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        // 4. 返回结果
        return Utils.succeededReturn(results, "Query Succeeded.");
    }

    // 获取所有客户列表，返回他们的姓名、身份、邮箱、电话号，并按照默认方式排序【可】
    public static JSONObject getClients(String name, String phone, String email) {
        return getClients("name", true, name, phone, email);
    }

    // 获取一位管理员的个人信息【可】
    public static JSONObject getAdminInfo(String userId) {
        Admin user = Admin.getUserByUsername(userId);
        return fetchPersonalInfo(user);
    }

    // 获取一位客户的个人信息【好像没用！】
    public static JSONObject getClientInfo(String userId) {
        Client user = Client.getUserByUsername(userId);
        return fetchPersonalInfo(user);
    }

    // 修改一位管理员的个人信息【可】
    public static JSONObject setAdminInfo(String userId, UpdateInfoRequest request) {
        if (request == null) {
            return Utils.failedReturn(ExceptionType.INVALID_COMING, "Invalid coming message.");
        }
        if (request.isUpdatingPassword()) {
            // test
            System.out.println("想修改管理员密码");
            // 要修改密码！
            // 1. 找到对应用户
            Admin user = Admin.getUserByUsername(request.getUsername());
            if (user == null) {
                return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
            }
            // 2. 对比旧密码
            if (!user.getPassword().equals(request.getOld_password())) {
                return Utils.failedReturn(ExceptionType.WRONG_PASSWORD, "Invalid old password.");
            }
            // 3. 修改新密码
            boolean state = Databases.execute(
                    String.format("UPDATE admin_user SET password='%s' WHERE name='%s';", request.getNew_password(), request.getUsername())
            );
            if (!state) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
            return Utils.succeededReturn("Execute succeeded.");
        }
        else {
            // test
            System.out.println("想修改管理员信息");
            // 要修改其他信息！
            // 1. 找到对应用户
            Admin user = Admin.getUserByUsername(userId);
            if (user == null || request.getUsername() == null) {
                return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
            }
            // 2. 直接修改信息
            boolean state = Databases.execute(
                    String.format("UPDATE admin_user SET name='%s', email='%s', phone='%s' WHERE name='%s';", request.getUsername(), request.getEmail(), request.getTelephone(), userId)
            );
            if (!state) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
            // 3. 获取新token！
            user = new Admin(request.getUsername(), user.getPassword());
            return Utils.succeededReturn(user.getToken(), "Execute succeeded.");
        }
    }

    // 用户修改一位用户的个人信息【可】
    public static JSONObject setClientInfo(String userId, UpdateInfoRequest request) {
        if (request == null) {
            return Utils.failedReturn(ExceptionType.INVALID_COMING, "Invalid coming message.");
        }
        if (request.isUpdatingPassword()) {
            // 要修改密码！
            // 1. 找到对应用户
            Client user = Client.getUserByUsername(request.getUsername());
            if (user == null) {
                return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
            }
            // 2. 对比旧密码
            if (!user.getPassword().equals(request.getOld_password())) {
                return Utils.failedReturn(ExceptionType.WRONG_PASSWORD, "Invalid old password.");
            }
            // 3. 修改新密码
            boolean state = Databases.execute(
                    String.format("UPDATE client_user SET password='%s' WHERE name='%s';", request.getNew_password(), request.getUsername())
            );
            if (!state) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
            return Utils.succeededReturn("Execute succeeded.");
        }
        else {
            // 要修改其他信息！
            // 1. 找到对应用户
            Client user = Client.getUserByUsername(userId);
            if (user == null || request.getUsername() == null) {
                return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
            }
            // 2. 直接修改信息
            boolean state = Databases.execute(
                    String.format("UPDATE client_user SET name='%s', email='%s', phone='%s' WHERE name='%s';", request.getUsername(), request.getEmail(), request.getTelephone(), userId)
            );
            if (!state) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
            }
            // 3. 获取新token！
            user = new Client(request.getUsername(), user.getPassword());
            return Utils.succeededReturn(user.getToken(), "Execute succeeded.");
        }
    }

    // 管理员修改一位客户的个人信息。管理员不会修改密码【可】
    public static JSONObject setClientInfo(UpdateInfoRequest info) {
        if (info == null) {
            return Utils.failedReturn(ExceptionType.INVALID_COMING, "Invalid coming message.");
        }
        // 1. 找到对应用户
        Client user = Client.getUserByUsername(info.getOld_username());
        if (user == null || info.getUsername() == null) {
            return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
        }
        // 2. 直接修改信息
        boolean state = Databases.execute(
                String.format("UPDATE client_user SET name='%s', email='%s', phone='%s' WHERE name='%s';", info.getUsername(), info.getEmail(), info.getTelephone(), user.getUsername())
        );
        if (!state) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        return Utils.succeededReturn("Execute succeeded.");
    }

    // 管理员删除一个用户【可】
    public static JSONObject removeClient(String userId) {
        if (userId == null) {
            return Utils.failedReturn(ExceptionType.INVALID_COMING, "Invalid coming message.");
        }
        // 1. 找到对应用户
        Client user = Client.getUserByUsername(userId);
        if (user == null) {
            return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "Invalid username.");
        }
        // 2. 删掉
        boolean state = Databases.execute(
                String.format("DELETE FROM client_user WHERE name='%s';", userId)
        );
        if (!state) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed.");
        }
        return Utils.succeededReturn("Execute succeeded.");
    }

    // TODO - 增加一个用户，注意鉴定管理员的身份（如果是要增加管理员的话）！
    public static JSONObject addUser(RegisterInfo info) {
        // 1. 执行增加算法
        if (info.isAdmin()) {
            // 1. 1. 鉴定管理员身份，如果是要增加管理员
            Admin adm = new Admin(info.getOperator(), info.getKey());
            if (adm.isNotValid()) {
                // 权限不足
                return Utils.failedReturn(ExceptionType.AUTH_FAILED, "Your account is lack of privileges. Perhaps wrong password.");
            }
            // 1. 2. 执行增加
            if (!Databases.execute(
                    String.format("INSERT INTO admin_user (name, phone, password, email) VALUES ('%s', '%s', '%s', '%s');",
                            info.getUsername(), info.getTelephone(), info.getPassword(), info.getEmail())
            )) {
                // 增加失败
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed. Perhaps duplicate username.");
            }
        } else {
            // 1. 执行增加（直接增加即可，因为是用户）
            if (!Databases.execute(
                    String.format("INSERT INTO client_user (name, phone, password, email) VALUES ('%s', '%s', '%s', '%s');",
                            info.getUsername(), info.getTelephone(), info.getPassword(), info.getEmail())
            )) {
                // 增加失败
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Execute failed. Perhaps duplicate username.");
            }
        }
        return Utils.succeededReturn("Operation done.");
    }

    // 管理员或用户退出登录 ？还要实现吗？我觉得就这样就可了？
    public static JSONObject logout(String username) {
        return Utils.succeededReturn("Logout succeeded.");
    }

    private static JSONObject fetchPersonalInfo(User user) {
        if (user == null) {
             return Utils.failedReturn(ExceptionType.NO_SUCH_USER, "No such user.");
        }
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", user.getUsername());
        dict.put("email", user.getEmail());
        dict.put("telephone", user.getTelephone());

        return Utils.succeededReturn(dict);
    }
}
