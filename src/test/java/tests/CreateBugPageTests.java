package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateBugPageTests extends BaseTest {

    @Test
    @DisplayName("Create a bug with all fields")
    void createBugWithAllFields() {
        // ensureHomePage() is already called in setUp(), so we start from HomePage

        String bugId = generateUniqueBugId();
        String bugTitle = "Best band?";

        // Navigate from HomePage to CreateBugPage
        assertTrue(getHomePage().assertOnPage(), "Should start on HomePage");

        getHomePage().clickCreateBug();
        assertTrue(getCreateBugPage().assertOnPage(), "Create Bug page should be visible");

        // Fill out the bug form
        getCreateBugPage()
                .enterBugId(bugId)
                .pickDate("01/09/2017")
                .enterTitle(bugTitle)
                .enterSteps("1. Take a deep breath \n2. Cry")
                .enterExpected("Dire Straits!")
                .enterActual("Hatikva 6")
                .setStatus("Not a Bug")
                .setSeverity("Trivial")
                .setPriority("Critical")
                .setDetectedBy("Joe Biden")
                .setFixedBy("Donald J. Trump")
                .pickDateClosed(today())
                .submit();

        // Navigate to ViewBugsPage and verify the bug was created
        getCreateBugPage().clickViewBugs();
        assertTrue(getViewBugsPage().assertOnPage(), "Should navigate to ViewBugs page");

        getViewBugsPage().clickButtonAll();
        getViewBugsPage().searchForBugs(bugTitle);
        getViewBugsPage().scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );

        waitFor(1000);

        getViewBugsPage().editBugById(bugId);
        assertTrue(getEditBugPage().assertOnPage(), "Edit page should be visible for the created bug");

    }
}