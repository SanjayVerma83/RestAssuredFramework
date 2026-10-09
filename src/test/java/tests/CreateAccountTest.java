package tests;

import java.util.Map;

import org.testng.annotations.Test;

import base.BaseTest;
import config.EndPoints;
import io.restassured.response.Response;
import payload.PayloadBuilder;


public class CreateAccountTest extends BaseTest{

    @Test
    public void shouldCreateNewUserAccount() {
    	
    	
        Map<String, String> accountDetails =
                PayloadBuilder.createAccountPayload();

        Response response =
        		requestSpecification
                        .basePath(EndPoints.CREATE_ACCOUNT)
                        .formParams(accountDetails)
                        .when()
                        .post();

        System.out.println("Status Code : " + response.statusCode());

        response.prettyPrint();
        
        response.then().spec(responseSpecification);

        
    }
}