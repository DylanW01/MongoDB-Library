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
        String date = request.getParameter("date");

        Date[] convertedDate = getFirstAndLastDayOfMonth(date);

        PrintWriter out = response.getWriter();
        AggregateIterable<Document> result = fineBean.geFineReportForCustomer(customerId, convertedDate[0], convertedDate[1]);

        // Iterate through the aggregateIterable and store the documents in a list
        List<Document> documents = new ArrayList<>();
        result.into(documents);

        // Convert the list of documents to a JSON array
        Gson gson = new Gson();
        String jsonArray = gson.toJson(documents);

        out.print(jsonArray);
        out.flush();
    }

    public static Date[] getFirstAndLastDayOfMonth(String yearMonth) {
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0); // Set time to midnight
        Date firstDay = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1); // Move to the last day of the month
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date lastDay = calendar.getTime();

        return new Date[] { firstDay, lastDay };
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
