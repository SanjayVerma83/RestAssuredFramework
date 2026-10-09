package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import config.EndPoints;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import pojo.Product;
import pojo.ProductResponse;

public class ProductTest extends BaseTest{

    @Test
    public void shouldGetProductsSuccessfully() {

    	 Response response =
                 requestSpecification
                         .basePath(EndPoints.PRODUCTS_LIST)
                         .when()
                         .get();

        System.out.println("Status Code  : " + response.statusCode());
        System.out.println("Content-Type : " + response.getContentType());
        
        System.out.println(" Product List :");

        response.prettyPrint();

        response.then().spec(responseSpecification);
    }
    
    @Test
    public void shouldDeserializeProductsResponseIntoPojo() {

        Response response =
                requestSpecification
                        .basePath(EndPoints.PRODUCTS_LIST)
                        .when()
                        .get();

        // Reuse the existing response specification
        response.then()
                .spec(responseSpecification);

        // Convert the JSON response into ProductResponse POJO
        
        ObjectMapperType mapperType = ObjectMapperType.JACKSON_2;

        ProductResponse productResponse =
                response.as(ProductResponse.class, mapperType);
        
   
        // Print the response code
        System.out.println(
                "Response Code: " + productResponse.getResponseCode()
        );

        // Print the total number of products
        System.out.println(
                "Total Products: " + productResponse.getProducts().size()
        );

        // Print the first product's details
        Product product = productResponse.getProducts().get(0);

        System.out.println("Product ID: " + product.getId());
        System.out.println("Product Name: " + product.getName());
        System.out.println("Product Price: " + product.getPrice());
        System.out.println("Product Brand: " + product.getBrand());

        // Access nested Category and UserType objects
        System.out.println(
                "Category: " + product.getCategory().getCategory()
        );

        System.out.println(
                "User Type: " +
                product.getCategory().getUsertype().getUsertype()
        );
        
        response.then().spec(responseSpecification);
        
     // Verify the API response code
        Assert.assertEquals(productResponse.getResponseCode(), 200);

        // Verify the product list is not empty
        Assert.assertFalse(productResponse.getProducts().isEmpty());

        // Verify the first product's details
        Assert.assertEquals(product.getId(), 1);
        Assert.assertEquals(product.getName(), "Blue Top");
        Assert.assertEquals(product.getPrice(), "Rs. 500");
        Assert.assertEquals(product.getBrand(), "Polo");

        // Verify nested category and user type
        Assert.assertEquals(product.getCategory().getCategory(), "Tops");

        Assert.assertEquals(
                product.getCategory().getUsertype().getUsertype(),
                "Women"
        );
        
     // Find product with ID 2
        
        Product selectedProduct=null;
        
        for(Product productItem: productResponse.getProducts())
        {
        	if(productItem.getId()==2)
        	{
        		selectedProduct = productItem;
                break;
        	}
        }
        
        // Verify the selected product
        Assert.assertNotNull(selectedProduct);

        Assert.assertEquals(selectedProduct.getName(), "Men Tshirt");

        System.out.println("Selected Product ID: " + selectedProduct.getId());
        System.out.println("Selected Product Name: " + selectedProduct.getName());
        
    }
}