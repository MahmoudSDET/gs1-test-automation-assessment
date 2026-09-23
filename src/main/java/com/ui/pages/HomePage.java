package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

import com.config.Config;

public final class HomePage extends BasePage {
    private static final By FILE_UPLOAD_LINK = By.linkText("File Upload");
    private static final By DYNAMIC_LOADING_LINK = By.linkText("Dynamic Loading");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the application home page")
    public HomePage open() {
        driver.get(Config.baseUrl());
        visible(FILE_UPLOAD_LINK);
        return this;
    }

    @Step("Open the file upload page")
    public FileUploadPage openFileUpload() {
        click(FILE_UPLOAD_LINK);
        return new FileUploadPage(driver).waitUntilReady();
    }

    @Step("Open the dynamic loading page")
    public DynamicLoadingPage openDynamicLoading() {
        click(DYNAMIC_LOADING_LINK);
        return new DynamicLoadingPage(driver).waitUntilReady();
    }
}