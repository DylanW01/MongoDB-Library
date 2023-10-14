package EJB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

@Stateless(name = "UserEJB")
public class UserBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createUser(Document user) {
        // Creating a Mongo client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("library");
        // Retrieving a collection
        MongoCollection collection = database.getCollection("users");
        // Insert Customer
        collection.insertOne(user);
    }
    public FindIterable<Document> getUsers() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get users
        MongoCollection<Document> users = db.getCollection("users");
        FindIterable<Document> foundUsers = users.find();
        return foundUsers;
    }
}
