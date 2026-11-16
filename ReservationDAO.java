package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public boolean addReservation(int bookId, int memberId) throws SQLException {
        String sql = "INSERT INTO reservations (book_id, member_id, status) VALUES (?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean cancelReservation(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE reservation_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Reservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("book_id"),
                    rs.getInt("member_id"),
                    rs.getString("reservation_date"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }
}