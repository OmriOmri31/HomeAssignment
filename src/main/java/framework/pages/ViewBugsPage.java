package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Page object for the "View Bugs" screen.
 */
public class ViewBugsPage extends BasePage {

    private final By screenRoot;
    private final By searchBugs;
    private final By buttonAll;
    private final By buttonOpen;
    private final By buttonFixed;
    private final By buttonClosed;
    private final By notABug;
    private final By bugListLocator;
    private String[] bugList;
    private final By buttonEdit;
    private final By buttonDelete;

    /**
     * Constructs the ViewBugsPage.
     *
     * @param driver          Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
     */
    public ViewBugsPage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.screenRoot = resourceId("viewBugsPage");
        this.searchBugs = resourceId("searchInput");
        this.buttonAll = textElement("All");
        this.buttonOpen = textElement("Open");
        this.buttonFixed = textElement("Fixed");
        this.buttonClosed = textElement("Closed");
        this.notABug = textElement("Not a Bug");
        this.bugListLocator = resourceId("bugList");
        this.buttonEdit = textElement("Edit");
        this.buttonDelete = textElement("Delete");
        this.bugList = new String[]{};
    }

    /**
     * Verifies that the View Bugs page is displayed.
     *
     * @return true if page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    /**
     * Searches for bugs using the search input field.
     *
     * @param value search query
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage searchForBugs(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        return this;
    }

    /**
     * Clicks the "All" filter button to show all bugs.
     *
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage clickButtonAll() {
        click(buttonAll);
        updateBugList();
        return this;
    }

    /**
     * Clicks the "Open" filter button to show only open bugs.
     *
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage clickButtonOpen() {
        click(buttonOpen);
        updateBugList();
        return this;
    }

    /**
     * Clicks the "Fixed" filter button to show only fixed bugs.
     *
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage clickButtonFixed() {
        click(buttonFixed);
        updateBugList();
        return this;
    }

    /**
     * Clicks the "Closed" filter button to show only closed bugs.
     *
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage clickButtonClosed() {
        click(buttonClosed);
        updateBugList();
        return this;
    }

    /**
     * Clicks the "Not a Bug" filter button.
     *
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage clickNotABug() {
        click(notABug);
        updateBugList();
        return this;
    }

    private void updateBugList() {
        WebElement list = waitVisible(bugListLocator);
        wait.until(d -> !list.findElements(io.appium.java_client.AppiumBy.className("android.widget.TextView")).isEmpty());
        this.bugList = list.findElements(io.appium.java_client.AppiumBy.className("android.widget.TextView"))
                .stream()
                .map(org.openqa.selenium.WebElement::getText)
                .filter(t -> t.contains("(ID:"))
                .toArray(String[]::new);
    }

    /**
     * Edits the first bug found with the given title.
     * Searches for the bug first, then clicks the Edit button.
     *
     * @param value bug title to search for
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage editBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Edit\").instance(0)"));
        return this;
    }

    /**
     * Edits a bug by its ID.
     * Searches through the bug list to find the matching ID, then clicks its Edit button.
     *
     * @param id bug ID (supports decimal values like 123.0 or 456.5)
     * @return this ViewBugsPage instance
     * @throws IllegalArgumentException if bug with given ID is not found
     */
    public ViewBugsPage editBugById(double id) {
        for (int i = 0; i < bugList.length; i++) {
            if (bugList[i].contains("ID: " + id)) {
                click(io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().text(\"Edit\").instance(" + i + ")"));
                return this;
            }
        }
        throw new IllegalArgumentException(String.format("Bug with ID: %s not found", id));
    }

    /**
     * Deletes the first bug found with the given title.
     * Searches for the bug first, then clicks the Delete button.
     *
     * @param value bug title to search for
     * @return this ViewBugsPage instance
     */
    public ViewBugsPage deleteBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Delete\").instance(0)"));
        return this;
    }

    /**
     * Deletes a bug by its ID.
     * Searches through the bug list to find the matching ID, then clicks its Delete button.
     *
     * @param id bug ID (supports decimal values like 123.0 or 456.5)
     * @return this ViewBugsPage instance
     * @throws IllegalArgumentException if bug with given ID is not found
     */
    public ViewBugsPage deleteBugById(double id) {
        for (int i = 0; i < bugList.length; i++) {
            if (bugList[i].contains("ID: " + id)) {
                click(io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().text(\"Delete\").instance(" + i + ")"));
                return this;
            }
        }
        throw new IllegalArgumentException(String.format("Bug with ID: %s not found", id));
    }

    /**
     * Gets the current bug list array.
     * Useful for verification in tests.
     *
     * @return array of bug strings from the list
     */
    public String[] getBugList() {
        return bugList;
    }

    /**
     * Gets the count of bugs currently displayed in the list.
     *
     * @return number of bugs in the list
     */
    public int getBugCount() {
        return bugList.length;
    }
}