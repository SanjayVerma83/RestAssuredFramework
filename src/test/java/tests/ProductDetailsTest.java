package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import config.EndPoints;
import io.restassured.response.Response;

public class ProductDetailsTest extends BaseTest {

    @Test
    public void shouldGetBrandsSuccessfully() {

        Response response =
                requestSpecification
                        .basePath(EndPoints.BRANDS_LIST)
                        .when()
                        .get();

        System.out.println("Status Code : " + response.statusCode());
        System.out.println("Content-Type : " + response.getContentType());
        
        System.out.println(" Brand List :");
        
        response.prettyPrint();

       response.then().spec(responseSpecification);
      
    }
}