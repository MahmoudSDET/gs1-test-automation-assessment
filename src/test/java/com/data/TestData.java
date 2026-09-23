package com.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.api.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestData {
    private TestData() {
    }

    @DataProvider(name = "uploadFiles")
    public static Object[][] uploadFiles() {
        return csv("data/upload-data.csv").stream()
                .map(row -> new Object[]{row[0]})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "existingBooks")
    public static Object[][] existingBooks() {
        return csv("data/books.csv").stream()
                .map(row -> new Object[]{Integer.parseInt(row[0]), row[1]})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "booksToCreate")
    public static Object[][] booksToCreate() {
        return bookPayloads("create");
    }

    @DataProvider(name = "booksToUpdate")
    public static Object[][] booksToUpdate() {
        return bookPayloads("update");
    }

    private static Object[][] bookPayloads(String group) {
        String resource = "data/book-payloads.json";
        try (InputStream stream = open(resource)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode books = mapper.readTree(stream).path(group);
            if (!books.isArray() || books.isEmpty()) {
                throw new IllegalStateException("No '" + group + "' payloads in " + resource);
            }
            return Arrays.stream(mapper.convertValue(books, Book[].class))
                    .map(book -> new Object[]{book})
                    .toArray(Object[][]::new);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + resource, exception);
        }
    }

    // ponytail: naive comma split, no quoted fields; swap in a CSV library if data needs commas
    private static List<String[]> csv(String resource) {
        try (InputStream stream = open(resource);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(line -> Arrays.stream(line.split(",", -1)).map(String::trim).toArray(String[]::new))
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + resource, exception);
        }
    }

    private static InputStream open(String resource) {
        InputStream stream = TestData.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new IllegalStateException("Missing test data: " + resource);
        }
        return stream;
    }
}
