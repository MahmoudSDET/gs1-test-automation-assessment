package com.api;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class BooksApiService {
    private static final String BOOKS_PATH = "/api/v1/Books";
    private static final String BOOK_PATH = BOOKS_PATH + "/{bookId}";

    private final RequestSpecification spec;

    public BooksApiService(String baseUrl) {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    @Step("GET all books")
    public Response getBooks() {
        return given(spec).get(BOOKS_PATH);
    }

    @Step("GET book with ID {bookId}")
    public Response getBook(int bookId) {
        return given(spec).pathParam("bookId", bookId).get(BOOK_PATH);
    }

    @Step("Create a book")
    public Response createBook(Book book) {
        return given(spec).body(book).post(BOOKS_PATH);
    }

    @Step("Update a book with ID {book.id}")
    public Response updateBook(Book book) {
        return given(spec).pathParam("bookId", book.id()).body(book).put(BOOK_PATH);
    }

    @Step("Delete book with ID {bookId}")
    public Response deleteBook(int bookId) {
        return given(spec).pathParam("bookId", bookId).delete(BOOK_PATH);
    }
}
