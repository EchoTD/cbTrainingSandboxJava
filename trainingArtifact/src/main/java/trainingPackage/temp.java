/* // 1. Setup Couchbase connection with Java SDK

// Maven Dependency

<dependency>
  <groupId>com.couchbase.client</groupId>
  <artifactId>java-client</artifactId>
  <version>3.3.0</version>
</dependency>


// Connect to the Couchbase cluster and bucket
Cluster cluster = Cluster.connect("couchbase://localhost", "username", "password");
Bucket bucket = cluster.bucket("bucketName");
Collection collection = bucket.defaultCollection(); // Use default collection

// 2. Basic Operations

// Insert a Document
JsonObject jsonObject = JsonObject.create()
    .put("name", "John Doe")
    .put("age", 29)
    .put("type", "customer");
MutationResult result = collection.insert("document-id", jsonObject);

// Get a Document
GetResult getResult = collection.get("document-id");
JsonObject retrievedObject = getResult.contentAsObject();
System.out.println("Name: " + retrievedObject.getString("name"));

// Upsert a Document (Insert or Update)
JsonObject updatedObject = JsonObject.create()
    .put("name", "John Doe")
    .put("age", 30); // Update age
MutationResult upsertResult = collection.upsert("document-id", updatedObject);

// Remove a Document
MutationResult removeResult = collection.remove("document-id");

// 3. Working with Queries

// N1QL Query Example
String query = "SELECT name, age FROM `bucketName` WHERE type = 'customer'";
QueryResult queryResult = cluster.query(query);
for (JsonObject row : queryResult.rowsAsObject()) {
    System.out.println("Name: " + row.getString("name"));
    System.out.println("Age: " + row.getInt("age"));
}

// Parameterized N1QL Query
String paramQuery = "SELECT * FROM `bucketName` WHERE name = $name";
QueryResult paramResult = cluster.query(paramQuery, 
    queryOptions().parameters(JsonObject.create().put("name", "John Doe")));
for (JsonObject row : paramResult.rowsAsObject()) {
    System.out.println(row);
}

// 4. Key Features

// Durability Options
MutationResult durabilityResult = collection.upsert("document-id", jsonObject, 
    upsertOptions().durability(DurabilityLevel.MAJORITY));

// Expiry (TTL) for Document
MutationResult ttlResult = collection.upsert("document-id", jsonObject, 
    upsertOptions().expiry(Duration.ofHours(2)));

// Batch Operations
List<GetResult> batchResults = collection.getAll(Arrays.asList("doc1", "doc2", "doc3"));
for (GetResult batchResult : batchResults) {
    System.out.println(batchResult.contentAsObject());
}

// 5. Error Handling

// Handling Exceptions
try {
    GetResult nonExistentResult = collection.get("non-existent-document-id");
} catch (DocumentNotFoundException e) {
    System.out.println("Document not found.");
}

// Handling Timeout
try {
    MutationResult timeoutResult = collection.upsert("document-id", jsonObject, 
        upsertOptions().timeout(Duration.ofSeconds(2)));
} catch (TimeoutException e) {
    System.out.println("Operation timed out.");
}

// 6. Advanced Operations

// Subdocument Operations (Mutating part of a document)
MutationResult subdocResult = collection.mutateIn("document-id", 
    Arrays.asList(MutateInSpec.upsert("address.street", "123 Main St")));

// Full Text Search (FTS)
SearchResult ftsResult = cluster.searchQuery("indexName", SearchQuery.term("searchTerm"));
for (SearchRow row : ftsResult.rows()) {
    System.out.println("Document ID: " + row.id());
}

// 7. Indexes

// Create a Primary Index
cluster.query("CREATE PRIMARY INDEX ON `bucketName`");

// Create a Secondary Index
cluster.query("CREATE INDEX `idx_type` ON `bucketName`(`type`)");
 */