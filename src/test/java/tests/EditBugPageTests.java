package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditBugPageTests extends BaseTest {

    @Test
    @DisplayName("Edits an existing bug, changes status of an existing bug")
    void editBug() {
        String bugId = "314";

        // Navigate to ViewBugsPage
        assertTrue(getHomePage().assertOnPage(), "Home page should be visible");
        getHomePage().clickViewBugs();
        assertTrue(getViewBugsPage().assertOnPage(), "View Bug page should be visible");

        // Find and edit the bug
        getViewBugsPage().scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );
        getViewBugsPage().editBugById(bugId);
        assertTrue(getEditBugPage().assertOnPage(), "Edit page should be visible for the bug");

        // Get current status and toggle it
        By statusSpinner = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"editBugStatus\")");
        getEditBugPage().scrollIntoViewIfNeeded(statusSpinner);
        getEditBugPage().click(statusSpinner);

        By checkedItem = AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.CheckedTextView\").checked(true)"
        );
        String current = getEditBugPage().getTextByLocator(checkedItem);
        String changeStatusTo = "Open".equals(current) ? "Closed" : "Open";

        getEditBugPage().click(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"" + changeStatusTo + "\")")
        );
        getEditBugPage().save();

        // Verify the change was saved by going to edit page of the bug and checking the field
        assertTrue(getViewBugsPage().assertOnPage(), "View Bug page should be visible after save");

        //finds the bug in the bug list
        getViewBugsPage().scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );
        //Going to the edit bug page
        getViewBugsPage().editBugById(bugId);
        assertTrue(getEditBugPage().assertOnPage(), "Edit page should be visible");

        //Looking for the changed field and clicking on it
        getEditBugPage().scrollIntoViewIfNeeded(statusSpinner);
        getEditBugPage().click(statusSpinner);

        //Gets the current value of the field
        String now = getEditBugPage().getTextByLocator(checkedItem);

        assertTrue(changeStatusTo.equals(now),
                "Status should have changed from '" + current + "' to '" + changeStatusTo + "'");

        //Saves
        getEditBugPage().click(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"" + changeStatusTo + "\")")
        );
        getEditBugPage().save();
    }
}