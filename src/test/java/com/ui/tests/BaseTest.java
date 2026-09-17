package com.ui.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.ui.driver.DriverFactory;

public abstract class BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }
}