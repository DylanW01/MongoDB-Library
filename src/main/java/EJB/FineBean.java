package EJB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

@Stateless(name = "FineEJB")
public class FineBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createFineUser(Document fine) {
        // Creating a Mongo client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("library");
        // Retrieving a collection
        MongoCollection collection = database.getCollection("fines");
        // Insert Customer
        collection.insertOne(fine);
    }
    public FindIterable<Document> getFines() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get users
        MongoCollection<Document> fines = db.getCollection("fines");
        FindIterable<Document> foundFines = fines.find();
        return foundFines;
    }

    public FindIterable<Document> getUnpaidFines() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get users
        MongoCollection<Document> fines = db.getCollection("fines");
        FindIterable<Document> foundFines = fines.find(eq("paid", false));
        return foundFines;
    }
}
