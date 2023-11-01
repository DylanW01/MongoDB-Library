package EJB;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Stateless(name = "FineEJB")
public class FineBean {
    @EJB
    MongoClientProviderBean mongoClientProviderBean;

    public void createFine(Document fine) {
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

    public void checkIssueFine(ObjectId loanId) {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");
        MongoCollection<Document> fines = db.getCollection("fines");

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("_id", loanId)), // Filter loans where OnLoan is true
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.computed("return_by", "$return_by"),
                                Projections.computed("return_date", "$return_date"),
                                Projections.computed("bookData.id", new Document("$toString", "$bookData._id")),
                                Projections.computed("userData.id", new Document("$toString", "$userData._id"))
                                // Add more projections as needed
                        )
                )
        );
        // Execute the aggregation and return the result as an AggregateIterable<Document>
        AggregateIterable<Document> result = loans.aggregate(pipeline);
        Document loan = result.first();

        if (loan != null) {
            Date returnBy = loan.getDate("return_by");
            Date returnDate = loan.getDate("return_date");
            String userId = loan.getString("userData.id");
            String bookId = loan.getString("bookData.id");

            // Check due date
            if (returnBy != null && returnBy.after(returnDate)) {
                // Issue fine

                // Calculate the time difference in milliseconds
                long timeDifference = returnBy.getTime() - returnDate.getTime();
                // Calculate the number of days
                int daysDifference = (int) (timeDifference / (1000 * 60 * 60 * 24));
                // Calculate the fine cost
                double fineCost = 2.5 * daysDifference;

                Document fine = new Document()
                        .append("user_id", userId)
                        .append("book_id", bookId)
                        .append("fine_amount", fineCost)
                        .append("fine_date", new Date())
                        .append("paid", false)
                        .append("loan_id", loanId);

                fines.insertOne(fine);
            }
        } else {
            System.out.println("No matching document found.");
        }
    }
}
