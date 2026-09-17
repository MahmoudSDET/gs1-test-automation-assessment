package com.ui.pages;

import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Step;

public final class FileUploadPage extends BasePage {
    private static final By FILE_INPUT = By.id("file-upload");
    private static final By UPLOAD_BUTTON = By.id("file-submit");
    private static final By SUCCESS_HEADING = By.cssSelector("#content h3");
    private static final By UPLOADED_FILE = By.id("uploaded-files");

    public FileUploadPage(WebDriver driver) {
        super(driver);
    }

    @Step("Wait for the file upload page")
    public FileUploadPage waitUntilReady() {
        visible(FILE_INPUT);
        return this;
    }

    @Step("Select the upload file")
    public FileUploadPage selectFile(Path file) {
        Path absoluteFile = file.toAbsolutePath().normalize();
        if (!absoluteFile.toFile().isFile()) {
            throw new IllegalArgumentException("Upload file does not exist: " + absoluteFile);
        }
        visible(FILE_INPUT).sendKeys(absoluteFile.toString());
        return this;
    }

    @Step("Submit the file upload")
    public FileUploadPage submit() {
        click(UPLOAD_BUTTON);
        visible(UPLOADED_FILE);
        visible(SUCCESS_HEADING);
        return this;
    }

    public String successMessage() {
        return textOf(SUCCESS_HEADING);
    }

    public String uploadedFileName() {
        return textOf(UPLOADED_FILE);
    }
}