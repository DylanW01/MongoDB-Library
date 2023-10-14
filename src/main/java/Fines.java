import EJB.FineBean;
import Objects.Fine;
import Objects.User;
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
import java.util.List;


@WebServlet(name = "Books", value = "/users")
public class Fines extends HttpServlet {
    @EJB
    FineBean fineBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        FindIterable<Document> foundFines = fineBean.getFines();
        MongoCursor<Document> cursor = foundFines.iterator();
/*
        List<Fine> fines = new ArrayList<Fine>();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                Fine fine = new Fine(
                        (ObjectId) doc.get("_id"),
                        doc.get("name").toString(),
                        doc.get("email").toString());
                fines.add(fine);
            }
        } finally {
            cursor.close();
        }

        String usersJsonString = new Gson().toJson(fines);
        out.print(usersJsonString);
        out.flush();*/
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
