package com.api.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.testng.annotations.DataProvider;

public final class BooksDataProvider {
    private static final String BOOK_DATA = "data/books.csv";

    private BooksDataProvider() {
    }

    @DataProvider(name = "existingBooks")
    public static Object[][] existingBooks() {
        try (InputStream stream = BooksDataProvider.class.getClassLoader().getResourceAsStream(BOOK_DATA)) {
            if (stream == null) {
                throw new IllegalStateException("Missing test data: " + BOOK_DATA);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .skip(1)
                        .map(String::trim)
                        .filter(line -> !line.isBlank())
                        .map(line -> Arrays.stream(line.split(",", -1)).map(String::trim).toArray(String[]::new))
                        .map(values -> new Object[]{Integer.parseInt(values[0]), values[1]})
                        .toArray(Object[][]::new);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + BOOK_DATA, exception);
        }
    }
}