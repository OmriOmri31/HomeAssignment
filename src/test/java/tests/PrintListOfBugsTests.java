package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintListOfBugsTests extends BaseTest {

    @Test
    @DisplayName("Print all of the bug list")
    void printAllBugs() throws InterruptedException {
        assertTrue(getHomePage().assertOnPage(), "Home page should be visible");

        getHomePage().clickViewBugs();
        assertTrue(getViewBugsPage().assertOnPage(), "View Bug page should be visible");

        getViewBugsPage().clickButtonAll();

        int timesToScroll = 3;
        for (int i = 0; i < timesToScroll; i++) {
            getViewBugsPage().getBugList();
            getViewBugsPage().scroll("down");
        }

        String[] bugList = getViewBugsPage().getBugList();

        System.out.println("=== Bug List (" + bugList.length + " bugs) ===");
        for (String bug : bugList) {
            System.out.println(bug);
        }
        System.out.println("=== End of Bug List ===");
    }
}