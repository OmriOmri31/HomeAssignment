package tests.base;

import framework.driver.DriverFactory;
import framework.pages.CreateBugPage;
import framework.pages.EditBugPage;
import framework.pages.HomePage;
import framework.pages.ViewBugsPage;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    protected AndroidDriver driver;
    protected static final Duration TIMEOUT = Duration.ofSeconds(15);
    protected static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private HomePage homePage;
    private CreateBugPage createBugPage;
    private ViewBugsPage viewBugsPage;
    private EditBugPage editBugPage;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.getDriver();
        resetPageObjects();
        ensureHomePage();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
        resetPageObjects();
    }

    protected void ensureHomePage() {
        Duration shortTimeout = Duration.ofSeconds(1);
        HomePage home = new HomePage(driver, shortTimeout);
        CreateBugPage create = new CreateBugPage(driver, shortTimeout);
        ViewBugsPage view = new ViewBugsPage(driver, shortTimeout);
        EditBugPage edit = new EditBugPage(driver, shortTimeout);

        if (home.assertOnPage()) {
            return;
        } else if (create.assertOnPage()) {
            create.clickHome();
        } else if (view.assertOnPage()) {
            view.clickHome();
        } else if (edit.assertOnPage()) {
            edit.clickHome();
        } else {
            home.scroll("up");
            home.scroll("up");
            home.scroll("up");
        }

        waitFor(500);
    }

    protected HomePage navigateToHomePage() {
        ensureHomePage();
        return getHomePage();
    }

    protected HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(driver, TIMEOUT);
        }
        return homePage;
    }

    protected CreateBugPage getCreateBugPage() {
        if (createBugPage == null) {
            createBugPage = new CreateBugPage(driver, TIMEOUT);
        }
        return createBugPage;
    }

    protected ViewBugsPage getViewBugsPage() {
        if (viewBugsPage == null) {
            viewBugsPage = new ViewBugsPage(driver, TIMEOUT);
        }
        return viewBugsPage;
    }

    protected EditBugPage getEditBugPage() {
        if (editBugPage == null) {
            editBugPage = new EditBugPage(driver, TIMEOUT);
        }
        return editBugPage;
    }

    private void resetPageObjects() {
        homePage = null;
        createBugPage = null;
        viewBugsPage = null;
        editBugPage = null;
    }

    protected String generateUniqueBugId() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected String today() {
        return LocalDate.now().format(DMY);
    }

    protected String yesterday() {
        return LocalDate.now().minusDays(1).format(DMY);
    }

    protected String formatDate(LocalDate date) {
        return date.format(DMY);
    }

    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}