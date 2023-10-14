
import jakarta.ejb.EJB;
import EJB.UserBean;
import org.bson.Document;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "Registration", value = "/registration")
public class Registration extends HttpServlet {
    @EJB
    UserBean registrationBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Working");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("Processing user registration...");

        /*Document customer = new Document()
                .append("First_Name", request.getParameter("firstname"))
                .append("Last_Name", request.getParameter("lastname"))
                .append("Email", request.getParameter("email"))
                .append("Password", request.getParameter("password"));

        registrationBean.createCustomer(customer);
        out.println("Registration completed, data stored...." + "<br><br>");

        FindIterable<Document> colHistory = registrationBean.getCustomerList();

        MongoCursor<Document> cursor = colHistory.iterator();
        out.println("List of Customers are below..." + "<br><br>");

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                out.println("First Name: " + doc.get("First_Name") + "<br>");
                out.println("Last Name: " + doc.get("Last_Name") + "<br>");
                out.println("Email: " + doc.get("Email") + "<br>");
                out.println("Password: " + doc.get("Password") + "<br>");
                out.println("-----------------------" + "<br>");
            }
        } finally {
            cursor.close();
        }*/
    }

    public void createCustomer(Document customer) {


    }
}
