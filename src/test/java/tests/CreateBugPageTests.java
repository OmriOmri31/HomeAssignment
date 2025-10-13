package tests;

import framework.driver.DriverFactory;
import framework.pages.HomePage;
import framework.pages.CreateBugPage;
import framework.pages.ViewBugsPage;
import framework.pages.EditBugPage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CreateBugPageTests {

    private AndroidDriver driver;
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String today() {
        return LocalDate.now().format(DMY);
    }

    @BeforeEach
    void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    @DisplayName("Create a bug with all fields")
    void createBugWithAllFields() throws InterruptedException {
            HomePage home = new HomePage(driver, TIMEOUT);
            CreateBugPage newBug;
            ViewBugsPage viewBug;
            EditBugPage edit = new EditBugPage(driver, TIMEOUT);

            String bugId = "" + System.currentTimeMillis();
            String bugTitle = "Best band?";

            assertTrue(home.assertOnPage(), "Create Bug page should be visible");
            newBug = home.clickCreateBug();
            assertTrue(newBug.assertOnPage(), "Create Bug page should be visible");
            newBug.enterBugId(bugId)
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

            viewBug = newBug.clickViewBugs();
            assertTrue(viewBug.assertOnPage(), "Should navigate to ViewBugs page .");
            viewBug.clickButtonAll();
            viewBug.searchForBugs(bugTitle);
            viewBug.scrollIntoViewIfNeeded(
                    AppiumBy.androidUIAutomator("new UiSelector().textContains(\"(ID: " + bugId + "\")")
            );
            Thread.sleep(1000);
            viewBug.editBugById(bugId);

            assertTrue(edit.assertOnPage(), "Edit page should be visible for the created bug");
            viewBug.clickHome();
            assertTrue(home.assertOnPage(), "Returned to HomePage after test completed");

    }
}
