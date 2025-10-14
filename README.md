# Android Mobile Test Automation Framework

A Page Object Model (POM) based test automation framework for Android applications using Appium, Selenium WebDriver, and JUnit 5.

**Author**: Omri Nir

---

## Features

- Page Object Model architecture
- Thread-safe driver management for parallel execution
- Smart scrolling with element detection
- Fluent API design
- Android native date picker handling
- Comprehensive logging with SLF4J + Logback
- Edge case handling (empty lists, missing elements)

---

## Prerequisites

- Java JDK 11+
- Android SDK with platform-tools
- Appium Server 2.x with UiAutomator2 driver
- Android emulator or physical device

---

## Installation

1. Clone the repository
2. Place your APK in `apps/` directory
3. Update `src/main/resources/config.properties`:
```properties
server.url=YOUR_SERVER_URL
deviceName=YOUR_DEVICE_NAME
platformVersion=YOUR_PLATFORM_VERSION
app.path=apps/your-app.apk
platformName=YOUR_PLATFORM_NAME
automationName=YOUR_AUTOMATION_NAME
explicitTimeoutSec=20 (Can choose your own)
noReset=true (prevent app data deletion in the end of a test)
```

4. Build: `./gradlew clean build`

---

## Project Structure
```
src/
├── main/java/framework/
│   ├── base/BasePage.java
│   ├── components/AndroidDatePicker.java
│   ├── driver/DriverFactory.java
│   ├── pages/
│   │   ├── CreateBugPage.java
│   │   ├── EditBugPage.java
│   │   ├── HomePage.java
│   │   └── ViewBugsPage.java
│   └── utils/Config.java
├── main/resources/
│   ├── config.properties
│   └── logback.xml
└── test/java/tests/
    ├── base/BaseTest.java
    ├── CreateBugPageTests.java
    ├── EditBugPageTests.java
    └── PrintListOfBugsTests.java
```

---

## Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests CreateBugPageTests

# View reports
open build/reports/tests/test/index.html
```

---

## Writing Tests Example
```java
@Test
@DisplayName("Create a bug with all fields")
void createBug() {
    String bugId = generateUniqueBugId();
    
    getHomePage().clickCreateBug();
    
    getCreateBugPage()
        .enterBugId(bugId)
        .pickDate("15/10/2024")
        .enterTitle("Bug title")
        .enterSteps("Steps to reproduce")
        .setStatus("Open")
        .setSeverity("Critical")
        .submit();
}
```

---

## Architecture

- **BasePage**: Common interactions (waiting, scrolling, clicking)
- **DriverFactory**: Thread-safe driver management with ThreadLocal
- **BaseTest**: Test setup, teardown, and utilities
- **Page Objects**: Encapsulate page structure and behavior with fluent API

---

## Logging

Logs are written to:
- Console (real-time)
- `logs/test-execution.log` (persistent)

Change log level in `logback.xml`:
```xml
<logger name="framework" level="INFO"/>  <!-- or DEBUG -->
```

---

## Troubleshooting

**Element not found**: Check locators, increase timeout in config  
**Connection refused**: Start Appium server (`appium`)  
**No devices**: Check `adb devices`  
**App not installed**: Verify APK path in config.properties  
**No bugs found**: Create bugs using `CreateBugPageTests` first

---

## Key Design Patterns

- Page Object Model (POM)
- Factory Pattern (driver creation)
- Singleton Pattern (thread-local drivers)
- Fluent Interface (method chaining)

---

## Technology Stack

- Java 11
- Appium 9.2.1
- Selenium 4.21.0
- JUnit 5.10.2
- Gradle 8.x
- SLF4J + Logback

---

**Made by Omri Nir**