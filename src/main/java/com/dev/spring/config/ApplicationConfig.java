package com.dev.spring.config;

import com.dev.spring.database.pool.ConnectionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import web.WebConfiguration;

@Import(value = {WebConfiguration.class})
@Configuration
public class ApplicationConfig {

    @Bean("pool2")
    public ConnectionPool pool2(@Value("${db.username}") String username){
        return new ConnectionPool(username, 20);
    }

    @Bean("pool3")
    public ConnectionPool pool3() {
        return new ConnectionPool("Roman Talanov", 20);
    }

}
