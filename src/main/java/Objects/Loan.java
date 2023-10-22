package Objects;

import java.util.Date;

public class Loan {
    private String Id;
    private String BookTitle;
    private String Author;
    private String CustomerName;
    private boolean OnLoan;

    public Loan(String id, String title, String author, String customerName, boolean onLoan) {
        Id = id;
        BookTitle = title;
        Author = author;
        CustomerName = customerName;
        OnLoan = onLoan;
    }

    // constructors
    // standard getters and setters.
}
