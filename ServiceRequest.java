package com.mycompany.jacinto_campusconnect_tsa4;

/**
 *
 * @author Gail Jacinto
 */
public class ServiceRequest {
    private int id;
    private String studentName;
    private String studentId;
    private String category;
    private String description;
    private String priority;
    private String status;
    private String createdAt;

    public ServiceRequest() {}

    public ServiceRequest(int id, String studentName, String studentId, String category, String description, String priority, String status, String createdAt) {
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}