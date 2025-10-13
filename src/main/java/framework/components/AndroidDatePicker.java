package framework.components;

import framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AndroidDatePicker extends BasePage {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final By yearHeader;
    private final By yearList;
    private final By setButton;

    public AndroidDatePicker(AndroidDriver driver, Duration explicitTimeout) {
        super(driver, explicitTimeout);
        this.yearHeader = id("android:id/date_picker_header_year");
        this.yearList = id("android:id/date_picker_year_picker");
        this.setButton = id("android:id/button1");
    }

    public void pick(By dateField, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, INPUT_FORMAT);
        click(dateField);
        waitVisible(yearHeader);

        selectYear(date.getYear());
        selectMonthAndDay(date.getMonthValue(), date.getDayOfMonth());

        click(setButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(yearHeader));
    }

    private void selectYear(int year) {
        click(yearHeader);
        waitVisible(yearList);

        String headerText = waitVisible(yearHeader).getText();
        int current;
        try {
            current = Integer.parseInt(headerText.replaceAll("\\D",""));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Year header not numeric: '" + headerText + "' (UI changed?)", e);
        }

        By yearItem = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"android:id/date_picker_year_picker\")" +
                        ".childSelector(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))");

        String scroller =
                "new UiScrollable(new UiSelector().resourceId(\"android:id/date_picker_year_picker\")).setAsVerticalList()";

        int swipes = 0, maxSwipes = 30;
        while (driver.findElements(yearItem).isEmpty() && swipes++ < maxSwipes) {
            if (year < current) {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollBackward()"));
            } else {
                driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + ".scrollForward()"));
            }
        }

        if (driver.findElements(yearItem).isEmpty()) {
            String toEnd = (year < current) ? ".flingToBeginning(10)" : ".flingToEnd(10)";
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scroller + toEnd));
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                    scroller + ".scrollIntoView(new UiSelector().className(\"android.widget.TextView\").text(\"" + year + "\"))"
            ));
        }

        click(yearItem);
        wait.until(ExpectedConditions.textToBe(yearHeader, String.valueOf(year)));
    }

    private void selectMonthAndDay(int month, int day) {
        goToMonth(month);
        clickDay(day);
    }

    private void goToMonth(int month) {
        while (true) {
            int currentMonth = getCurrentMonth();
            if (currentMonth == month) break;

            int before = currentMonth;
            click(month > currentMonth ? id("android:id/next") : id("android:id/prev"));
            wait.until(d -> getCurrentMonth() != before);
        }
    }

    private int getCurrentMonth() {
        WebElement firstOfMonth = driver.findElement(
                io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.view.View\").descriptionStartsWith(\"01 \")"
                )
        );

        String desc = firstOfMonth.getAttribute("content-desc");
        String[] parts = desc == null ? new String[0] : desc.split(" ");
        if (parts.length < 3)
            throw new IllegalStateException("Unexpected date description: '" + desc + "' (UI changed?)");

        String monthName = parts[1];
        final Month monthEnum;
        try {
            monthEnum = Month.valueOf(monthName.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new IllegalStateException("Unsupported month '" + monthName + "' in '" + desc + "'. Device language/UI likely changed.", e);
        }
        return monthEnum.getValue();
    }

    private void clickDay(int day) {
        By dayLocator = io.appium.java_client.AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + day + "\")"
        );
        click(dayLocator);
    }
}
