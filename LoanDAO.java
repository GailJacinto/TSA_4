package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public boolean issueLoan(int bookId, int memberId, String dueDate) throws SQLException {
        String sql = "INSERT INTO loans (book_id, member_id, due_date, status) VALUES (?, ?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setString(3, dueDate);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean returnLoan(int loanId) throws SQLException {
        String sql = "UPDATE loans SET status = 'RETURNED', return_date = CURRENT_TIMESTAMP WHERE loan_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Loan getActiveLoanById(int loanId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE loan_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                        rs.getInt("loan_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public boolean hasActiveLoan(int memberId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND book_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("book_id"),
                    rs.getInt("member_id"),
                    rs.getString("borrow_date"),
                    rs.getString("due_date"),
                    rs.getString("return_date"),
                    rs.getString("status")
                ));
            }
        }
        return loans;
    }
}