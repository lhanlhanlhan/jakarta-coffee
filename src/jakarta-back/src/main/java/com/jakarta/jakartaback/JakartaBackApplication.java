package com.jakarta.jakartaback;

import com.jakarta.jakartaback.utils.Databases;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JakartaBackApplication {

    // context对象
    private static ConfigurableApplicationContext appContext;

    public static ConfigurableApplicationContext getAppContext() {
        return appContext;
    }

    public static void main(String[] args) {
        appContext = SpringApplication.run(JakartaBackApplication.class, args);
        // 保存db对象
        Databases.setDB(appContext.getBean(JdbcTemplate.class));
    }

}
