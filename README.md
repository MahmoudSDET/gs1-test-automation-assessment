# API and GUI Test Automation Assessment

Maven-based automation framework implemented with Java 25, REST Assured, Selenium WebDriver, and TestNG.

## API assessment

The Books API tests target the [FakeRESTApi bookstore API](https://fakerestapi.azurewebsites.net/index.html) and use a reusable `BooksApiService` service object for the documented `/api/v1/Books` endpoints. The suite covers two happy paths, including data-driven retrieval and create-response validation, plus an unknown-book negative case.

The base URL is externalized in `src/test/resources/api.properties` and can be overridden with `-Dapi.baseUrl=<url>`. FakeRESTApi may simulate writes, so tests validate returned responses rather than assuming submitted data is persisted.

## Covered scenarios

1. **File Upload** — opens the upload page, uploads a small image, submits it, and verifies the success message and uploaded filename.
2. **Dynamic Loading / Example 2** — starts the loading process, waits explicitly for completion, and verifies `Hello World!` (the site's actual text, including punctuation).

## Design highlights

- Page Object Model with page-specific behavior and locators.
- Fluent page methods for readable test flows.
- Thread-local driver management, allowing safe parallel execution.
- Selenium Manager handles browser-driver discovery automatically.
- Explicit waits; no `Thread.sleep` calls.
- Configuration and reusable test inputs are externalized.
- TestNG assertions provide clear failure messages.
- Allure records API requests/responses and named UI/assertion steps.
- Failure screenshots are written to `screenshots/`.

## Prerequisites

- JDK 21 or newer
- Maven 3.9 or newer
- Google Chrome or Mozilla Firefox
- Internet access on the first run so Selenium Manager can obtain a compatible driver if one is not already available

Check the tools:

```bash
java -version
mvn -version
```

## Install Allure

Allure requires Java and is used to view the test results generated under
`target/allure-results/`. On Windows, install the Allure command-line tool with
one of these package managers:

```powershell
# Chocolatey
choco install allure

# Scoop
scoop install allure
```

Alternatively, download the Allure command-line ZIP from the [Allure releases](https://github.com/allure-framework/allure2/releases), extract it, and add its `bin` directory to the Windows `Path` environment variable.

Verify the installation:

```powershell
allure --version
```

## Run the tests

From the project root:

The project has separate TestNG suite files:

- `testng.xml` runs API and UI tests together.
- `testng-api.xml` runs API tests only through the `api-tests` Maven profile.
- `testng-ui.xml` runs UI tests only through the `ui-tests` Maven profile.

```bash
mvn clean test allure:report
```

This cleans previous Allure output, runs API and UI tests in parallel using two
TestNG threads, and generates the report under `target/allure-report/`.

To serve the generated results directly with the Allure CLI:

```powershell
allure serve target/allure-results
```

Allure starts a local web server and prints the report URL. Press `Ctrl+C` in
the terminal to stop the server.

To run the tests and open the live Allure report:

```bash
mvn clean test allure:serve
```

To run only API tests and generate their Allure report:

```bash
mvn -Papi-tests clean test allure:report
```

To run only UI tests and generate their Allure report:

```bash
mvn -Pui-tests clean test allure:report
```

After any test run, serve the current results with the Allure CLI:

```bash
allure serve target/allure-results
```

Run only the API tests:

```bash
mvn -Papi-tests test
```

The Allure suite name is `API Automation Assessment` for this run.

Run only the UI tests:

```bash
mvn -Pui-tests test
```

The Allure suite name is `GUI Automation Assessment` for this run. When both
test types run, the suite name remains `API and GUI Automation Assessment`.

Run in headless Chrome:

```bash
mvn clean test -Dheadless=true
```

Run in Firefox:

```bash
mvn clean test -Dbrowser=firefox
```

Run a single test class:

```bash
mvn -Dtest=FileUploadTest test
mvn -Dtest=DynamicLoadingTest test
```

The defaults in `src/test/resources/config.properties` can be overridden from the command line using `-Dbrowser`, `-Dheadless`, `-DbaseUrl`, or `-DtimeoutSeconds`.

## Results

- Maven/TestNG results: `target/surefire-reports/`
- Failure screenshots: `screenshots/`

## Project structure

```text
src/main/java/com/ui/
  config/       Configuration reader
  driver/       Thread-safe WebDriver lifecycle
  pages/        Page objects
src/main/java/com/api/
  Book.java             API request model
  BooksApiConfig.java   API configuration
  BooksApiService.java  REST Assured service object
src/test/java/com/ui/
  data/         CSV DataProvider
  listeners/    Failure screenshot listener
  tests/        Test classes and shared setup
src/test/java/com/api/
  data/         Books CSV DataProvider
  tests/        Books API tests
src/test/resources/
  data/         Reusable test data and upload image
```

## Git workflow

The submission is initialized as a Git repository. A normal change flow is:

```bash
git checkout -b feature/my-change
git add .
git commit -m "Describe the change"
```
