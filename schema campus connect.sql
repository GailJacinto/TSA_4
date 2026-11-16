CREATE TABLE IF NOT EXISTS service_request (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_name TEXT NOT NULL,
    student_id TEXT NOT NULL,
    category TEXT NOT NULL CHECK(category IN ('IT Support', 'Facilities', 'Registrar', 'Advising')),
    description TEXT NOT NULL,
    priority TEXT NOT NULL CHECK(priority IN ('Low', 'Medium', 'High', 'Urgent')),
    status TEXT NOT NULL CHECK(status IN ('Pending', 'In Progress', 'Resolved', 'Closed')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
