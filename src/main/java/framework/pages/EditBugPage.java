package framework.pages;

import framework.base.BasePage;
import framework.components.AndroidDatePicker;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Page object for the "Edit Bug" screen.
 * Provides methods to modify existing bug details and save or cancel changes.
 */
public class EditBugPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(EditBugPage.class);

    private final By screenRoot;
    private final By bugTitle;
    private final By bugSteps;
    private final By bugExpectedResult;
    private final By bugActualResult;
    private final By bugStatus;
    private final By bugSeverity;
    private final By bugPriority;
    private final By bugDetectedBy;
    private final By bugFixedBy;
    private final By bugDate;
    private final By bugDateClosed;
    private final By bugFile;
    private final By saveChanges;
    private final By cancelEditing;
    private final AndroidDatePicker datePicker;


    /**
     * Constructs the EditBugPage with the given driver and timeout.
     *
     * @param driver the Android driver instance
     * @param explicitTimeout maximum wait time for page elements
     */
    public EditBugPage(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);

        this.screenRoot = textElement("Edit Bug");
        this.bugTitle = resourceId("editBugTitle");
        this.bugSteps = resourceId("editBugSteps");
        this.bugExpectedResult = resourceId("editBugExpectedResult");
        this.bugActualResult = resourceId("editBugActualResult");
        this.bugStatus = resourceId("editBugStatus");
        this.bugSeverity = resourceId("editBugSeverity");
        this.bugPriority = resourceId("editBugPriority");
        this.bugDetectedBy = resourceId("editBugDetectedBy");
        this.bugFixedBy = resourceId("editBugFixedBy");
        this.bugDate = resourceId("editBugDate");
        this.bugDateClosed = resourceId("editBugDateClosed");
        this.bugFile = resourceId("editBugFile");
        this.saveChanges = textElement("Save Changes");
        this.cancelEditing = textElement("Cancel Editing");
        this.datePicker = new AndroidDatePicker(driver, explicitTimeout);
    }

    /**
     * Verifies that the Edit Bug page is currently displayed.
     *
     * @return true if the page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    /**
     * Selects the bug detection date using the date picker.
     *
     * @param date date in dd/MM/yyyy format
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage pickDate(String date) {
        scrollIntoViewIfNeeded(bugDate);
        datePicker.pick(bugDate, date);
        return this;
    }

    /**
     * Updates the bug title field.
     *
     * @param value the new bug title
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage enterTitle(String value) {
        scrollIntoViewIfNeeded(bugTitle);
        type(bugTitle, normalize(value));
        return this;
    }

    /**
     * Updates the steps to recreate the bug.
     *
     * @param value the new steps description
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage enterSteps(String value) {
        scrollIntoViewIfNeeded(bugSteps);
        type(bugSteps, normalize(value));
        return this;
    }

    /**
     * Updates the expected result field.
     *
     * @param value the new expected result
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage enterExpected(String value) {
        scrollIntoViewIfNeeded(bugExpectedResult);
        type(bugExpectedResult, normalize(value));
        return this;
    }

    /**
     * Updates the actual result field.
     *
     * @param value the new actual result
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage enterActual(String value) {
        scrollIntoViewIfNeeded(bugActualResult);
        type(bugActualResult, normalize(value));
        return this;
    }

    /**
     * Changes the bug status via dropdown selection.
     *
     * @param value the new status (e.g., "Open", "Fixed", "Closed")
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage setStatus(String value) {
        scrollIntoViewIfNeeded(bugStatus);
        return selectDropdownOption(bugStatus, normalize(value), "Status");
    }

    /**
     * Changes the bug severity via dropdown selection.
     *
     * @param value the new severity (e.g., "Critical", "Major", "Minor")
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage setSeverity(String value) {
        scrollIntoViewIfNeeded(bugSeverity);
        return selectDropdownOption(bugSeverity, normalize(value), "Severity");
    }

    /**
     * Changes the bug priority via dropdown selection.
     *
     * @param value the new priority (e.g., "High", "Medium", "Low")
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage setPriority(String value) {
        scrollIntoViewIfNeeded(bugPriority);
        return selectDropdownOption(bugPriority, normalize(value), "Priority");
    }

    /**
     * Updates the name of the person who detected the bug.
     *
     * @param value the detector's name
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage setDetectedBy(String value) {
        scrollIntoViewIfNeeded(bugDetectedBy);
        type(bugDetectedBy, normalize(value));
        return this;
    }

    /**
     * Updates the name of the person who fixed the bug.
     *
     * @param value the fixer's name
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage setFixedBy(String value) {
        scrollIntoViewIfNeeded(bugFixedBy);
        type(bugFixedBy, normalize(value));
        return this;
    }

    /**
     * Selects the date the bug was closed using the date picker.
     *
     * @param date date in dd/MM/yyyy format
     * @return this EditBugPage instance for method chaining
     */
    public EditBugPage pickDateClosed(String date) {
        scrollIntoViewIfNeeded(bugDateClosed);
        datePicker.pick(bugDateClosed, date);
        return this;
    }

    /**
     * Saves all changes made to the bug.
     */
    public void save() {
        logger.info("Saving bug changes");
        scrollIntoViewIfNeeded(saveChanges);
        click(saveChanges);
    }
    /**
     * Cancels editing and discards all changes.
     */
    public void cancel() {
        logger.info("Canceling bug edit");
        scrollIntoViewIfNeeded(cancelEditing);
        click(cancelEditing);
    }

    /**
     * Retrieves the text content of any element by its locator.
     * Useful for reading dropdown values or field contents.
     *
     * @param locator the element locator
     * @return the element's text content
     */
    public String getTextByLocator(By locator) {
        return text(locator);
    }

    /**
     * Selects an option from a dropdown/spinner field.
     *
     * @param field the dropdown field locator
     * @param value the option text to select (will be normalized)
     * @param fieldName field name used in error messages
     * @return this EditBugPage instance for method chaining
     * @throws IllegalArgumentException if value is null or blank
     * @throws AssertionError if the specified option cannot be found
     */
    private EditBugPage selectDropdownOption(By field, String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " value must not be blank");
        }

        click(field);

        By option = textElement(value);
        if (!isVisible(option)) {
            try {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().text(\"" + value + "\"))"));
            } catch (NoSuchElementException e) {
                throw new AssertionError(fieldName + " option not found: '" + value + "'", e);
            }
        }
        click(option);
        return this;
    }
}
