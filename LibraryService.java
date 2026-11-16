package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final LoanDAO loanDAO = new LoanDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public boolean registerMember(String name, String email, String phone) throws SQLException, IllegalArgumentException {
        if (name == null || name.trim().isEmpty() || email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid name or email format.");
        }
        return memberDAO.addMember(new Member(0, name, email, phone, ""));
    }

    public boolean addBook(String title, String author, String isbn, int totalCopies) throws SQLException, IllegalArgumentException {
        if (title == null || title.trim().isEmpty() || isbn == null || totalCopies < 0) {
            throw new IllegalArgumentException("Invalid book details or negative copies.");
        }
        return bookDAO.addBook(new Book(0, title, author, isbn, totalCopies, totalCopies));
    }

    public synchronized boolean issueLoan(int bookId, int memberId, String dueDate) throws SQLException, IllegalStateException {
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            throw new IllegalStateException("Book not found.");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies for loan.");
        }
        if (loanDAO.hasActiveLoan(memberId, bookId)) {
            throw new IllegalStateException("Member already has an active loan for this book.");
        }

        // Transactional decrement and issue
        boolean updated = bookDAO.updateAvailableCopies(bookId, -1);
        if (!updated) {
            throw new IllegalStateException("Failed to update book inventory.");
        }
        return loanDAO.issueLoan(bookId, memberId, dueDate);
    }

    public synchronized boolean returnLoan(int loanId) throws SQLException, IllegalStateException {
        Loan loan = loanDAO.getActiveLoanById(loanId);
        if (loan == null) {
            throw new IllegalStateException("Active loan not found.");
        }

        boolean returned = loanDAO.returnLoan(loanId);
        if (returned) {
            bookDAO.updateAvailableCopies(loan.getBookId(), 1);
        }
        return returned;
    }

    public boolean makeReservation(int bookId, int memberId) throws SQLException {
        return reservationDAO.addReservation(bookId, memberId);
    }

    public boolean cancelReservation(int reservationId) throws SQLException {
        return reservationDAO.cancelReservation(reservationId);
    }

    public List<Book> getBooks() throws SQLException { return bookDAO.getAllBooks(); }
    public List<Member> getMembers() throws SQLException { return memberDAO.getAllMembers(); }
    public List<Loan> getLoans() throws SQLException { return loanDAO.getAllLoans(); }
    public List<Reservation> getReservations() throws SQLException { return reservationDAO.getAllReservations(); }
}