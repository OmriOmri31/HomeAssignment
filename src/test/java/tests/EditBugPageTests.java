package tests;

import framework.driver.DriverFactory;
import framework.pages.EditBugPage;
import framework.pages.HomePage;
import framework.pages.ViewBugsPage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditBugPageTests {

    private AndroidDriver driver;
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    @DisplayName("Edits an existing bug, changes status of an existing bug")
    void editBug() {
        HomePage home = new HomePage(driver, TIMEOUT);
        ViewBugsPage viewBug;
        EditBugPage edit = new EditBugPage(driver, TIMEOUT);
        String bugId = "314";

        assertTrue(home.assertOnPage(), "Home page should be visible");
        viewBug = home.clickViewBugs();
        assertTrue(viewBug.assertOnPage(), "View Bug page should be visible");

        viewBug.scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );
        viewBug.editBugById(bugId);
        assertTrue(edit.assertOnPage(), "Edit page should be visible for the created bug");

        By statusSpinner = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"editBugStatus\")");
        edit.scrollIntoViewIfNeeded(statusSpinner);
        edit.click(statusSpinner);
        By checkedItem = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.CheckedTextView\").checked(true)");
        String current = edit.getTextByLocator(checkedItem);
        String changeStatusTo = "Open".equals(current) ? "Closed" : "Open";
        edit.click(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + changeStatusTo + "\")"));
        edit.save();

        viewBug = new ViewBugsPage(driver, TIMEOUT);
        assertTrue(viewBug.assertOnPage(), "View Bug page should be visible");

        viewBug.scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );
        viewBug.editBugById(bugId);
        assertTrue(edit.assertOnPage(), "Edit page should be visible");

        edit.scrollIntoViewIfNeeded(statusSpinner);
        edit.click(statusSpinner);
        String now = edit.getTextByLocator(checkedItem);
        assertTrue(changeStatusTo.equals(now));
    }
}
