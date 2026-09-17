package com.ui.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ui.pages.DynamicLoadingPage;
import com.ui.pages.HomePage;

import io.qameta.allure.Allure;

public final class DynamicLoadingTest extends BaseTest {
    @Test
    public void shouldDisplayHelloWorldAfterDynamicLoading() {
        DynamicLoadingPage dynamicLoadingPage = new HomePage(driver())
                .open()
                .openDynamicLoading()
                .openExampleTwo()
                .startAndWaitForCompletion();

        Allure.step("Assert dynamic loading text", () -> Assert.assertEquals(dynamicLoadingPage.loadedText(),
                "Hello World!", "Unexpected text after dynamic loading completed"));
    }
}