
package tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import base.BaseTest;
import config.EndPoints;
import io.restassured.response.Response;

public class GetApiTest extends BaseTest {

    @Test
    public void verifyProductsListAndSchema() throws Exception {

        // Step 1: Send GET request
        Response response = requestSpecification
                .when()
                .get(EndPoints.PRODUCTS_LIST);

        // Step 2: Validate HTTP response
        response.then().spec(responseSpecification);

        // Step 3: Extract JSON from the HTML response
        String body = response.getBody().asString();

        int start = body.indexOf("{");
        int end = body.lastIndexOf("}");

        Assert.assertTrue(
                start >= 0 && end > start,
                "Response does not contain valid JSON content"
        );

        String json = body.substring(start, end + 1);

        // Step 4: Validate JSON schema
        assertThat(
                json,
                matchesJsonSchemaInClasspath(
                        "schemas/products-schema.json")
        );

        System.out.println("JSON schema validation passed.");

        // Step 5: Validate response values
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(com.fasterxml.jackson.databind.JsonNode.class);
        com.fasterxml.jackson.databind.JsonNode result =
                reader.readValue(json);

        Assert.assertEquals(
                result.get("responseCode").asInt(), 200);

        com.fasterxml.jackson.databind.JsonNode products =
                result.get("products");

        Assert.assertTrue(products.isArray());
        Assert.assertTrue(products.size() > 0);

        // Verify the first product
        Assert.assertEquals(
                products.get(0).get("name").asText(), "Blue Top");

        Assert.assertEquals(
                products.get(0).get("price").asText(), "Rs. 500");

        System.out.println("Products API validation passed.");
        System.out.println("Total products: " + products.size());
    }
}
