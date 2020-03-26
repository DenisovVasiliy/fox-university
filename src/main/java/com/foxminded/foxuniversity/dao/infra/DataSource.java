package com.foxminded.foxuniversity.dao.infra;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.Properties;

public class DataSource {
    private static DataSource instance;
    private SimpleDriverDataSource dataSource;

    private DataSource () {

    }

    public static synchronized DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
            instance.createDataSource();
        }
        return instance;
    }

    public SimpleDriverDataSource getDataSource() {
        return dataSource;
    }

    private void createDataSource() {
        DataReader dataReader = DataReader.getInstance();
        Properties properties = dataReader.getAccessProperties("database.properties");
        dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("login"));
        dataSource.setPassword(properties.getProperty("password"));
    }



}
