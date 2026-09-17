package com.ui.tests;

import java.nio.file.Path;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ui.data.CsvDataProvider;
import com.ui.pages.FileUploadPage;
import com.ui.pages.HomePage;

import io.qameta.allure.Allure;

public final class FileUploadTest extends BaseTest {
    @Test(dataProvider = "uploadFiles", dataProviderClass = CsvDataProvider.class)
    public void shouldUploadImageSuccessfully(String relativeFilePath) {
        Path image = Path.of(relativeFilePath);

        FileUploadPage uploadPage = new HomePage(driver())
                .open()
                .openFileUpload()
                .selectFile(image)
                .submit();

                Allure.step("Assert upload success heading", () -> Assert.assertEquals(uploadPage.successMessage(),
                                "File Uploaded!", "The upload success heading was not displayed"));
                Allure.step("Assert uploaded filename", () -> Assert.assertEquals(uploadPage.uploadedFileName(),
                                image.getFileName().toString(), "The uploaded filename did not match the selected file"));
    }
}