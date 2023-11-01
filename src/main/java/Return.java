import EJB.BookBean;
import EJB.LoanBean;
import EJB.FineBean;
import com.google.gson.Gson;
import com.mongodb.client.AggregateIterable;
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
import java.util.List;

@WebServlet(name = "Return", value = "/return")
public class Return extends HttpServlet {
    @EJB
    LoanBean loanBean;
    @EJB
    BookBean bookBean;
    @EJB
    FineBean fineBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        AggregateIterable<Document> result = loanBean.getActiveLoans();

        // Iterate through the aggregateIterable and store the documents in a list
        List<Document> documents = new ArrayList<>();
        result.into(documents);

        // Convert the list of documents to a JSON array
        Gson gson = new Gson();
        String jsonArray = gson.toJson(documents);

        out.print(jsonArray);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("Book Returned. <a href=\"http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/newloan.jsp\">Click Here</a> to go back");
        ObjectId loanId = new ObjectId(request.getParameter("loanId"));
        String bookId = request.getParameter("bookId");

        // Mark loan as returned
        loanBean.returnBook(loanId);

        // Get loan details, check due date & issue fine if needed
        fineBean.checkIssueFine(loanId);

        // Mark book as returned
        bookBean.markAsReturned(bookId);
    }
}
