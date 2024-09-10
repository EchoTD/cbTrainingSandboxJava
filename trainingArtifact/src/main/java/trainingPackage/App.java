// https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html

package trainingPackage;

import com.couchbase.client.core.deps.com.google.api.SystemParameter;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.json.*;
import com.couchbase.client.java.query.*;

import java.time.Duration;


public class App {

    static String connectionString = "couchbase://127.0.0.1";
    static String username = "acg";
    static String password = System.getenv("CB_PSWD"); // remember to export env
    static String bucketName = "travel-sample";

    public static void main(String[] args) {
        
        Cluster cluster = Cluster.connect(connectionString, username, password);
        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        
        Scope scope = bucket.scope("tenant_agent_00");
        Collection collection = scope.collection("users");

        // Upsert(update if exists else create) document 
        MutationResult upsertResult = collection.upsert(
            "my-document",
            JsonObject.create().put("name", "alaaddin")
        );

        // Get document
        GetResult getResult = collection.get("my-document");
        String name = getResult.contentAsObject().getString("name");
        System.out.println(name);

        // Query
        Scope inventoryScope = bucket.scope("inventory");
        QueryResult result = inventoryScope.query("SELECT * FROM airline WHERE id = 10;");
        /* System.out.println(result.rowsAsObject()); */
        QueryResult result2 = inventoryScope.query("SELECT id, name FROM airline WHERE country = 'United States' ORDER BY id");
        /* System.out.println("Found Row: " + result2.rowsAsObject()); */
        QueryResult result3 = cluster.query("SELECT name FROM `travel-sample`.`inventory`.`airline` WHERE country = 'France' ORDER BY id");
        /* System.out.println("Found Row: " + result3.rowsAsObject()); */
        /* String query = "SELECT name FROM `travel-sample`.`inventory`.`airline` WHERE country = $country";
        QueryOptions options = QueryOptions
                                .queryOptions()
                                .parameters(JsonObject
                                    .create()
                                    .put("Country", "United States")
                                );
        QueryResult result4 = scope.query(query, options);
        System.out.println(result4.rowsAsObject()); */

        


        cluster.disconnect();
    }
}
