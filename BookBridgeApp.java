package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookBridgeApp extends JFrame {
    private final LibraryService service = new LibraryService();

    private JTable catalogTable, memberTable, loanTable, reservationTable;
    private DefaultTableModel catalogModel, memberModel, loanModel, reservationModel;
    private JLabel statusLabel;

    public BookBridgeApp() {
        setTitle("BookBridge - Desktop Library Application (TSA4)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Catalog", createCatalogPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Circulation", createCirculationPanel());
        tabbedPane.addTab("Reservations", createReservationPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        statusLabel = new JLabel(" Ready.");
        add(statusLabel, BorderLayout.SOUTH);

        refreshAllData();
    }

    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        catalogModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "ISBN", "Total", "Available"}, 0);
        catalogTable = new JTable(catalogModel);
        panel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton addBtn = new JButton("Add Book");
        JButton refreshBtn = new JButton("Refresh Catalog");
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        addBtn.addActionListener(e -> {
            try {
                String title = JOptionPane.showInputDialog(this, "Enter Title:");
                if (title == null || title.trim().isEmpty()) return;
                String author = JOptionPane.showInputDialog(this, "Enter Author:");
                String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
                int copies = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Total Copies:"));

                service.addBook(title, author, isbn, copies);
                statusLabel.setText(" Book added successfully.");
                refreshCatalogAsync();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshCatalogAsync());
        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        memberModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Registered Date"}, 0);
        memberTable = new JTable(memberModel);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton addBtn = new JButton("Register Member");
        topPanel.add(addBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        addBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Enter Name:");
                if (name == null || name.trim().isEmpty()) return;
                String email = JOptionPane.showInputDialog(this, "Enter Email:");
                String phone = JOptionPane.showInputDialog(this, "Enter Phone:");

                service.registerMember(name, email, phone);
                statusLabel.setText(" Member registered successfully.");
                refreshMembersAsync();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private JPanel createCirculationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        loanModel = new DefaultTableModel(new String[]{"Loan ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Return Date", "Status"}, 0);
        loanTable = new JTable(loanModel);
        panel.add(new JScrollPane(loanTable), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton issueBtn = new JButton("Issue Loan");
        JButton returnBtn = new JButton("Return Book");
        topPanel.add(issueBtn);
        topPanel.add(returnBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        issueBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID:"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Member ID:"));
                String dueDate = JOptionPane.showInputDialog(this, "Enter Due Date (YYYY-MM-DD):");

                service.issueLoan(bookId, memberId, dueDate);
                statusLabel.setText(" Loan issued successfully.");
                refreshAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Loan Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnBtn.addActionListener(e -> {
            try {
                int loanId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Active Loan ID:"));
                service.returnLoan(loanId);
                statusLabel.setText(" Book returned successfully.");
                refreshAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Return Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        reservationModel = new DefaultTableModel(new String[]{"Res ID", "Book ID", "Member ID", "Date", "Status"}, 0);
        reservationTable = new JTable(reservationModel);
        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        JButton reserveBtn = new JButton("Make Reservation");
        JButton cancelBtn = new JButton("Cancel Reservation");
        topPanel.add(reserveBtn);
        topPanel.add(cancelBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        reserveBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID:"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Member ID:"));
                service.makeReservation(bookId, memberId);
                statusLabel.setText(" Reservation created.");
                refreshReservationsAsync();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Reservation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> {
            try {
                int resId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Reservation ID:"));
                service.cancelReservation(resId);
                statusLabel.setText(" Reservation cancelled.");
                refreshReservationsAsync();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Cancellation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton generateBtn = new JButton("Generate Summary Report");
        panel.add(generateBtn, BorderLayout.NORTH);

        generateBtn.addActionListener(e -> {
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    List<Book> books = service.getBooks();
                    List<Member> members = service.getMembers();
                    List<Loan> loans = service.getLoans();
                    List<Reservation> res = service.getReservations();

                    return "=== BOOKBRIDGE SYSTEM REPORT ===\n" +
                           "Total Books in Catalog: " + books.size() + "\n" +
                           "Registered Members: " + members.size() + "\n" +
                           "Total Loans Recorded: " + loans.size() + "\n" +
                           "Total Reservations: " + res.size() + "\n";
                }

                @Override
                protected void done() {
                    try {
                        reportArea.setText(get());
                        statusLabel.setText(" Report generated successfully.");
                    } catch (Exception ex) {
                        reportArea.setText("Error generating report: " + ex.getMessage());
                    }
                }
            }.execute();
        });

        return panel;
    }

    private void refreshAllData() {
        refreshCatalogAsync();
        refreshMembersAsync();
        refreshLoansAsync();
        refreshReservationsAsync();
    }

    private void refreshCatalogAsync() {
        new SwingWorker<List<Book>, Void>() {
            protected List<Book> doInBackground() throws Exception { return service.getBooks(); }
            protected void done() {
                try {
                    catalogModel.setRowCount(0);
                    for (Book b : get()) {
                        catalogModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getTotalCopies(), b.getAvailableCopies()});
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    private void refreshMembersAsync() {
        new SwingWorker<List<Member>, Void>() {
            protected List<Member> doInBackground() throws Exception { return service.getMembers(); }
            protected void done() {
                try {
                    memberModel.setRowCount(0);
                    for (Member m : get()) {
                        memberModel.addRow(new Object[]{m.getMemberId(), m.getName(), m.getEmail(), m.getPhone(), m.getRegisteredDate()});
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    private void refreshLoansAsync() {
        new SwingWorker<List<Loan>, Void>() {
            protected List<Loan> doInBackground() throws Exception { return service.getLoans(); }
            protected void done() {
                try {
                    loanModel.setRowCount(0);
                    for (Loan l : get()) {
                        loanModel.addRow(new Object[]{l.getLoanId(), l.getBookId(), l.getMemberId(), l.getBorrowDate(), l.getDueDate(), l.getReturnDate(), l.getStatus()});
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    private void refreshReservationsAsync() {
        new SwingWorker<List<Reservation>, Void>() {
            protected List<Reservation> doInBackground() throws Exception { return service.getReservations(); }
            protected void done() {
                try {
                    reservationModel.setRowCount(0);
                    for (Reservation r : get()) {
                        reservationModel.addRow(new Object[]{r.getReservationId(), r.getBookId(), r.getMemberId(), r.getReservationDate(), r.getStatus()});
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookBridgeApp().setVisible(true));
    }
}