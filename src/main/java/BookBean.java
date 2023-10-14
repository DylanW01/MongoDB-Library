import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;

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
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get customers
        MongoCollection<Document> books = db.getCollection("books");
        FindIterable<Document> foundBooks = books.find(eq("OnLoan", false));
        return foundBooks;
    }
}
