import Objects.Book;
import EJB.BookBean;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "bookListing", value = "/books/available")
public class bookListing extends HttpServlet {

    @EJB
    BookBean bookBean;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        FindIterable<Document> foundBooks = bookBean.getAvailableBooks();
        MongoCursor<Document> cursor = foundBooks.iterator();

        List<Book> books = new ArrayList<Book>();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                Book book = new Book(
                        doc.get("_id").toString(),
                        doc.get("Title").toString(),
                        doc.get("Author").toString(),
                        doc.get("ISBN").toString(),
                        (Integer) doc.get("Pages"),
                        (Date) doc.get("Added"),
                        (Boolean) doc.get("OnLoan"));

                books.add(book);
            }
        } finally {
            cursor.close();
        }

        String booksJsonString = new Gson().toJson(books);
        out.print(booksJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}