package com.jll.jdbc.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DruidPoolConnection {
    private static DruidPoolConnection druidPoolConnection = null;
    private static DruidDataSource druidDataSource = null;

    static {
        Properties properties = loadPropertyFile("dbconfig.properties");
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DruidPoolConnection() {
    }

    public static synchronized DruidPoolConnection getInstance() {
        if (null == druidPoolConnection) {
            druidPoolConnection = new DruidPoolConnection();
        }
        return druidPoolConnection;
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

    public static Properties loadPropertyFile(String fullFile) {
        if (null == fullFile || fullFile.equals(""))
            throw new IllegalArgumentException(
                    "Properties file path can not be null : " + fullFile);
        InputStream inputStream = DruidPoolConnection.class.getClassLoader().getResourceAsStream(fullFile);
        Properties p = null;
        try {
            p = new Properties();
            p.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Properties file not found: "
                    + fullFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Properties file can not be loading: " + fullFile);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }
}