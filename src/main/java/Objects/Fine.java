package Objects;

import org.bson.types.ObjectId;

import java.util.Date;

public class Fine {
    private ObjectId Id;
    private String User_Name;
    private String User_Email;
    private String Book_Title;
    private String Book_Author;
    private double Fine_Amount;
    private Date Fine_Date;
    private boolean Paid;
    private boolean Loan_Returned;

    public Fine(ObjectId id, String userName, String userEmail, String bookTitle, String bookAuthor, double fineAmount, Date fineDate, boolean paid, boolean returned) {
        Id = id;
        User_Name = userName;
        User_Email = userEmail;
        Book_Title = bookTitle;
        Book_Author = bookAuthor;
        Fine_Amount = fineAmount;
        Fine_Date = fineDate;
        Paid = paid;
        Loan_Returned = returned;
    }

    // constructors
    // standard getters and setters.
}
