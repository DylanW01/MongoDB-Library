import com.mongodb.client.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

@Stateless(name = "RegistrationEJB")
public class RegistrationBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createCustomer(Document customer) {
        // Creating a Mongo client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("dbSales");
        // Retrieving a collection
        MongoCollection collection = database.getCollection("Collection_Customer");
        // Insert Customer
        collection.insertOne(customer);
    }

    public FindIterable<Document> getCustomerList() {
        // Client
        //MongoClient mongo = (MongoClient) new com.mongodb.MongoClient("localhost");
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("dbSales");
        // Get customers
        MongoCollection<Document> collection = db.getCollection("Collection_Customer");
        FindIterable<Document> colHistory = collection.find();
        return colHistory;
    }
}
