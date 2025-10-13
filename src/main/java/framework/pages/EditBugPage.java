package framework.pages;

import framework.base.BasePage;
import framework.components.AndroidDatePicker;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

public class EditBugPage extends BasePage {
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
     * Constructs the CreateBugPage.
     * @param driver Android driver for interactions (non-null)
     * @param explicitTimeout max wait per explicit condition
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
     * Verifies that the Create Bug page is displayed.
     * @return true if page is visible, false otherwise
     */
    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }


    /**
     * Selects a date using the date picker.
     * @param date date in dd/MM/yyyy format
     * @return this CreateBugPage instance
     */
    public EditBugPage pickDate(String date) {
        scrollIfNeeded(bugDate);
        datePicker.pick(bugDate, date);
        return this;
    }

    /**
     * Enters the bug title.
     * @param value bug title
     * @return this CreateBugPage instance
     */
    public EditBugPage enterTitle(String value) {
        scrollIfNeeded(bugTitle);
        type(bugTitle, normalize(value));
        return this;
    }

    /**
     * Enters the steps to recreate the bug.
     * @param value steps description
     * @return this CreateBugPage instance
     */
    public EditBugPage enterSteps(String value) {
        scrollIfNeeded(bugSteps);
        type(bugSteps, normalize(value));
        return this;
    }

    /**
     * Enters the expected result.
     * @param value expected result description
     * @return this CreateBugPage instance
     */
    public EditBugPage enterExpected(String value) {
        scrollIfNeeded(bugExpectedResult);
        type(bugExpectedResult, normalize(value));
        return this;
    }

    /**
     * Enters the actual result.
     * @param value actual result description
     * @return this CreateBugPage instance
     */
    public EditBugPage enterActual(String value) {
        scrollIfNeeded(bugActualResult);
        type(bugActualResult, normalize(value));
        return this;
    }

    /**
     * Sets the bug status from dropdown.
     * @param value status option (e.g., "Open", "Fixed", "Closed")
     * @return this CreateBugPage instance
     */
    public EditBugPage setStatus(String value) {
        scrollIfNeeded(bugStatus);
        return selectDropdownOption(bugStatus, normalize(value), "Status");
    }

    /**
     * Sets the bug severity from dropdown.
     * @param value severity option (e.g., "Critical", "Major", "Minor")
     * @return this CreateBugPage instance
     */
    public EditBugPage setSeverity(String value) {
        scrollIfNeeded(bugSeverity);
        return selectDropdownOption(bugSeverity, normalize(value), "Severity");
    }

    /**
     * Sets the bug priority from dropdown.
     * @param value priority option (e.g., "High", "Medium", "Low")
     * @return this CreateBugPage instance
     */
    public EditBugPage setPriority(String value) {
        scrollIfNeeded(bugPriority);
        return selectDropdownOption(bugPriority, normalize(value), "Priority");
    }

    /**
     * Enters the name of the person who detected the bug.
     * @param value detector name
     * @return this CreateBugPage instance
     */
    public EditBugPage setDetectedBy(String value) {
        scrollIfNeeded(bugDetectedBy);
        type(bugDetectedBy, normalize(value));
        return this;
    }

    /**
     * Enters the name of the person who fixed the bug.
     * @param value fixer name
     * @return this CreateBugPage instance
     */
    public EditBugPage setFixedBy(String value) {
        scrollIfNeeded(bugFixedBy);
        type(bugFixedBy, normalize(value));
        return this;
    }

    /**
     * Selects the date the bug was closed.
     * @param date date in dd/MM/yyyy format
     * @return this CreateBugPage instance
     */
    public EditBugPage pickDateClosed(String date) {
        scrollIfNeeded(bugDateClosed);
        datePicker.pick(bugDateClosed, date);
        return this;
    }
    private void scrollIfNeeded(By element) {
        if(!isVisible(element)) {
            scroll("down");
        }
        if(!isVisible(element)) {
            scroll("up");
        }
    }

    /**
     * Submits the edited bug form.
     */
    public void save() {
        scrollIfNeeded(saveChanges);
        click(saveChanges);
    }

    /**
     * Clicking Cancel changes
     */
    public void cancel() {
        scrollIfNeeded(cancelEditing);
        click(cancelEditing);
    }
    /**
     * Selects an option from a dropdown/spinner field.
     * @param field the dropdown field locator
     * @param value the option text to select
     * @param fieldName field name for error messages
     * @return this CreateBugPage instance
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
