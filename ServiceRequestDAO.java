package com.mycompany.jacinto_campusconnect_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {
    private static final String DB_URL = "jdbc:sqlite:campusconnect.db";

    public Connection getConnection() throws SQLException {
        File dbFile = new File("campusconnect.db");
        if (!dbFile.exists()) {
            throw new SQLException("Database connection failed: campusconnect.db not found.");
        }
        
        Connection conn = DriverManager.getConnection(DB_URL);
        createTableIfNotExists(conn);
        return conn;
    }
    
    private void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS service_request (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "student_name TEXT NOT NULL," +
                     "student_id TEXT NOT NULL," +
                     "category TEXT NOT NULL," +
                     "description TEXT NOT NULL," +
                     "priority TEXT NOT NULL," +
                     "status TEXT NOT NULL," +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(ServiceRequest req) throws SQLException {
        String sql = "INSERT INTO service_request (student_name, student_id, category, description, priority, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, req.getStudentName());
            pstmt.setString(2, req.getStudentId());
            pstmt.setString(3, req.getCategory());
            pstmt.setString(4, req.getDescription());
            pstmt.setString(5, req.getPriority());
            pstmt.setString(6, req.getStatus());
            pstmt.executeUpdate();
        }
    }

    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM service_request ORDER BY id DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public ServiceRequest findById(int id) throws SQLException {
        String sql = "SELECT * FROM service_request WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean updateStatus(int id, String newStatus) throws SQLException {
        String sql = "UPDATE service_request SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM service_request WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
            rs.getInt("id"),
            rs.getString("student_name"),
            rs.getString("student_id"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getString("priority"),
            rs.getString("status"),
            rs.getString("created_at")
        );
    }
}