package com.api;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

public final class BooksApiService {
    private static final String BOOKS_PATH = "/api/v1/Books";

    private final String baseUrl;

    public BooksApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Step("GET all books")
    public Response getBooks() {
        return request().when().get(BOOKS_PATH);
    }

    @Step("GET book with ID {bookId}")
    public Response getBook(int bookId) {
        return request()
                .pathParam("bookId", bookId)
                .when().get(BOOKS_PATH + "/{bookId}");
    }

    @Step("Create a book")
    public Response createBook(Book book) {
        return request()
                .contentType("application/json")
                .body(book)
                .when().post(BOOKS_PATH);
    }

    @Step("Update a book with ID {book.id}")
    public Response updateBook(Book book) {
        return request()
                .contentType("application/json")
                .pathParam("bookId", book.id())
                .body(book)
                .when().put(BOOKS_PATH + "/{bookId}");
    }

    @Step("Delete book with ID {bookId}")
    public Response deleteBook(int bookId) {
        return request()
                .pathParam("bookId", bookId)
                .when().delete(BOOKS_PATH + "/{bookId}");
    }

    private io.restassured.specification.RequestSpecification request() {
        return given()
                .baseUri(baseUrl)
                .filter(new AllureRestAssured());
    }
}