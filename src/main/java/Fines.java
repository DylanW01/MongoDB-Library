import EJB.FineBean;
import Objects.Fine;
import Objects.User;
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
import java.util.List;


@WebServlet(name = "Fines", value = "/fines")
public class Fines extends HttpServlet {
    @EJB
    FineBean fineBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        AggregateIterable<Document> result = fineBean.getFines();

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

    }
}
