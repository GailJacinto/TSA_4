package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
public class Loan {
    private int loanId;
    private int bookId;
    private int memberId;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private String status;

    public Loan(int loanId, int bookId, int memberId, String borrowDate, String dueDate, String returnDate, String status) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getLoanId() { return loanId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
}