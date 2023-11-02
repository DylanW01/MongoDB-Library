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

import java.time.Instant;
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
    public AggregateIterable<Document> getFines() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> fines = db.getCollection("fines");
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("loans", "loan_id", "_id", "loanData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$loanData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("paid"),
                                Projections.include("fine_amount"),
                                Projections.include("fine_date"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author"),
                                Projections.computed("userData.email", "$userData.email"),
                                Projections.computed("userData.name", "$userData.name"),
                                Projections.computed("loanData.returned", "$loanData.returned"),
                                Projections.computed("loanData.return_date", "$loanData.return_date"),
                                Projections.computed("loanData.return_by", "$loanData.return_by")
                                // Add more projections as needed
                        )
                )
        );
        // Execute the aggregation and return the result as an AggregateIterable<Document>
        return fines.aggregate(pipeline);
    }

    public AggregateIterable<Document> getUnpaidFines() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> fines = db.getCollection("fines");
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("paid", false)), // Filter loans by customer ID
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("loans", "loan_id", "_id", "loanData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$loanData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("paid"),
                                Projections.include("fine_amount"),
                                Projections.include("fine_date"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author"),
                                Projections.computed("userData.email", "$userData.email"),
                                Projections.computed("userData.name", "$userData.name"),
                                Projections.computed("loanData.returned", "$loanData.returned"),
                                Projections.computed("loanData.return_date", "$loanData.return_date"),
                                Projections.computed("loanData.return_by", "$loanData.return_by")
                                // Add more projections as needed
                        )
                )
        );
        // Execute the aggregation and return the result as an AggregateIterable<Document>
        return fines.aggregate(pipeline);
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
            String userId = loan.get("userData", Document.class).getString("id");
            String bookId = loan.get("bookData", Document.class).getString("id");

            Instant returnByInstant = returnBy.toInstant();
            Instant returnDateInstant = returnDate.toInstant();
            // Check due date
            if (returnDateInstant.isAfter(returnByInstant)) {
                // Issue fine

                // Calculate the time difference in milliseconds
                long timeDifference = returnDate.getTime() - returnBy.getTime();
                // Calculate the number of days
                int daysDifference = (int) (timeDifference / (1000 * 60 * 60 * 24));
                // Calculate the fine cost
                double fineCost = 2.5 * daysDifference;

                Document fine = new Document()
                        .append("user_id", new ObjectId(userId))
                        .append("book_id", new ObjectId(bookId))
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

    public void markAsPaid(String fineId) {
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        MongoDatabase db = mongo.getDatabase("library");
        MongoCollection<Document> fines = db.getCollection("fines");

        Document filter = new Document("_id", new ObjectId(fineId));
        Document update = new Document("$set", new Document("paid", true));

        fines.updateOne(filter, update);
    }

    public AggregateIterable<Document> geFineReportForCustomer(ObjectId customerId, Date startDate, Date endDate) {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> fines = db.getCollection("fines");
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");

        Bson matchStage = Aggregates.match(
                Filters.and(
                        Filters.eq("user_id", customerId), // Filter loans by customer ID
                        Filters.gte("fine_date", startDate), // Filter fine_date >= startDate
                        Filters.lte("fine_date", endDate)  // Filter fine_date <= endDate
                )
        );

        List<Bson> pipeline = Arrays.asList(
                matchStage,
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("paid"), // Include loan-specific fields
                                Projections.include("fine_amount"),
                                Projections.include("fine_date"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author")
                        )
                )
        );
        // Execute the aggregation and return the result as an AggregateIterable<Document>
        return fines.aggregate(pipeline);
    }
}
