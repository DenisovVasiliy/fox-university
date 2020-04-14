package com.foxminded.foxuniversity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.foxminded.foxuniversity"})
@PropertySource("classpath:database.properties")
public class AppConfig {

    @Value("${url}")
    private String url;
    @Value("${driver}")
    private String driver;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean()
    public SimpleJdbcInsert jdbcInsert() {
        return new SimpleJdbcInsert(dataSource());
    }
}