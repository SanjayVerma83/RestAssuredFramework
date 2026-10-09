package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pojo.ProductRequest;

public class ProductSerializationTest {

    @Test
    public void shouldSerializeProductPojoIntoJson() throws JsonProcessingException {

        // Create a ProductRequest object
        ProductRequest productRequest = new ProductRequest();

        productRequest.setId(1);
        productRequest.setName("Blue Top");
        productRequest.setPrice("Rs. 500");
        productRequest.setBrand("Polo");

        // Convert the Java object into a JSON String
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonPayload = objectMapper.writeValueAsString(productRequest);

        System.out.println("Serialized JSON:");
        System.out.println(
                objectMapper.readTree(jsonPayload).toPrettyString());
        
        JsonNode jsonNode = objectMapper.readTree(jsonPayload);

        Assert.assertEquals(jsonNode.get("id").asInt(), 1);
        Assert.assertEquals(jsonNode.get("name").asText(), "Blue Top");
        Assert.assertEquals(jsonNode.get("price").asText(), "Rs. 500");
        Assert.assertEquals(jsonNode.get("brand").asText(), "Polo");
      
    }
}