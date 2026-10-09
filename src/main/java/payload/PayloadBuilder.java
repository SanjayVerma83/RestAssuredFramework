package payload;

import java.util.HashMap;
import java.util.Map;

public class PayloadBuilder {

    public static Map<String, String> createAccountPayload() {

        Map<String, String> accountDetails = new HashMap<>();

        accountDetails.put("name", "Sanjay");
        accountDetails.put("email", "sanjay"+ System.currentTimeMillis()+"@test.com");
        accountDetails.put("password", "Test@123");
        accountDetails.put("title", "Mr");
        accountDetails.put("birth_date", "10");
        accountDetails.put("birth_month", "10");
        accountDetails.put("birth_year", "1990");
        accountDetails.put("firstname", "Sanjay");
        accountDetails.put("lastname", "Kumar");
        accountDetails.put("company", "ABC Technologies");
        accountDetails.put("address1", "MG Road");
        accountDetails.put("address2", "Near Main Road");
        accountDetails.put("country", "India");
        accountDetails.put("zipcode", "560001");
        accountDetails.put("state", "Karnataka");
        accountDetails.put("city", "Bangalore");
        accountDetails.put("mobile_number", "9876543210");
        
        String email = accountDetails.get("email");
     //   String password = accountDetails.get("password");
        System.out.println("Created account email: " + email);
        //System.out.println("Created account password: " + password.length());

        return accountDetails;
    }
}