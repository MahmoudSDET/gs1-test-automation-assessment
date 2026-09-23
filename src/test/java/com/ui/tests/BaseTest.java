package com.ui.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.ui.driver.DriverFactory;

import io.qameta.allure.Allure;

public abstract class BaseTest {
    private static final Path SCREENSHOTS = Path.of("target", "screenshots");

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(result.getMethod().getMethodName());
            }
        } finally {
            DriverFactory.quitDriver();
        }
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    private void captureScreenshot(String testName) {
        try {
            byte[] png = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
            Files.createDirectories(SCREENSHOTS);
            Files.write(SCREENSHOTS.resolve(testName + "-" + System.currentTimeMillis() + ".png"), png);
            Allure.addAttachment("Failure screenshot: " + testName, "image/png", new ByteArrayInputStream(png), ".png");
        } catch (IOException | WebDriverException exception) {
            System.err.println("Unable to capture failure screenshot: " + exception.getMessage());
        }
    }
}
