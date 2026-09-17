package com.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.ui.driver.DriverFactory;

import io.qameta.allure.Allure;

public final class TestFailureListener implements ITestListener, ISuiteListener {
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Override
    public void onStart(ISuite suite) {
        String scope = System.getProperty("test.scope", "all");
        suite.getXmlSuite().setName(switch (scope) {
            case "api" -> "API Automation Assessment";
            case "ui" -> "GUI Automation Assessment";
            default -> "API and GUI Automation Assessment";
        });
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            if (driver == null) {
                return;
            }

            Path outputDirectory = Path.of("screenshots");
            Files.createDirectories(outputDirectory);

            String fileName = result.getMethod().getMethodName()
                    + "-" + LocalDateTime.now().format(TIMESTAMP) + ".png";
            Path screenshotPath = outputDirectory.resolve(fileName);

            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, screenshotPath, StandardCopyOption.REPLACE_EXISTING);

            try (var screenshotStream = Files.newInputStream(screenshotPath)) {
                Allure.addAttachment("Failure screenshot: " + result.getMethod().getMethodName(), "image/png", screenshotStream, ".png");
            }
        } catch (IllegalStateException | IOException exception) {
            System.err.println("Unable to capture failure screenshot: " + exception.getMessage());
        }
    }
}