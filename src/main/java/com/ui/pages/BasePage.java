package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ui.config.Config;

public abstract class BasePage {
    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected String textOf(By locator) {
        return wait.until(driver -> {
            try {
                String text = driver.findElement(locator).getText().trim();
                return text.isEmpty() ? null : text;
            } catch (StaleElementReferenceException exception) {
                return null;
            }
        });
    }
}