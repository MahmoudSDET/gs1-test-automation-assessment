package com.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BooksApiConfig {
    private static final Properties PROPERTIES = loadProperties();

    private BooksApiConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("api.baseUrl", PROPERTIES.getProperty("api.baseUrl"));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = BooksApiConfig.class.getClassLoader()
                .getResourceAsStream("api.properties")) {
            if (input == null) {
                throw new IllegalStateException("Missing api.properties test configuration");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load API test configuration", exception);
        }
    }
}