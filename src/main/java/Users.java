import EJB.UserBean;
import Objects.Book;
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
import java.util.Date;
import java.util.List;


@WebServlet(name = "Books", value = "/users")
public class Users extends HttpServlet {
    @EJB
    UserBean userBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        FindIterable<Document> foundUsers = userBean.getUsers();
        MongoCursor<Document> cursor = foundUsers.iterator();

        List<User> users = new ArrayList<User>();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                User user = new User(
                        doc.get("_id").toString(),
                        doc.get("name").toString(),
                        doc.get("email").toString());
                users.add(user);
            }
        } finally {
            cursor.close();
        }

        String usersJsonString = new Gson().toJson(users);
        out.print(usersJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
