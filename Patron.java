import java.util.ArrayList;

public class Patron {
    private String name;
    private String id;
    private ArrayList<Book> borrowedBooks;
    private double outstandingFees;

    public Patron(String name, String id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
        this.outstandingFees = 0.0;
    }

    // Getters
    public String getName() { return name; }
    public String getId() { return id; }
    public ArrayList<Book> getBorrowedBooks() { return borrowedBooks; }
    public double getOutstandingFees() { return outstandingFees; }

    // Methods
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public void addFee(double amount) {
        outstandingFees += amount;
    }

    public void payFee(double amount) {
        if (amount > outstandingFees) {
            amount = outstandingFees;
        }
        outstandingFees -= amount;
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size();
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ") - Borrowed: " + borrowedBooks.size() +
                " books, Fees: Rs " + outstandingFees;
    }
}
