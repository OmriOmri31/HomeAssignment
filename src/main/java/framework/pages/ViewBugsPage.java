package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Set<String> accumulatedBugs = new LinkedHashSet<>();

    /**
     * Constructs the ViewBugsPage with the given driver and timeout.
     *
     * @param driver the Android driver instance
     * @param explicitTimeout maximum wait time for page elements
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
     * Verifies that the View Bugs page is currently displayed.
     *
     * @return true if the page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    /**
     * Searches for bugs matching the specified text and updates the bug list.
     *
     * @param value the search query
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage searchForBugs(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        return this;
    }

    /**
     * Filters to show all bugs regardless of status.
     *
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage clickButtonAll() {
        click(buttonAll);
        updateBugList();
        return this;
    }

    /**
     * Filters to show only bugs with "Open" status.
     *
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage clickButtonOpen() {
        click(buttonOpen);
        updateBugList();
        return this;
    }

    /**
     * Filters to show only bugs with "Fixed" status.
     *
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage clickButtonFixed() {
        click(buttonFixed);
        updateBugList();
        return this;
    }

    /**
     * Filters to show only bugs with "Closed" status.
     *
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage clickButtonClosed() {
        click(buttonClosed);
        updateBugList();
        return this;
    }

    /**
     * Filters to show only bugs with "Not a Bug" status.
     *
     * @return this ViewBugsPage instance for method chaining
     */
    public ViewBugsPage clickNotABug() {
        click(notABug);
        updateBugList();
        return this;
    }

    /**
     * Updates the internal bug list from currently visible bugs.
     * Accumulates bugs across multiple calls during scrolling.
     * Handles empty result sets gracefully with a 2 second timeout
     * and returns an empty arry of strings
     */
    private void updateBugList() {
        WebElement list = waitVisible(bugListLocator);

        try {
            wait.withTimeout(Duration.ofSeconds(2))
                    .until(d -> !list.findElements(AppiumBy.className("android.widget.TextView")).isEmpty());
        } catch (TimeoutException e) {
            // List is empty, which is valid
            this.bugList = new String[0];
            return;
        }

        String[] visibleBugs = list.findElements(AppiumBy.className("android.widget.TextView"))
                .stream()
                .map(WebElement::getText)
                .filter(t -> t.contains("(ID:"))
                .toArray(String[]::new);

        for (String bug : visibleBugs) {
            accumulatedBugs.add(bug);
        }

        this.bugList = accumulatedBugs.toArray(new String[0]);
    }

    public void updateBugListAfterDeletion() {
        accumulatedBugs.clear();
        updateBugList();
    }

    /*public ViewBugsPage editBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Edit\").instance(0)"));
        return this;
    }*/

    /**
     * Opens the edit page for a specific bug by its ID.
     * Scrolls the bug into view if necessary.
     *
     * @param idText the bug ID as a string
     * @return this ViewBugsPage instance for method chaining
     * @throws AssertionError if the bug is not found after scrolling
     */
    public ViewBugsPage editBugById(String idText) {
        By row = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")");
        scrollIntoViewIfNeeded(row);

        By editForRow = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")" +
                        ".fromParent(new UiSelector().text(\"Edit\"))");
        click(editForRow);
        return this;
    }

    /*public ViewBugsPage deleteBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Delete\").instance(0)"));
        return this;
    }*/

    /**
     * Deletes a specific bug by its ID.
     * Scrolls the bug into view if necessary.
     *
     * @param idText the bug ID as a string
     * @return this ViewBugsPage instance for method chaining
     * @throws AssertionError if the bug is not found
     */
    public ViewBugsPage deleteBugById(String idText) {
        By row = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")");
        scrollIntoViewIfNeeded(row);
        if (!isVisible(row)) {
            throw new AssertionError("Bug with ID " + idText + " not found");
        }
        By deleteForRow = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")" +
                        ".fromParent(new UiSelector().text(\"Delete\"))");
        click(deleteForRow);
        return this;
    }

    /**
     * Retrieves the current accumulated bug list.
     * Automatically updates the list before returning.
     *
     * @return array of bug descriptions including IDs
     */
    public String[] getBugList() {
        updateBugList();
        return bugList;
    }

    /**
     * Gets the number of bugs in the current bug list.
     *
     * @return the bug count
     */
    public int getBugCount() {
        return bugList.length;
    }

    /*private static String formatId(double id) {
        if (id == Math.rint(id)) return String.valueOf((long) id);
        return BigDecimal.valueOf(id).stripTrailingZeros().toPlainString();
    }

    private static String extractId(String s) {
        Matcher m = Pattern.compile("\\bID:\\s*([^\\s\\)]+)").matcher(s);
        if (m.find()) return m.group(1);
        throw new IllegalStateException("Unable to parse ID from: " + s);
    }*/
}