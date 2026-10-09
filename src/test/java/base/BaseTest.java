package base;

import org.testng.annotations.BeforeClass;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseTest {

    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {

    	ConfigManager configManager = new ConfigManager();
        requestSpecification =
                RestAssured
                        .given()
                        .baseUri(configManager.getProperty("baseUrl"));
        
        responseSpecification =
                RestAssured
                        .expect()
                        .statusCode(200);
    }
}