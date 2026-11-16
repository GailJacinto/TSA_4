package com.mycompany.jacinto_campusconnect_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CampusConnectFrame extends JFrame {
    private final ServiceRequestService service;
    private JTextField txtName, txtStudentId, txtSearchId;
    private JTextArea txtDescription;
    private JComboBox<String> cbCategory, cbPriority, cbStatus;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;

    public CampusConnectFrame() {
        service = new ServiceRequestService();
        setTitle("CampusConnect - TSA4 Desktop Application");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadDataInBackground();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Service Request Entry & Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        formPanel.add(txtName, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Student ID (YYYY-NNNNN):"), gbc);
        gbc.gridx = 3;
        txtStudentId = new JTextField(12);
        formPanel.add(txtStudentId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        cbCategory = new JComboBox<>(new String[]{"IT Support", "Facilities", "Registrar", "Advising"});
        formPanel.add(cbCategory, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 3;
        cbPriority = new JComboBox<>(new String[]{"Low", "Medium", "High", "Urgent"});
        formPanel.add(cbPriority, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDescription = new JTextArea(3, 30);
        txtDescription.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDescription), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Update Status:"), gbc);
        gbc.gridx = 1;
        cbStatus = new JComboBox<>(new String[]{"Pending", "In Progress", "Resolved", "Closed"});
        formPanel.add(cbStatus, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Search/Target ID:"), gbc);
        gbc.gridx = 3;
        txtSearchId = new JTextField(8);
        formPanel.add(txtSearchId, gbc);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Student ID", "Category", "Description", "Priority", "Status", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAdd = new JButton("Add Request");
        JButton btnSearch = new JButton("Find by ID");
        JButton btnUpdate = new JButton("Update Status");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        southPanel.add(buttonPanel, BorderLayout.CENTER);

        lblStatus = new JLabel(" Ready.");
        lblStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        southPanel.add(lblStatus, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addRecord());
        btnSearch.addActionListener(e -> searchRecord());
        btnUpdate.addActionListener(e -> updateRecordStatus());
        btnDelete.addActionListener(e -> deleteRecord());
        btnRefresh.addActionListener(e -> loadDataInBackground());
    }

    private void addRecord() {
        try {
            ServiceRequest req = new ServiceRequest();
            req.setStudentName(txtName.getText().trim());
            req.setStudentId(txtStudentId.getText().trim());
            req.setCategory((String) cbCategory.getSelectedItem());
            req.setDescription(txtDescription.getText().trim());
            req.setPriority((String) cbPriority.getSelectedItem());
            req.setStatus("Pending");

            service.addRequest(req);
            lblStatus.setText(" Success: Request added.");
            clearForm();
            loadDataInBackground();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecord() {
        try {
            int id = Integer.parseInt(txtSearchId.getText().trim());
            ServiceRequest req = service.getRequestById(id);
            if (req != null) {
                JOptionPane.showMessageDialog(this, "Found: ID " + req.getId() + " | " + req.getStudentName() + " | " + req.getStatus(), "Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No record found with ID " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecordStatus() {
        try {
            int id = Integer.parseInt(txtSearchId.getText().trim());
            String newStatus = (String) cbStatus.getSelectedItem();
            if (service.updateStatus(id, newStatus)) {
                loadDataInBackground();
                JOptionPane.showMessageDialog(this, "Status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Record ID not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        try {
            int id = Integer.parseInt(txtSearchId.getText().trim());
            if (JOptionPane.showConfirmDialog(this, "Delete request ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.deleteRequest(id)) {
                    loadDataInBackground();
                    lblStatus.setText(" Deleted record ID " + id);
                } else {
                    JOptionPane.showMessageDialog(this, "Record not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataInBackground() {
        lblStatus.setText(" Loading data...");
        SwingWorker<List<ServiceRequest>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ServiceRequest> doInBackground() throws Exception {
                return service.getAllRequests();
            }
            @Override
            protected void done() {
                try {
                    List<ServiceRequest> list = get();
                    tableModel.setRowCount(0);
                    for (ServiceRequest r : list) {
                        tableModel.addRow(new Object[]{r.getId(), r.getStudentName(), r.getStudentId(), r.getCategory(), r.getDescription(), r.getPriority(), r.getStatus(), r.getCreatedAt()});
                    }
                    lblStatus.setText(" Loaded " + list.size() + " records successfully.");
                } catch (Exception e) {
                    lblStatus.setText(" Load failed.");
                }
            }
        };
        worker.execute();
    }

    private void clearForm() {
        txtName.setText("");
        txtStudentId.setText("");
        txtDescription.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusConnectFrame().setVisible(true));
    }
}