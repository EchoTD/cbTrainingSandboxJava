/*
 * You are tasked with creating a basic customer management system. This system will allow you to:

    (+) Add a new customer (with fields like customerId, name, email, and address).
    (+) Update customer details (e.g., changing email or address).
    (+) Retrieve a customer's information by customerId.
    (+) Delete a customer from the database.
    (-) Run a query to find customers based on certain criteria (e.g., customers from a specific city).
 */

 /* TODO:   -Fix the updateCustomerInfo function: Create a statement for checking if the data types of field and value match
  *         -Add a temp trashbin type of array function to deleteCustomer or before deleting backup the customer data somewhere else
  */


package trainingPackage;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.*;
import com.couchbase.client.java.query.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

public class exampleCRM {
    static String connectionString = "couchbase://127.0.0.1";
    static String username = System.getenv("CB_USER");
    static String password = System.getenv("CB_PSWD"); // remember to export env
    
    static String bucketName = "CRM-training";

    public static void main(String[] args) {
        Cluster currentCluster = Cluster.connect(connectionString, username, password);

        // select a bucket, scope, collection referance
        Bucket currentBucket = currentCluster.bucket(bucketName);
        currentBucket.waitUntilReady(Duration.ofSeconds(10));
        Scope currentScope = currentBucket.scope("_default");
        Collection currentCollection = currentScope.collection("_default");

        /* ---------------------------------------------------------------------------------------- */

        addCustomer(currentCollection,  "Istanbul/Esenyurt", 21, "alaaddin.c.gursoy@gmail.com",
                                        "Alaaddin Can", "male", "Motorcycling", false, "intern",
                                        "Gürsoy", 0, "+90-505-127-1685", 0.0f);
        /* getCustomerById(currentCollection, "002215c5-be7f-4fe9-8bb2-432177fad39a");
        updateCustomerInfo(currentCollection, "002215c5-be7f-4fe9-8bb2-432177fad39a", "is_married", true);
        getCustomerById(currentCollection, "002215c5-be7f-4fe9-8bb2-432177fad39a"); */

        deleteCustomer(currentCollection, "004435b6-9685-4c01-9cde-694dbd72aff7");
        deleteCustomer(currentCollection, "004435b6-9685-4c01-9cde-694dbd72aff7");

        currentCluster.disconnect();

    }
    // functions
    public static void addCustomer(Collection collection, 
                                    String address,
                                    int age,
                                    String email,
                                    String first_name,
                                    String gender,
                                    String hobbies,
                                    boolean is_married,
                                    String job,
                                    String last_name,
                                    int orders,
                                    String phone,
                                    float spent){
        JsonObject customer = JsonObject.create()
                .put("address", address)
                .put("age", age)
                .put("email", email)
                .put("first_name", first_name)
                .put("gender", gender)
                .put("hobbies", hobbies)
                .put("is_married", is_married)
                .put("job", job)
                .put("last_name", last_name)
                .put("orders", orders)
                .put("phone", phone)
                .put("registered", Instant.now().toString())
                .put("spent", spent);

        MutationResult result = collection.insert(UUID.randomUUID().toString(), customer);
        System.out.println("Customer Added with this ID: " + result.mutationToken().get());
    }
    public static void getCustomerById(Collection collection, String customerUUId){
        try {
            GetResult result = collection.get(customerUUId);
            System.out.println("Customer Information: " + result.contentAsObject());
        } catch (DocumentNotFoundException e) {
            System.err.println("Customer not found with ID: " + customerUUId);
        }
    }
    /* Update customer info by taking in the collection, customerUUID; the fieldToChange(ex. first_name, orders, email...) and the updated newValue */
    public static void updateCustomerInfo(Collection collection, String customerUUID, String fieldToChange, String newValue){
        /* LookupInResult fieldResult = collection.lookupIn(customerUUID, List.of(LookupInSpec.get(fieldToChange)));
        
        if (fieldResult != valueResult){
            System.err.println("The type of data doesn't match: " + fieldResult + "!=" + valueResult);
        } */
        try {
            MutationResult result = collection.mutateIn(customerUUID, List.of(MutateInSpec.upsert(fieldToChange, newValue)));
        } catch (DocumentNotFoundException e) {
            System.err.println("Something went wrong?");
        }
    }
    public static void updateCustomerInfo(Collection collection, String customerUUID, String fieldToChange, int newValue){
        try {
            MutationResult result = collection.mutateIn(customerUUID, List.of(MutateInSpec.upsert(fieldToChange, newValue)));
        } catch (DocumentNotFoundException e) {
            System.err.println("Something went wrong?");
        }
    }
    public static void updateCustomerInfo(Collection collection, String customerUUID, String fieldToChange, float newValue){
        try {
            MutationResult result = collection.mutateIn(customerUUID, List.of(MutateInSpec.upsert(fieldToChange, newValue)));
        } catch (DocumentNotFoundException e) {
            System.err.println("Something went wrong?");
        }
    }
    public static void updateCustomerInfo(Collection collection, String customerUUID, String fieldToChange, boolean newValue){
        try {
            MutationResult result = collection.mutateIn(customerUUID, List.of(MutateInSpec.upsert(fieldToChange, newValue)));
        } catch (DocumentNotFoundException e) {
            System.err.println("Something went wrong?");
        }
    }
    public static void deleteCustomer(Collection collection, String customerUUID){
        try {
            collection.remove(customerUUID);
            System.out.println("Customer deleted with ID: " + customerUUID);
        } catch (DocumentNotFoundException e) {
            System.err.println("Customer with the specified ID couldn't be found.");
        }
    }
}