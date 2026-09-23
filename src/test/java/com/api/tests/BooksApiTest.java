package com.api.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.Matcher;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.Book;
import com.api.BooksApiService;
import com.config.Config;
import com.data.TestData;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

public final class BooksApiTest {
    private BooksApiService booksApi;

    @BeforeClass
    public void setUp() {
        booksApi = new BooksApiService(Config.apiBaseUrl());
    }

    @Test
    public void shouldReturnTheBooksCollection() {
        Response response = booksApi.getBooks();

        Allure.step("Assert books collection response", () -> response.then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("id", hasItem(1)));
    }

    @Test(dataProvider = "existingBooks", dataProviderClass = TestData.class)
    public void shouldGetAnExistingBook(int bookId, String expectedTitle) {
        Response response = booksApi.getBook(bookId);

        Allure.step("Assert existing book response", () -> response.then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .body("title", equalTo(expectedTitle))
                .body("description", notNullValue())
                .body("pageCount", greaterThan(-1)));
    }

    @Test(dataProvider = "booksToCreate", dataProviderClass = TestData.class)
    public void shouldCreateABookAndReturnSubmittedData(Book book) {
        Response response = booksApi.createBook(book);

        assertReturnedBook("Assert created book response", response, book, notNullValue());
    }

    @Test(dataProvider = "booksToUpdate", dataProviderClass = TestData.class)
    public void shouldUpdateABookAndReturnSubmittedData(Book book) {
        Response response = booksApi.updateBook(book);

        assertReturnedBook("Assert updated book response", response, book, equalTo(book.id()));
    }

    @Test
    public void shouldDeleteABookSuccessfully() {
        Response response = booksApi.deleteBook(1);

        Allure.step("Assert deleted book response", () -> response.then().statusCode(200));
    }

    @Test
    public void shouldReturnNotFoundForAnUnknownBook() {
        Response response = booksApi.getBook(99999);

        Allure.step("Assert unknown book response", () -> response.then().statusCode(404));
    }

    private static void assertReturnedBook(String stepName, Response response, Book book, Matcher<?> idMatcher) {
        Allure.step(stepName, () -> response.then()
                .statusCode(200)
                .body("id", idMatcher)
                .body("title", equalTo(book.title()))
                .body("description", equalTo(book.description()))
                .body("pageCount", equalTo(book.pageCount()))
                .body("excerpt", equalTo(book.excerpt()))
                .body("publishDate", equalTo(book.publishDate())));
    }
}
