
package tests;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import config.EndPoints;
import payload.PayloadBuilder;
import io.restassured.response.Response;

public class ApiChainingTest extends BaseTest {

    private String email;
    private String password;
    private boolean accountCreated = false;

    @Test
    public void createAccountAndVerifyLogin() throws Exception {

        // Step 1: Get account details
        Map<String, String> accountDetails =
                PayloadBuilder.createAccountPayload();

        email = accountDetails.get("email");
        password = accountDetails.get("password");

        System.out.println("Account email: " + email);

        ObjectMapper mapper = new ObjectMapper();

        // Step 2: Create account
        Response createResponse = requestSpecification
                .formParams(accountDetails)
                .when()
                .post(EndPoints.CREATE_ACCOUNT);

        createResponse.then().spec(responseSpecification);

        String createBody = createResponse.getBody().asString();
        String createJson = extractJson(createBody);
        JsonNode createResult = mapper.readTree(createJson);

        Assert.assertEquals(
                createResult.get("responseCode").asInt(), 201);

        accountCreated = true;

        System.out.println("Account creation completed.");

        // Step 3: Verify login
        Response loginResponse = requestSpecification
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post(EndPoints.VERIFY_LOGIN);

        loginResponse.then().spec(responseSpecification);

        // Step 4: Validate login response
        JsonNode loginResult = mapper.readTree(
                extractJson(loginResponse.getBody().asString()));

        Assert.assertEquals(
                loginResult.get("responseCode").asInt(), 200);

        Assert.assertEquals(
                loginResult.get("message").asText(), "User exists!");

        System.out.println("Login verified successfully.");

        // Step 5: Get user details
        Response userDetailsResponse = requestSpecification
                .queryParam("email", email)
                .when()
                .get(EndPoints.GET_USER_DETAILS);

        userDetailsResponse.then().spec(responseSpecification);

        JsonNode userDetailsResult = mapper.readTree(
                extractJson(userDetailsResponse.getBody().asString()));

        String returnedEmail =
                userDetailsResult.get("user").get("email").asText();

        Assert.assertEquals(returnedEmail, email);

        System.out.println("User email validation passed.");
    }

    // Step 6: Automatically clean up the test account
    @AfterMethod(alwaysRun = true)
    public void deleteTestAccount() throws Exception {

        if (!accountCreated) {
            System.out.println(
                    "Cleanup skipped: account was not confirmed as created.");
            return;
        }

        Response deleteResponse = requestSpecification
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .delete(EndPoints.DELETE_ACCOUNT);

        deleteResponse.then().spec(responseSpecification);

        JsonNode deleteResult = new ObjectMapper().readTree(
                extractJson(deleteResponse.getBody().asString()));

        Assert.assertEquals(
                deleteResult.get("responseCode").asInt(), 200);

        Assert.assertEquals(
                deleteResult.get("message").asText(),
                "Account deleted!");

        accountCreated = false;

        System.out.println("Cleanup successful: test account deleted.");
    }

    // Extract JSON from the API's HTML-wrapped response
    private String extractJson(String body) {

        int start = body.indexOf("{");
        int end = body.lastIndexOf("}");

        if (start < 0 || end < start) {
            throw new IllegalStateException(
                    "Response does not contain valid JSON content.");
        }

        return body.substring(start, end + 1);
    }
}
