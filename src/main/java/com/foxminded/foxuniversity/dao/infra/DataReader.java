package com.foxminded.foxuniversity.dao.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataReader {
    private static DataReader instance;

    private DataReader() {
    }

    public static synchronized DataReader getInstance() {
        if (instance == null) {
            instance = new DataReader();
        }
        return instance;
    }

    public Properties getAccessProperties (String fileName) {
        Properties properties = null;
        try (InputStream stream = DataReader.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            properties = new Properties();
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
