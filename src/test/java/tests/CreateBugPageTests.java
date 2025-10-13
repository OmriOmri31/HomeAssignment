package tests;

import framework.driver.DriverFactory;
import framework.pages.HomePage;
import framework.pages.CreateBugPage;
import framework.pages.ViewBugsPage;
import framework.pages.EditBugPage;
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
    void createBugWithAllFields() {
        //pages
        HomePage home = new HomePage(driver, TIMEOUT);
        CreateBugPage newBug;
        ViewBugsPage viewBug;
        EditBugPage edit = new EditBugPage(driver, TIMEOUT);

        // Unique id so the test is repeatable
        String bugId = "" + System.currentTimeMillis();

        // Home -> Create Bug
        assertTrue(home.verifyHomePage(), "Create Bug page should be visible");
        newBug = home.clickCreateBug();
        assertTrue(newBug.assertOnPage(), "Create Bug page should be visible");
        //Entering fields
        newBug.enterBugId(bugId) //time signature for this particular test
                .pickDate("01/09/2014")
                .enterTitle("Test Title  :)")
                .enterSteps("1. Take a deep breath \n2. Cry")
                .enterExpected("Queen")
                .enterActual("Beatles")
                .setStatus("Not a Bug")
                .setSeverity("Trivial")
                .setPriority("Critical")
                .setDetectedBy("Joe Biden")
                .setFixedBy("Donald J. Trump")
                .pickDateClosed(today())
                .submit();

        //CreateBug -> View Bugs
        viewBug = newBug.clickViewBugs();

        assertTrue(viewBug.assertOnPage(), "Should navigate to ViewBugs page .");
        viewBug.clickButtonAll();

        // Verify the new bug exists by ID
        viewBug.editBugById(bugId);  // should navigate to Edit page for that row

        // Back out without persisting an edit
        assertTrue(edit.assertOnPage(), "Edit page should be visible for the created bug");
    }
}
