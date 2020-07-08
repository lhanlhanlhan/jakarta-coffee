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
public class Client implements User {
    private String username;
    private String password;
    private String email;
    private String telephone;

    // 初始化用户
    public Client(String u, String p) {
        this.username = u;
        this.password = p;
    }

    // 获取用户密码
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

    @Override
    public boolean isAdmin() {
        return false;
    }

    // 获取Token
    public String getToken() {
        return JWT.create()
                .withClaim("position", "client")
                .withAudience(this.username)
                .sign(Algorithm.HMAC256(this.password));
    }

    // 访问数据库，查询密码、用户名。如果二者之一无效，返回false 【可】
    public boolean isNotValid() {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT name, password FROM client_user WHERE name = '%s';", this.username)
        );
        return (results == null ||
                results.size() <= 0 ||
                !results.get(0).get("name").equals(this.username) ||
                !results.get(0).get("password").equals(this.password));
    }

    // 访问数据库，查询用户对应的密码和其他信息。如果用户有效，返回新实例（反之返回NULL）【可】
    public static Client getUserByUsername(String username) {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT password, phone, email FROM client_user WHERE name = '%s';", username)
        );
        if (results == null || results.size() == 0) {
            return null;
        }
        Map<String, Object> user = results.get(0);
        Client client = new Client(username, (String) user.get("password"));
        client.email = (String) user.get("email");
        client.telephone = (String) user.get("phone");

        return client;
    }

    // 访问数据库，返回该用户名是否存在超过1个
    public boolean hasDuplicateUsername() {
        List<Map<String, Object>> results = Databases.select(String.format(
                "SELECT name FROM client_user WHERE name = '%s';", username)
        );
        return results == null || results.size() > 0;
    }
}
