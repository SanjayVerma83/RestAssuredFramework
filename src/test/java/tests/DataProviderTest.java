
package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {

        return new Object[][] {
            {"validUser@test.com", "ValidPass123"},
            {"invalidUser@test.com", "WrongPass123"},
            {"anotherUser@test.com", "TestPass456"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyLoginTest(String email, String password) {

        System.out.println("Testing email: " + email);
        System.out.println("Testing login scenario");
        System.out.println("----------------------");
    }
}
