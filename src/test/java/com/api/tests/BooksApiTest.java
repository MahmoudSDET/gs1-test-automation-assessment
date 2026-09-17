package com.api.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.Book;
import com.api.BooksApiConfig;
import com.api.BooksApiService;
import com.api.data.BooksDataProvider;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

public final class BooksApiTest {
	private BooksApiService booksApi;

	@BeforeClass
	public void setUp() {
		booksApi = new BooksApiService(BooksApiConfig.baseUrl());
	}

	@Test
	public void shouldReturnTheBooksCollection() {
		Response response = booksApi.getBooks();

		Allure.step("Assert books collection response", () -> response.then()
				.statusCode(200)
				.body("size()", greaterThan(0))
				.body("id", hasItem(1)));
	}

	@Test(dataProvider = "existingBooks", dataProviderClass = BooksDataProvider.class)
	public void shouldGetAnExistingBook(int bookId, String expectedTitle) {
		Response response = booksApi.getBook(bookId);

		Allure.step("Assert existing book response", () -> response.then()
				.statusCode(200)
				.body("id", equalTo(bookId))
				.body("title", equalTo(expectedTitle))
				.body("description", notNullValue())
				.body("pageCount", greaterThan(-1)));
	}

	@Test
	public void shouldCreateABookAndReturnSubmittedData() {
		Book book = new Book(0, "API automation book", "Created by the Books API assessment",
				240, "A reusable service-object test", "2026-01-15T00:00:00");

		Response response = booksApi.createBook(book);

		Allure.step("Assert created book response", () -> response.then()
				.statusCode(200)
				.body("id", notNullValue())
				.body("title", equalTo(book.title()))
				.body("description", equalTo(book.description()))
				.body("pageCount", equalTo(book.pageCount()))
				.body("excerpt", equalTo(book.excerpt()))
				.body("publishDate", equalTo(book.publishDate())));
	}

	@Test
	public void shouldUpdateABookAndReturnSubmittedData() {
		Book book = new Book(1, "Updated API automation book", "Updated by the Books API assessment",
				241, "An updated service-object test", "2026-01-16T00:00:00");

		Response response = booksApi.updateBook(book);

		Allure.step("Assert updated book response", () -> response.then()
				.statusCode(200)
				.body("id", equalTo(book.id()))
				.body("title", equalTo(book.title()))
				.body("description", equalTo(book.description()))
				.body("pageCount", equalTo(book.pageCount()))
				.body("excerpt", equalTo(book.excerpt()))
				.body("publishDate", equalTo(book.publishDate())));
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
}