package com.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private Config() {
    }

    public static String baseUrl() {
        return value("baseUrl");
    }

    public static String apiBaseUrl() {
        return value("api.baseUrl");
    }

    public static String browser() {
        return value("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(value("headless"));
    }

    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(value("timeoutSeconds")));
    }

    private static String value(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
        return value.trim();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Missing classpath resource: " + CONFIG_FILE);
            }
            properties.load(stream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + CONFIG_FILE, exception);
        }
    }
}