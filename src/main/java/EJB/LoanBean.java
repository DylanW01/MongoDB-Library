package EJB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

@Stateless(name = "LoanEJB")
public class LoanBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createLoan(Document loan) {
        // Creating a Mongo client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("library");
        // Retrieving a collection
        MongoCollection collection = database.getCollection("loans");
        // Insert Customer
        collection.insertOne(loan);
    }
    public FindIterable<Document> getLoans() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get users
        MongoCollection<Document> loans = db.getCollection("loans");
        FindIterable<Document> foundLoans = loans.find();
        return foundLoans;
    }

    public FindIterable<Document> getActiveLoans() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get users
        MongoCollection<Document> loans = db.getCollection("loans");
        FindIterable<Document> foundLoans = loans.find(eq("returned", false));
        return foundLoans;
    }
}
