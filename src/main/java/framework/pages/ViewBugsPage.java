package framework.pages;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
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

    public boolean assertOnPage() {
        return assertOnPage(screenRoot);
    }

    public ViewBugsPage searchForBugs(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        return this;
    }

    public ViewBugsPage clickButtonAll() {
        click(buttonAll);
        updateBugList();
        return this;
    }

    public ViewBugsPage clickButtonOpen() {
        click(buttonOpen);
        updateBugList();
        return this;
    }

    public ViewBugsPage clickButtonFixed() {
        click(buttonFixed);
        updateBugList();
        return this;
    }

    public ViewBugsPage clickButtonClosed() {
        click(buttonClosed);
        updateBugList();
        return this;
    }

    public ViewBugsPage clickNotABug() {
        click(notABug);
        updateBugList();
        return this;
    }

    private void updateBugList() {
        WebElement list = waitVisible(bugListLocator);
        wait.until(d -> !list.findElements(io.appium.java_client.AppiumBy.className("android.widget.TextView")).isEmpty());

        String[] visibleBugs = list.findElements(io.appium.java_client.AppiumBy.className("android.widget.TextView"))
                .stream()
                .map(org.openqa.selenium.WebElement::getText)
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

    public ViewBugsPage editBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Edit\").instance(0)"));
        return this;
    }

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

    public ViewBugsPage deleteBugByTitle(String value) {
        type(searchBugs, normalize(value));
        updateBugList();
        click(io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Delete\").instance(0)"));
        return this;
    }

    public ViewBugsPage deleteBugById(String idText) {
        By row = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")");
        scrollIntoViewIfNeeded(row);
        By deleteForRow = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"(ID: " + idText + "\")" +
                        ".fromParent(new UiSelector().text(\"Delete\"))");
        click(deleteForRow);
        return this;
    }

    @Deprecated
    public ViewBugsPage editBugById(double id) { return editBugById(formatId(id)); }

    @Deprecated
    public ViewBugsPage deleteBugById(double id) { return deleteBugById(formatId(id)); }

    public String[] getBugList() {
        updateBugList();
        return bugList;
    }

    public int getBugCount() {
        return bugList.length;
    }

    private static String formatId(double id) {
        if (id == Math.rint(id)) return String.valueOf((long) id);
        return BigDecimal.valueOf(id).stripTrailingZeros().toPlainString();
    }

    private static String extractId(String s) {
        Matcher m = Pattern.compile("\\bID:\\s*([^\\s\\)]+)").matcher(s);
        if (m.find()) return m.group(1);
        throw new IllegalStateException("Unable to parse ID from: " + s);
    }
}