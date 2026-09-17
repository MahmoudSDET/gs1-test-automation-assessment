package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

public final class DynamicLoadingPage extends BasePage {
    private static final By EXAMPLE_TWO_LINK = By.partialLinkText("Example 2");
    private static final By START_BUTTON = By.cssSelector("#start button");
    private static final By FINISH_TEXT = By.cssSelector("#finish h4");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    @Step("Wait for the dynamic loading page")
    public DynamicLoadingPage waitUntilReady() {
        visible(EXAMPLE_TWO_LINK);
        return this;
    }

    @Step("Open dynamic loading example two")
    public DynamicLoadingPage openExampleTwo() {
        click(EXAMPLE_TWO_LINK);
        visible(START_BUTTON);
        return this;
    }

    @Step("Start dynamic loading and wait for completion")
    public DynamicLoadingPage startAndWaitForCompletion() {
        click(START_BUTTON);
        visible(FINISH_TEXT);
        return this;
    }

    public String loadedText() {
        return textOf(FINISH_TEXT);
    }
}