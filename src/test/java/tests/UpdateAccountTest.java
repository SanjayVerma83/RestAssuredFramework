
package tests;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.BaseTest;
import config.EndPoints;
import payload.PayloadBuilder;
import io.restassured.response.Response;

public class UpdateAccountTest extends BaseTest {

    private String email;
    private String password;
    private boolean accountCreated = false;

    @Test
    public void verifyUpdateAccount() throws Exception {

        // Step 1: Prepare account details
        Map<String, String> accountDetails =
                PayloadBuilder.createAccountPayload();

        email = accountDetails.get("email");
        password = accountDetails.get("password");

        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Account email: " + email);

        // Step 2: Create account
        Response createResponse = given()
                .spec(requestSpecification)
                .formParams(accountDetails)
                .when()
                .post(EndPoints.CREATE_ACCOUNT);

        createResponse.then().spec(responseSpecification);

        JsonNode createResult = mapper.readTree(
                extractJson(createResponse.getBody().asString()));

        Assert.assertEquals(
                createResult.get("responseCode").asInt(), 201);

        accountCreated = true;
        System.out.println("Account created successfully.");

        // Step 3: Update first name
        accountDetails.put("firstname", "UpdatedSanjay");

        Response updateResponse = given()
                .spec(requestSpecification)
                .formParams(accountDetails)
                .when()
                .put(EndPoints.UPDATE_ACCOUNT);

        updateResponse.then().spec(responseSpecification);

        JsonNode updateResult = mapper.readTree(
                extractJson(updateResponse.getBody().asString()));

        Assert.assertEquals(
                updateResult.get("responseCode").asInt(), 200);

        Assert.assertEquals(
                updateResult.get("message").asText(), "User updated!");

        System.out.println("Account updated successfully.");

        // Step 4: Retrieve updated account details
        Response getResponse = given()
                .spec(requestSpecification)
                .queryParam("email", email)
                .when()
                .get(EndPoints.GET_USER_DETAILS);

        getResponse.then().spec(responseSpecification);

        JsonNode userDetails = mapper.readTree(
                extractJson(getResponse.getBody().asString()));

        // Step 5: Verify updated first name
        String updatedFirstName =
                userDetails.get("user").get("first_name").asText();

        Assert.assertEquals(updatedFirstName, "UpdatedSanjay");

        System.out.println("Updated first name verified successfully.");
    }

    // Step 6: Automatically delete the test account
    @AfterMethod(alwaysRun = true)
    public void deleteTestAccount() throws Exception {

        if (!accountCreated) {
            return;
        }

        Response deleteResponse = given()
                .spec(requestSpecification)
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
                deleteResult.get("message").asText(), "Account deleted!");

        accountCreated = false;

        System.out.println("Test account deleted successfully.");
    }

    // Extract JSON from HTML-wrapped API response
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
