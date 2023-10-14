package Objects;

import org.bson.types.ObjectId;

import java.util.Date;

public class User {
    private ObjectId Id;
    private String Name;
    private String Email;

    public User(ObjectId id, String name, String email) {
        Id = id;
        Name = name;
        Email = email;
    }

    // constructors
    // standard getters and setters.
}
