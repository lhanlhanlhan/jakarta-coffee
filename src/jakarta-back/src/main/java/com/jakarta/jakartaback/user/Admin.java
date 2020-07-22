package com.jakarta.jakartaback.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jakarta.jakartaback.utils.Databases;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements User {
    private String username;
    private String password;
    private String email;
    private String telephone;

    public Admin(String u, String p) {
        this.username = u;
        this.password = p;
    }

    // 获取用户密码
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    // 获取Token
    @Override
    public String getToken() {
        return JWT.create().withAudience(this.username)
                .withClaim("position", "admin")
                .sign(Algorithm.HMAC256(this.password));
    }


    @Override
    public boolean isAdmin() {
        return true;
    }

    // 访问数据库，返回该用户是否有效【可】
    @Override
    public boolean isNotValid() {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT name, password FROM admin_user WHERE name = '%s';", this.username)
        );
        return (results == null ||
                results.size() <= 0 ||
                !results.get(0).get("name").equals(this.username) ||
                !results.get(0).get("password").equals(this.password));
    }

    // 访问数据库，查询用户对应的密码。如果用户有效，返回新实例（反之返回NULL）【可】
    public static Admin getUserByUsername(String username) {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT password, phone, email FROM admin_user WHERE name = '%s';", username)
        );
        if (results == null || results.size() == 0) {
            return null;
        }
        Map<String, Object> user = results.get(0);
        Admin adm = new Admin(username, (String) user.get("password"));
        adm.email = (String) user.get("email");
        adm.telephone = (String) user.get("phone");

        return adm;
    }

    // 访问数据库，返回该用户名是否存在超过1个
    public boolean hasDuplicateUsername() {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT name FROM admin_user WHERE name = '%s';", username)
        );
        return results == null || results.size() > 0;
    }
}
