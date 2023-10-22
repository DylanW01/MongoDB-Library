package EJB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;
import org.bson.types.ObjectId;

@Stateless(name = "BookEJB")
public class BookBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createBook(Document book) {
        // Creating a Mongo client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Accessing the database
        MongoDatabase db = mongo.getDatabase("library");
        // Retrieving a collection
        MongoCollection books = db.getCollection("books");
        // Insert Customer
        books.insertOne(book);
    }

    public FindIterable<Document> getBooks() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get customers
        MongoCollection<Document> books = db.getCollection("books");
        FindIterable<Document> foundBooks = books.find();
        return foundBooks;
    }

    public FindIterable<Document> getAvailableBooks() {
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        MongoDatabase db = mongo.getDatabase("library");
        MongoCollection<Document> books = db.getCollection("books");

        FindIterable<Document> foundBooks = books.find(eq("OnLoan", false));

        return foundBooks;
    }

    public void markAsBorrowed(String bookIdString) {
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        MongoDatabase db = mongo.getDatabase("library");
        MongoCollection<Document> books = db.getCollection("books");

        Document filter = new Document("_id", new ObjectId(bookIdString));
        Document update = new Document("$set", new Document("OnLoan", true));

        books.updateOne(filter, update);
    }

    public void markAsReturned(String bookIdString) {
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        MongoDatabase db = mongo.getDatabase("library");
        MongoCollection<Document> books = db.getCollection("books");

        Document filter = new Document("_id", new ObjectId(bookIdString));
        Document update = new Document("$set", new Document("OnLoan", false));

        books.updateOne(filter, update);
    }
}
