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
        // String bugId = "314"; // Use this to edit a specific bug by ID

        // Navigate to ViewBugsPage
        assertTrue(getHomePage().assertOnPage(), "Home page should be visible");
        getHomePage().clickViewBugs();
        assertTrue(getViewBugsPage().assertOnPage(), "View Bug page should be visible");

        // Wait a moment for page to fully load
        waitFor(500);

        // Get all bugs and ensure at least one exists
        getViewBugsPage().clickButtonAll();

        // Give time for filter to apply
        waitFor(500);

        String[] bugList = getViewBugsPage().getBugList();

        // If no bugs found, fail with clear message
        if (bugList.length == 0) {
            throw new AssertionError("No bugs found in the list - create at least one bug before running this test");
        }

        // Extract the ID from the first bug in the list
        String firstBug = bugList[0];
        String bugId = extractBugId(firstBug);

        // Find and edit the first bug
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

        // Verify the change was saved
        assertTrue(getViewBugsPage().assertOnPage(), "View Bug page should be visible after save");
        waitFor(500);

        // Find the bug in the bug list
        getViewBugsPage().scrollIntoViewIfNeeded(
                AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
        );

        // Go to the edit bug page
        getViewBugsPage().editBugById(bugId);
        assertTrue(getEditBugPage().assertOnPage(), "Edit page should be visible");

        // Look for the changed field and click on it
        getEditBugPage().scrollIntoViewIfNeeded(statusSpinner);
        getEditBugPage().click(statusSpinner);

        // Get the current value of the field
        String now = getEditBugPage().getTextByLocator(checkedItem);

        assertTrue(changeStatusTo.equals(now),
                "Status should have changed from '" + current + "' to '" + changeStatusTo + "'");

        // Close the dropdown
        getEditBugPage().click(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"" + changeStatusTo + "\")")
        );
        getEditBugPage().save();
    }

    // Helper method to extract bug ID from bug list entry
    private String extractBugId(String bugEntry) {
        // Bug entry format: "Bug Title (ID: 12345)"
        int idStart = bugEntry.indexOf("(ID: ") + 5;
        int idEnd = bugEntry.indexOf(")", idStart);
        if (idStart == -1 || idEnd == -1) {
            throw new IllegalStateException("Unable to extract bug ID from: " + bugEntry);
        }
        return bugEntry.substring(idStart, idEnd);
    }

}