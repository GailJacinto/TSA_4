package com.mycompany.jacinto_campusconnect_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceRequestService {
    private final ServiceRequestDAO dao;
    private final AtomicInteger processedCount = new AtomicInteger(0); // Thread-safe counter avoiding race conditions

    public ServiceRequestService() {
        this.dao = new ServiceRequestDAO();
    }

    public void addRequest(ServiceRequest req) throws SQLException, IllegalArgumentException {
        validate(req);
        if (req.getStatus() == null || req.getStatus().isEmpty()) {
            req.setStatus("Pending");
        }
        dao.insert(req);
    }

    public List<ServiceRequest> getAllRequests() throws SQLException {
        return dao.findAll();
    }

    public ServiceRequest getRequestById(int id) throws SQLException {
        return dao.findById(id);
    }

    public boolean updateStatus(int id, String status) throws SQLException {
        boolean updated = dao.updateStatus(id, status);
        if (updated) {
            processedCount.incrementAndGet();
        }
        return updated;
    }

    public boolean deleteRequest(int id) throws SQLException {
        return dao.delete(id);
    }

    public int getProcessedCount() {
        return processedCount.get();
    }

    private void validate(ServiceRequest req) {
        if (req.getStudentName() == null || req.getStudentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (req.getStudentId() == null || !req.getStudentId().matches("^\\d{4}-\\d{5}$")) {
            throw new IllegalArgumentException("Student ID must match format YYYY-NNNNN (e.g., 2024-12345).");
        }
        if (req.getDescription() == null || req.getDescription().trim().length() < 5) {
            throw new IllegalArgumentException("Description must be at least 5 characters long.");
        }
    }
}