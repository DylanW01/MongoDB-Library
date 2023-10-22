import EJB.LoanBean;
import EJB.BookBean;
import Objects.Loan;
import com.google.gson.Gson;
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
        FindIterable<Document> foundLoans = loanBean.getActiveLoans();
        MongoCursor<Document> cursor = foundLoans.iterator();

        List<Loan> loans = new ArrayList<Loan>();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                Loan loan = new Loan(
                        doc.get("_id").toString(),
                        doc.get("Title").toString(),
                        doc.get("Author").toString(),
                        doc.get("CustomerName").toString(),
                        (Boolean) doc.get("OnLoan"));

                loans.add(loan);
            }
        } finally {
            cursor.close();
        }

        String loansJsonString = new Gson().toJson(loans);
        out.print(loansJsonString);
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
