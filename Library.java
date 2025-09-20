import java.util.ArrayList;
import java.util.Scanner;

public class Library {
    private ArrayList<Book> books;
    private ArrayList<Patron> patrons;
    private double totalFeesCollected;

    public Library() {
        books = new ArrayList<>();
        patrons = new ArrayList<>();
        totalFeesCollected = 0.0;
    }

    // Book management methods
    public void addBook(String title, String author, String isbn) {
        // Check if ISBN already exists
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                System.out.println("Error: Book with ISBN " + isbn + " already exists!");
                return;
            }
        }

        Book newBook = new Book(title, author, isbn);
        books.add(newBook);
        System.out.println(" Book added successfully: " + title);
    }

    public void removeBook(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book != null) {
            if (!book.isAvailable()) {
                System.out.println("Cannot remove book. It is currently borrowed.");
                return;
            }
            books.remove(book);
            System.out.println("✓ Book removed successfully.");
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
    }

    // Patron management methods
    public void registerPatron(String name, String id) {
        // Check if patron ID already exists
        for (Patron patron : patrons) {
            if (patron.getId().equals(id)) {
                System.out.println("Error: Patron with ID " + id + " already exists!");
                return;
            }
        }

        Patron newPatron = new Patron(name, id);
        patrons.add(newPatron);
        System.out.println(" Patron registered successfully: " + name);
    }

    // Book lending methods
    public void issueBook(String isbn, String patronId) {
        Book book = findBookByIsbn(isbn);
        Patron patron = findPatronById(patronId);

        if (book == null) {
            System.out.println("Error: Book not found!");
            return;
        }

        if (patron == null) {
            System.out.println("Error: Patron not found!");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Error: Book is already borrowed!");
            return;
        }

        if (patron.getBorrowedBooksCount() >= 5) {
            System.out.println("Error: Patron has reached borrowing limit (5 books)!");
            return;
        }

        if (patron.getOutstandingFees() > 0) {
            System.out.println("Error: Patron has outstanding fees of Rs " + patron.getOutstandingFees());
            return;
        }

        // Issue the book
        book.setAvailable(false);
        patron.borrowBook(book);
        System.out.println("✓ Book '" + book.getTitle() + "' issued to " + patron.getName());
    }

    public void returnBook(String isbn, String patronId) {
        Book book = findBookByIsbn(isbn);
        Patron patron = findPatronById(patronId);

        if (book == null || patron == null) {
            System.out.println("Error: Book or patron not found!");
            return;
        }

        if (!patron.getBorrowedBooks().contains(book)) {
            System.out.println("Error: This patron didn't borrow this book!");
            return;
        }

        // Return the book
        book.setAvailable(true);
        patron.returnBook(book);

        // Calculate late fee (simplified - 10 Rs per day late)
        double lateFee = calculateLateFee();
        if (lateFee > 0) {
            patron.addFee(lateFee);
            System.out.println("Late fee applied: Rs " + lateFee);
        }

        System.out.println("✓ Book '" + book.getTitle() + "' returned successfully");
    }

    // Fee management
    public void payFees(String patronId, double amount) {
        Patron patron = findPatronById(patronId);
        if (patron != null) {
            if (amount <= 0) {
                System.out.println("Error: Amount must be positive!");
                return;
            }

            double amountToPay = Math.min(amount, patron.getOutstandingFees());
            patron.payFee(amountToPay);
            totalFeesCollected += amountToPay;

            System.out.println(" Paid Rs " + amountToPay + ". Remaining fees: Rs " + patron.getOutstandingFees());
        } else {
            System.out.println("Error: Patron not found!");
        }
    }

    // Report generation methods
    public void generateAvailableBooksReport() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        boolean found = false;
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println("• " + book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books available");
        }
    }

    public void generateBorrowedBooksReport() {
        System.out.println("\n=== BORROWED BOOKS ===");
        boolean found = false;
        for (Book book : books) {
            if (!book.isAvailable()) {
                // Find which patron has this book
                Patron borrower = findBorrower(book);
                System.out.println("• " + book.getTitle() + "  Borrowed by: " +
                        (borrower != null ? borrower.getName() : "Unknown"));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books currently borrowed");
        }
    }

    public void generatePatronsReport() {
        System.out.println("\n=== PATRONS REPORT ===");
        if (patrons.isEmpty()) {
            System.out.println("No patrons registered");
        } else {
            for (Patron patron : patrons) {
                System.out.println("• " + patron);
            }
        }
    }

    public void generateLibraryStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + books.size());
        System.out.println("Total Patrons: " + patrons.size());
        System.out.println("Available Books: " + countAvailableBooks());
        System.out.println("Borrowed Books: " + (books.size() - countAvailableBooks()));
        System.out.println("Total Fees Collected: Rs " + totalFeesCollected);
        System.out.println("Current Outstanding Fees: Rs " + calculateTotalOutstandingFees());
    }

    // Helper methods
    private Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    private Patron findPatronById(String id) {
        for (Patron patron : patrons) {
            if (patron.getId().equals(id)) {
                return patron;
            }
        }
        return null;
    }

    private Patron findBorrower(Book book) {
        for (Patron patron : patrons) {
            if (patron.getBorrowedBooks().contains(book)) {
                return patron;
            }
        }
        return null;
    }

    private int countAvailableBooks() {
        int count = 0;
        for (Book book : books) {
            if (book.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    private double calculateTotalOutstandingFees() {
        double total = 0;
        for (Patron patron : patrons) {
            total += patron.getOutstandingFees();
        }
        return total;
    }

    private double calculateLateFee() {
        // Simplified late fee calculation
        // In real system, you'd calculate based on due date and current date
        return Math.random() > 0.7 ? 10.0 : 0.0; // 30% chance of late fee
    }

    // Main method with menu system
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Add sample data
        library.addBook("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565");
        library.addBook("To Kill a Mockingbird", "Harper Lee", "978-0061120084");
        library.addBook("1984", "George Orwell", "978-0451524935");
        library.addBook("Pride and Prejudice", "Jane Austen", "978-0141439518");

        library.registerPatron("Ali Khan", "P001");
        library.registerPatron("Sara Ahmed", "P002");
        library.registerPatron("Usman Malik", "P003");

        System.out.println(" Welcome to Library Management System!");

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Register Patron");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Pay Fees");
            System.out.println("7. Generate Reports");
            System.out.println("8. Library Statistics");
            System.out.println("9. Exit");
            System.out.print("Choose option (1-9): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1:
                        System.out.print("Enter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine();
                        library.addBook(title, author, isbn);
                        break;

                    case 2:
                        System.out.print("Enter ISBN of book to remove: ");
                        String removeIsbn = scanner.nextLine();
                        library.removeBook(removeIsbn);
                        break;

                    case 3:
                        System.out.print("Enter patron name: ");
                        String patronName = scanner.nextLine();
                        System.out.print("Enter patron ID: ");
                        String patronId = scanner.nextLine();
                        library.registerPatron(patronName, patronId);
                        break;

                    case 4:
                        System.out.print("Enter ISBN of book to issue: ");
                        String issueIsbn = scanner.nextLine();
                        System.out.print("Enter patron ID: ");
                        String issuePatronId = scanner.nextLine();
                        library.issueBook(issueIsbn, issuePatronId);
                        break;

                    case 5:
                        System.out.print("Enter ISBN of book to return: ");
                        String returnIsbn = scanner.nextLine();
                        System.out.print("Enter patron ID: ");
                        String returnPatronId = scanner.nextLine();
                        library.returnBook(returnIsbn, returnPatronId);
                        break;

                    case 6:
                        System.out.print("Enter patron ID: ");
                        String feePatronId = scanner.nextLine();
                        System.out.print("Enter amount to pay: Rs ");
                        double amount = scanner.nextDouble();
                        library.payFees(feePatronId, amount);
                        break;

                    case 7:
                        System.out.println("\n--- REPORTS ---");
                        System.out.println("1. Available Books");
                        System.out.println("2. Borrowed Books");
                        System.out.println("3. Patrons Report");
                        System.out.print("Choose report: ");
                        int reportChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (reportChoice) {
                            case 1: library.generateAvailableBooksReport(); break;
                            case 2: library.generateBorrowedBooksReport(); break;
                            case 3: library.generatePatronsReport(); break;
                            default: System.out.println("Invalid choice");
                        }
                        break;

                    case 8:
                        library.generateLibraryStatistics();
                        break;

                    case 9:
                        System.out.println("Thank you for using Library Management System!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Please choose 1-9.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
