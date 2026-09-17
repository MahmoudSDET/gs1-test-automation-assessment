package com.api;

public record Book(int id, String title, String description, int pageCount, String excerpt, String publishDate) {
}