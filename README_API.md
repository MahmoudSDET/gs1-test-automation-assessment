# Books API Test Automation

Maven-based API automation for the FakeRESTApi bookstore service using Java 21, REST Assured, and TestNG.

## API under test

Base URL:

```text
https://fakerestapi.azurewebsites.net
```

Books endpoints:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/api/v1/Books` | Retrieve all books |
| GET | `/api/v1/Books/{id}` | Retrieve one book |
| POST | `/api/v1/Books` | Create a book |
| PUT | `/api/v1/Books/{id}` | Update a book |
| DELETE | `/api/v1/Books/{id}` | Delete a book |

FakeRESTApi may simulate create, update, and delete operations without permanently storing data. Tests therefore validate the response returned by each operation rather than relying on later requests to observe persisted state.

## Design

- `BooksApiService` provides reusable REST Assured methods for the Books endpoints.
- `Book` is the request model used for create and update operations.
- `BooksApiConfig` loads the API base URL from external configuration.
- `BooksDataProvider` loads reusable book IDs and titles from CSV.
- `BooksApiTest` contains fluent REST Assured assertions.

## Test coverage

- Happy path: retrieve existing books using externalized, data-driven test data.
- Happy path: create a book and validate the returned ID and submitted fields.
- Negative path: request an unknown book ID and verify the `404 Not Found` response.

## Prerequisites

- JDK 21 or newer
- Maven 3.9 or newer
- Internet access to reach FakeRESTApi

Verify the local tools:

```bash
java -version
mvn -version
```

## Run the API tests

From the project root:

```bash
mvn -Papi-tests clean test
```

The `api-tests` profile uses `testng-api.xml`, which contains only the Books API scenarios. Its Allure suite name is `API Automation Assessment`.

Run the UI tests only:

```bash
mvn -Pui-tests clean test
```

The `ui-tests` profile uses `testng-ui.xml`, which contains only the GUI scenarios. Its Allure suite name is `GUI Automation Assessment`.

Run all tests without a profile:

```bash
mvn clean test
```

The default run uses `testng.xml` and keeps both API and GUI scenarios. Its Allure suite name is `API and GUI Automation Assessment`.

Override the API base URL when testing a compatible environment:

```bash
mvn -Papi-tests test -Dapi.baseUrl=https://fakerestapi.azurewebsites.net
```

## Test data and configuration

- API configuration: `src/test/resources/api.properties`
- Book data: `src/test/resources/data/books.csv`
- API service: `src/main/java/com/api/BooksApiService.java`
- API data provider: `src/test/java/com/api/data/BooksDataProvider.java`
- API tests: `src/test/java/com/api/tests/BooksApiTest.java`

## Results

Maven and TestNG reports are generated under:

```text
target/surefire-reports/
```

Allure results and the generated HTML report are stored under:

```text
target/allure-results/
target/allure-report/
```

Allure captures API request and response details through the REST Assured filter.
UI page-object workflows and browser startup/cleanup are recorded as named steps.

Generate the Allure report after running all tests:

```bash
mvn clean test allure:report
```

Run only the API tests and generate the API Allure report:

```bash
mvn -Papi-tests clean test allure:report
```

To serve the current results in a local browser with the Allure CLI:

```bash
allure serve target/allure-results
```

The command prints a local URL. Press `Ctrl+C` to stop the server. The generated
HTML report is also available under `target/allure-report/` after `allure:report`.

Install and verify the Allure CLI on Windows:

```powershell
choco install allure
allure --version
```

Scoop users can install it with `scoop install allure`. Alternatively, download
the Allure command-line ZIP from the [Allure releases](https://github.com/allure-framework/allure2/releases), extract it, and add its `bin` directory to the Windows `Path`.
