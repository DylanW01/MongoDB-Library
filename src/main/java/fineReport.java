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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(name = "loan-report", value = "/fine-report")
public class fineReport extends HttpServlet {
    @EJB
    FineBean fineBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        ObjectId customerId = new ObjectId(request.getParameter("id"));

        PrintWriter out = response.getWriter();
        AggregateIterable<Document> result = fineBean.geFineReportForCustomer(customerId);

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
