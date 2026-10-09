
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import config.EndPoints;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

public class LoginTest extends BaseTest {

    @Test
    public void shouldVerifyLoginWithValidCredentials() {

        // Send login request using form parameters
        Response response =
                requestSpecification
                        .basePath(EndPoints.VERIFY_LOGIN)
                        .formParam("email", "sanjay789@test.com")
                        .formParam("password", "Test@123")
                        .when()
                        .post();

        // Validate HTTP status using existing framework specification
        response.then()
                .spec(responseSpecification);

        // Read the JSON response body
        JsonPath jsonPath =
                JsonPath.from(response.asString());

        // Validate the API response
        Assert.assertEquals(jsonPath.getInt("responseCode"), 200);
        Assert.assertEquals(
                jsonPath.getString("message"),
                "User exists!"
        );

        System.out.println("Login verification successful");
        System.out.println(
                "Response Code: " +
                jsonPath.getInt("responseCode")
        );
        System.out.println(
                "Message: " + jsonPath.getString("message")
        );
    }
        @Test
        public void shouldRejectLoginWithInvalidCredentials() {

            Response response =
                    requestSpecification
                            .basePath(EndPoints.VERIFY_LOGIN)
                            .formParam("email", "invaliduser@example.com")
                            .formParam("password", "WrongPassword123")
                            .when()
                            .post();

            // Validate HTTP status using the existing framework
            response.then()
                    .spec(responseSpecification);

            // Read the JSON response
            JsonPath jsonPath = JsonPath.from(response.asString());

            // Validate the API-level error response
            Assert.assertEquals(jsonPath.getInt("responseCode"), 404);
            Assert.assertEquals(
                    jsonPath.getString("message"),
                    "User not found!"
            );

            System.out.println("Invalid login rejected successfully");
            System.out.println("Response Code: " + jsonPath.getInt("responseCode"));
            System.out.println("Message: " + jsonPath.getString("message"));
        }
    
}
