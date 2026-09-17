package com.ui.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.testng.annotations.DataProvider;

public final class CsvDataProvider {
    private static final String UPLOAD_DATA = "data/upload-data.csv";

    private CsvDataProvider() {
    }

    @DataProvider(name = "uploadFiles")
    public static Object[][] uploadFiles() {
        try (InputStream stream = CsvDataProvider.class.getClassLoader().getResourceAsStream(UPLOAD_DATA)) {
            if (stream == null) {
                throw new IllegalStateException("Missing test data: " + UPLOAD_DATA);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                List<String> paths = reader.lines()
                        .skip(1)
                        .map(String::trim)
                        .filter(line -> !line.isBlank())
                        .toList();
                return paths.stream().map(path -> new Object[]{path}).toArray(Object[][]::new);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + UPLOAD_DATA, exception);
        }
    }
}