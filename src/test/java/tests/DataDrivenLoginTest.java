
package tests;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.BaseTest;
import config.EndPoints;
import payload.PayloadBuilder;
import io.restassured.response.Response;

public class DataDrivenLoginTest extends BaseTest {

    private String validEmail;
    private String validPassword;
    private boolean accountCreated = false;

    @BeforeClass
    public void createTestAccount() throws Exception {

        Map<String, String> accountDetails =
                PayloadBuilder.createAccountPayload();

        validEmail = accountDetails.get("email");
        validPassword = accountDetails.get("password");

        Response response = requestSpecification
                .formParams(accountDetails)
                .when()
                .post(EndPoints.CREATE_ACCOUNT);

        response.then().spec(responseSpecification);

        String json = extractJson(response.getBody().asString());
        JsonNode result = new ObjectMapper().readTree(json);

        assertEquals(result.get("responseCode").asInt(), 201);

        accountCreated = true;

        System.out.println("Test account created successfully.");
    }



@DataProvider(name = "loginData")
public Object[][] getLoginData() throws Exception {

    List<Object[]> testData = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                    getClass().getClassLoader()
                            .getResourceAsStream("testdata/loginData.csv"),
                    StandardCharsets.UTF_8))) {

        String line;
        boolean header = true;

        while ((line = reader.readLine()) != null) {

            if (header) {
                header = false;
                continue;
            }

            String[] values = line.split(",", -1);

            String emailType = values[0].trim();
            String passwordType = values[1].trim();
            int expectedCode = Integer.parseInt(values[2].trim());
            String expectedMessage = values[3].trim();

            String email = emailType.equals("VALID")
                    ? validEmail
                    : "invalid" + System.currentTimeMillis() + "@test.com";

            String password = passwordType.equals("VALID")
                    ? validPassword
                    : "WrongPass123";

            testData.add(new Object[] {
                    email, password, expectedCode, expectedMessage
            });
        }
    }

    return testData.toArray(new Object[0][]);
}


    @Test(dataProvider = "loginData")
    public void verifyLoginWithMultipleData(
            String email,
            String password,
            int expectedCode,
            String expectedMessage) throws Exception {

        Response response = requestSpecification
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post(EndPoints.VERIFY_LOGIN);

        response.then().spec(responseSpecification);

        String json = extractJson(response.getBody().asString());
        JsonNode result = new ObjectMapper().readTree(json);

        assertEquals(result.get("responseCode").asInt(), expectedCode);
        assertEquals(result.get("message").asText(), expectedMessage);

        System.out.println("Login scenario passed for: " + email);
    }

    @AfterClass(alwaysRun = true)
    public void deleteTestAccount() throws Exception {

        if (!accountCreated) {
            return;
        }

        Response response = requestSpecification
                .formParam("email", validEmail)
                .formParam("password", validPassword)
                .when()
                .delete(EndPoints.DELETE_ACCOUNT);

        response.then().spec(responseSpecification);

        String json = extractJson(response.getBody().asString());
        JsonNode result = new ObjectMapper().readTree(json);

        assertEquals(result.get("responseCode").asInt(), 200);
        assertEquals(result.get("message").asText(), "Account deleted!");

        accountCreated = false;

        System.out.println("Test account deleted successfully.");
    }

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
