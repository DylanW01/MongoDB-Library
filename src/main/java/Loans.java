import EJB.LoanBean;
import EJB.BookBean;
import Objects.Loan;
import com.google.gson.Gson;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "Loans", value = "/loans")
public class Loans extends HttpServlet {
    @EJB
    LoanBean loanBean;
    @EJB
    BookBean bookBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        AggregateIterable<Document> result = loanBean.getLoans();

        // Iterate through the aggregateIterable and store the documents in a list
        List<Document> documents = new ArrayList<>();
        result.into(documents);

        // Convert the list of documents to a JSON array
        //String jsonArray = documents.toString();

        Gson gson = new Gson();
        String jsonArray = gson.toJson(documents);

        out.print(jsonArray);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("Loan Created. <a href=\"http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/newloan.jsp\">Click Here</a> to go back");
        ObjectId userId = new ObjectId(request.getParameter("users"));
        ObjectId bookId = new ObjectId(request.getParameter("books"));

        Document loan = new Document()
                .append("user_id", userId)
                .append("book_id", bookId)
                .append("returned", false);

        loanBean.createLoan(loan);
        bookBean.markAsBorrowed(request.getParameter("books"));
    }
}
