package EJB;

import com.mongodb.*;
import com.mongodb.client.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Aggregates;
import java.util.Date;
import com.mongodb.client.model.Projections;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

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
    public AggregateIterable<Document> getLoans() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("returned"), // Include loan-specific fields
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author"),
                                Projections.computed("bookData.ISBN", "$bookData.ISBN"),
                                Projections.computed("bookData.Pages", "$bookData.Pages"),
                                Projections.computed("bookData.Added", "$bookData.Added"),
                                Projections.computed("userData.email", "$userData.email"),
                                Projections.computed("userData.name", "$userData.name"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.id", new Document("$toString", "$bookData._id")) // Convert _id to a string
                                // Add more projections as needed
                        )
                )
        );

        // Execute the aggregation and return the result as an AggregateIterable<Document>
        return loans.aggregate(pipeline);
    }

    public AggregateIterable<Document> getActiveLoans() {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("returned", false)), // Filter loans where OnLoan is true
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("returned"), // Include loan-specific fields
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author"),
                                Projections.computed("bookData.ISBN", "$bookData.ISBN"),
                                Projections.computed("bookData.Pages", "$bookData.Pages"),
                                Projections.computed("bookData.Added", "$bookData.Added"),
                                Projections.computed("userData.email", "$userData.email"),
                                Projections.computed("userData.name", "$userData.name"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.id", new Document("$toString", "$bookData._id")) // Convert _id to a string
                                // Add more projections as needed
                        )
                )
        );

        // Execute the aggregation and return the result as an AggregateIterable<Document>
        return loans.aggregate(pipeline);
    }

    public AggregateIterable<Document> geLoanReportForCustomer(ObjectId customerId, Date startDate, Date endDate) {
        // Client
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        // Get DB
        MongoDatabase db = mongo.getDatabase("library");
        // Get loans
        MongoCollection<Document> loans = db.getCollection("loans");
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> users = db.getCollection("users");


        Bson matchStage = Aggregates.match(
                Filters.and(
                        Filters.eq("user_id", customerId), // Filter loans by customer ID
                        Filters.gte("return_by", startDate), // Filter return_by >= startDate
                        Filters.lte("return_by", endDate)  // Filter return_by <= endDate
                )
        );

        // Create an aggregation pipeline to join the "loans", "users" and "books" collections
        List<Bson> pipeline = Arrays.asList(
                matchStage,
                Aggregates.lookup("books", "book_id", "_id", "bookData"),
                Aggregates.lookup("users", "user_id", "_id", "userData"),

                Aggregates.unwind("$bookData"),
                Aggregates.unwind("$userData"),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("returned"), // Include loan-specific fields
                                Projections.include("return_by"),
                                Projections.include("return_date"),
                                Projections.computed("id", new Document("$toString", "$_id")), // Convert _id to a string
                                Projections.computed("bookData.Title", "$bookData.Title"), // Include book title
                                Projections.computed("bookData.Author", "$bookData.Author"),
                                Projections.computed("bookData.ISBN", "$bookData.ISBN"),
                                Projections.computed("bookData.Pages", "$bookData.Pages"),
                                Projections.computed("bookData.Added", "$bookData.Added"),
                                Projections.computed("userData.email", "$userData.email"),
                                Projections.computed("userData.name", "$userData.name")
                                // Add more projections as needed
                        )
                )
        );
            // Execute the aggregation and return the result as an AggregateIterable<Document>
        return loans.aggregate(pipeline);
    }

    public void returnBook(ObjectId loanId) {
        MongoClient mongo = mongoClientProviderBean.getMongoClient();
        MongoDatabase db = mongo.getDatabase("library");
        MongoCollection<Document> books = db.getCollection("loans");
        MongoCollection<Document> fines = db.getCollection("fines");

        Document filter = new Document("_id", loanId);
        Date currentDate = new Date();
        Document update = new Document("$set", new Document("returned", true)
                .append("return_date", currentDate));

        books.updateOne(filter, update);
    }
}
