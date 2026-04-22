package rentalmanagementsystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class MainFrame extends JFrame {

    private final PropertyDAO propertyDAO = new PropertyDAO();
    private final TenantDAO tenantDAO = new TenantDAO();
    private final LeaseDAO leaseDAO = new LeaseDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    private JTabbedPane tabbedPane;

    // Properties table
    private JTable propertiesTable;
    private DefaultTableModel propertiesTableModel;
    private TableRowSorter<DefaultTableModel> propertiesSorter;
    private JTextField propertySearchField;
    private JComboBox<String> propertyStatusFilter;
    private JComboBox<String> propertyTypeFilter;

    // Tenants table
    private JTable tenantsTable;
    private DefaultTableModel tenantsTableModel;
    private TableRowSorter<DefaultTableModel> tenantsSorter;
    private JTextField tenantSearchField;
    private JComboBox<String> tenantStatusFilter;

    // Leases table
    private JTable leasesTable;
    private DefaultTableModel leasesTableModel;
    private TableRowSorter<DefaultTableModel> leasesSorter;
    private JTextField leaseSearchField;
    private JComboBox<String> leaseStatusFilter;

    // Payments table
    private JTable paymentsTable;
    private DefaultTableModel paymentsTableModel;
    private TableRowSorter<DefaultTableModel> paymentsSorter;
    private JTextField paymentSearchField;
    private JComboBox<String> paymentStatusFilter;

    private JLabel statusLabel;

    private JLabel lblTotalProperties;
    private JLabel lblAvailableProperties;
    private JLabel lblOccupiedProperties;
    private JLabel lblTotalTenants;
    private JLabel lblActiveLeases;
    private JLabel lblTotalPayments;

    private JButton btnAddHouse;
    private JButton btnAddTownhouse;
    private JButton btnAddFlat;
    private JButton btnViewProperties;
    private JButton btnUpdateProperty;
    private JButton btnDeleteProperty;

    private JButton btnAddTenant;
    private JButton btnViewTenants;
    private JButton btnUpdateTenant;
    private JButton btnDeleteTenant;
    private JButton btnTenantHistory;

    private JButton btnAddLease;
    private JButton btnViewLeases;
    private JButton btnUpdateLease;
    private JButton btnExportLease;
    private JButton btnDeleteLease;
    private JButton btnRecordPayment;
    private JButton btnViewPayments;
    private JButton btnExportInvoice;
    private JButton btnDeletePayment;

    private JButton btnExportReport;
    private JButton btnLogout;

    public MainFrame() {
        setTitle("Rental Management System");
        setSize(1200, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(245, 245, 245));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);

        // Property
        btnAddHouse.addActionListener(e -> addHouse());
        btnAddTownhouse.addActionListener(e -> addTownhouse());
        btnAddFlat.addActionListener(e -> addFlat());
        btnViewProperties.addActionListener(e -> {
            loadPropertiesTable();
            tabbedPane.setSelectedIndex(0);
        });
        btnUpdateProperty.addActionListener(e -> updateProperty());
        btnDeleteProperty.addActionListener(e -> deleteProperty());

        // Tenant
        btnAddTenant.addActionListener(e -> addTenant());
        btnViewTenants.addActionListener(e -> {
            loadTenantsTable();
            tabbedPane.setSelectedIndex(1);
        });
        btnUpdateTenant.addActionListener(e -> updateTenant());
        btnDeleteTenant.addActionListener(e -> deleteTenant());
        btnTenantHistory.addActionListener(e -> viewTenantLeaseHistory());

        // Lease & Payment
        btnAddLease.addActionListener(e -> addLease());
        btnViewLeases.addActionListener(e -> {
            loadLeasesTable();
            tabbedPane.setSelectedIndex(2);
        });
        btnUpdateLease.addActionListener(e -> updateLease());
        btnExportLease.addActionListener(e -> exportLeaseContract());
        btnDeleteLease.addActionListener(e -> deleteLease());

        btnRecordPayment.addActionListener(e -> addPaymentAuto());
        btnViewPayments.addActionListener(e -> {
            loadPaymentsTable();
            tabbedPane.setSelectedIndex(3);
        });
        btnExportInvoice.addActionListener(e -> exportInvoice());
        btnDeletePayment.addActionListener(e -> deletePayment());

        // System
        btnExportReport.addActionListener(e -> exportReport());
        btnLogout.addActionListener(e -> logout());

        refreshDashboard();
        loadPropertiesTable();
        loadTenantsTable();
        loadLeasesTable();
        loadPaymentsTable();
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        headerPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Rental Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(new Color(40, 40, 40));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(createDashboardPanel(), BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        dashboardPanel.setBackground(new Color(245, 245, 245));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        lblTotalProperties = createDashboardValueLabel();
        lblAvailableProperties = createDashboardValueLabel();
        lblOccupiedProperties = createDashboardValueLabel();
        lblTotalTenants = createDashboardValueLabel();
        lblActiveLeases = createDashboardValueLabel();
        lblTotalPayments = createDashboardValueLabel();

        dashboardPanel.add(createDashboardCard("Total Properties", lblTotalProperties));
        dashboardPanel.add(createDashboardCard("Available", lblAvailableProperties));
        dashboardPanel.add(createDashboardCard("Occupied", lblOccupiedProperties));
        dashboardPanel.add(createDashboardCard("Total Tenants", lblTotalTenants));
        dashboardPanel.add(createDashboardCard("Active Leases", lblActiveLeases));
        dashboardPanel.add(createDashboardCard("Payments", lblTotalPayments));

        return dashboardPanel;
    }

    private JPanel createDashboardCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(new Color(70, 70, 70));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createDashboardValueLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    private void refreshDashboard() {
        ArrayList<String> properties = propertyDAO.getAllProperties();
        ArrayList<String> tenants = tenantDAO.getAllTenants();
        ArrayList<String> leases = leaseDAO.getAllLeases();
        ArrayList<String> payments = paymentDAO.getAllPayments();

        int totalProperties = properties.size();
        int availableProperties = 0;
        int occupiedProperties = 0;
        int activeLeases = 0;
        int totalPayments = payments.size();

        for (String property : properties) {
            String lower = property.toLowerCase();
            if (lower.contains("available")) {
                availableProperties++;
            }
            if (lower.contains("occupied")) {
                occupiedProperties++;
            }
        }

        for (String lease : leases) {
            if (lease.toLowerCase().contains("active")) {
                activeLeases++;
            }
        }

        lblTotalProperties.setText(String.valueOf(totalProperties));
        lblAvailableProperties.setText(String.valueOf(availableProperties));
        lblOccupiedProperties.setText(String.valueOf(occupiedProperties));
        lblTotalTenants.setText(String.valueOf(tenants.size()));
        lblActiveLeases.setText(String.valueOf(activeLeases));
        lblTotalPayments.setText(String.valueOf(totalPayments));
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));

        JPanel topControlsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        topControlsPanel.setBackground(new Color(245, 245, 245));

        topControlsPanel.add(createPropertyPanel());
        topControlsPanel.add(createTenantPanel());
        topControlsPanel.add(createLeasePaymentPanel());
        topControlsPanel.add(createSystemPanel());

        centerPanel.add(topControlsPanel, BorderLayout.NORTH);
        centerPanel.add(createTabbedPane(), BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createPropertyPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Property Management",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        panel.setBackground(Color.WHITE);

        btnAddHouse = createStyledButton("Add House");
        btnAddTownhouse = createStyledButton("Add Townhouse");
        btnAddFlat = createStyledButton("Add Flat");
        btnViewProperties = createStyledButton("View Properties");
        btnUpdateProperty = createStyledButton("Update Property");
        btnDeleteProperty = createStyledButton("Delete Property");

        panel.add(btnAddHouse);
        panel.add(btnAddTownhouse);
        panel.add(btnAddFlat);
        panel.add(btnViewProperties);
        panel.add(btnUpdateProperty);
        panel.add(btnDeleteProperty);

        return panel;
    }

    private JPanel createTenantPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Tenant Management",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        panel.setBackground(Color.WHITE);

        btnAddTenant = createStyledButton("Add Tenant");
        btnViewTenants = createStyledButton("View Tenants");
        btnUpdateTenant = createStyledButton("Update Tenant");
        btnDeleteTenant = createStyledButton("Delete Tenant");
        btnTenantHistory = createStyledButton("Tenant Lease History");

        panel.add(btnAddTenant);
        panel.add(btnViewTenants);
        panel.add(btnUpdateTenant);
        panel.add(btnDeleteTenant);
        panel.add(btnTenantHistory);

        return panel;
    }

    private JPanel createLeasePaymentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Lease & Payment Management",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        panel.setBackground(Color.WHITE);

        btnAddLease = createStyledButton("Add Lease");
        btnViewLeases = createStyledButton("View Leases");
        btnUpdateLease = createStyledButton("Update Lease");
        btnDeleteLease = createStyledButton("Terminate Lease");
        btnRecordPayment = createStyledButton("Record Payment");
        btnViewPayments = createStyledButton("View Payments");
        btnDeletePayment = createStyledButton("Delete Payment");
        btnExportLease = createStyledButton("Export Lease");
        btnExportInvoice = createStyledButton("Export Invoice");

        panel.add(btnAddLease);
        panel.add(btnViewLeases);
        panel.add(btnUpdateLease);
        panel.add(btnDeleteLease);
        panel.add(btnRecordPayment);
        panel.add(btnViewPayments);
        panel.add(btnDeletePayment);
        panel.add(btnExportLease);
        panel.add(btnExportInvoice);

        return panel;
    }

    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "System Functions",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        panel.setBackground(Color.WHITE);

        btnExportReport = createStyledButton("Export Report");
        btnLogout = createStyledButton("Logout");

        panel.add(btnExportReport);
        panel.add(btnLogout);

        return panel;
    }

    private JTabbedPane createTabbedPane() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Properties", createPropertiesTabPanel());
        tabbedPane.addTab("Tenants", createTenantsTabPanel());
        tabbedPane.addTab("Leases", createLeasesTabPanel());
        tabbedPane.addTab("Payments", createPaymentsTabPanel());

        return tabbedPane;
    }

    private JPanel createLeasesTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        leaseSearchField = new JTextField(20);
        leaseSearchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        leaseStatusFilter = new JComboBox<>(new String[]{"All", "Active", "Expired", "Terminated"});
        leaseStatusFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton clearFilterBtn = new JButton("Clear");
        clearFilterBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearFilterBtn.setFocusPainted(false);

        filterPanel.add(searchLabel);
        filterPanel.add(leaseSearchField);
        filterPanel.add(statusLabel);
        filterPanel.add(leaseStatusFilter);
        filterPanel.add(clearFilterBtn);

        JScrollPane tableScrollPane = createLeasesTable();

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        leaseSearchField.addActionListener(e -> applyLeaseFilter());
        leaseStatusFilter.addActionListener(e -> applyLeaseFilter());

        clearFilterBtn.addActionListener(e -> {
            leaseSearchField.setText("");
            leaseStatusFilter.setSelectedIndex(0);
            applyLeaseFilter();
        });

        return panel;
    }

    private JScrollPane createLeasesTable() {
        String[] columns = {
            "ID", "Tenant ID", "Property ID", "Start Date", "End Date",
            "Rent", "Deposit", "Due Day", "Grace Period", "Penalty Rate", "Status"
        };

        leasesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leasesTable = new JTable(leasesTableModel);
        leasesTable.setRowHeight(25);
        leasesTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        leasesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        leasesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leasesTable.setRowSelectionAllowed(true);

        leasesSorter = new TableRowSorter<>(leasesTableModel);
        leasesTable.setRowSorter(leasesSorter);

        return new JScrollPane(leasesTable);
    }

    private void loadLeasesTable() {
        leasesTableModel.setRowCount(0);

        ArrayList<String> leases = leaseDAO.getAllLeases();

        for (String l : leases) {
            String[] parts = l.split("\\|");

            if (parts.length >= 11) {
                leasesTableModel.addRow(new Object[]{
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim(),
                    parts[7].trim(),
                    parts[8].trim(),
                    parts[9].trim(),
                    parts[10].trim()
                });
            }
        }

        applyLeaseFilter();
        setStatus("LEASES loaded");
    }

    private void applyLeaseFilter() {
        if (leasesSorter == null) {
            return;
        }

        String searchText = leaseSearchField.getText().trim();
        String selectedStatus = leaseStatusFilter.getSelectedItem().toString();

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
        }

        if (!selectedStatus.equals("All")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 10));
        }

        if (filters.isEmpty()) {
            leasesSorter.setRowFilter(null);
        } else {
            leasesSorter.setRowFilter(RowFilter.andFilter(filters));
        }

        setStatus("Lease filter applied");
    }

    private JPanel createPaymentsTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        paymentSearchField = new JTextField(20);
        paymentSearchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        paymentStatusFilter = new JComboBox<>(new String[]{"All", "Paid", "Late"});
        paymentStatusFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton clearFilterBtn = new JButton("Clear");
        clearFilterBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearFilterBtn.setFocusPainted(false);

        filterPanel.add(searchLabel);
        filterPanel.add(paymentSearchField);
        filterPanel.add(statusLabel);
        filterPanel.add(paymentStatusFilter);
        filterPanel.add(clearFilterBtn);

        JScrollPane tableScrollPane = createPaymentsTable();

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        paymentSearchField.addActionListener(e -> applyPaymentFilter());
        paymentStatusFilter.addActionListener(e -> applyPaymentFilter());

        clearFilterBtn.addActionListener(e -> {
            paymentSearchField.setText("");
            paymentStatusFilter.setSelectedIndex(0);
            applyPaymentFilter();
        });

        return panel;
    }

    private JScrollPane createPaymentsTable() {
        String[] columns = {
            "ID", "Lease ID", "Amount Paid", "Payment Date",
            "Status", "Penalty", "Outstanding Balance"
        };

        paymentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        paymentsTable = new JTable(paymentsTableModel);
        paymentsTable.setRowHeight(25);
        paymentsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        paymentsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentsTable.setRowSelectionAllowed(true);

        paymentsSorter = new TableRowSorter<>(paymentsTableModel);
        paymentsTable.setRowSorter(paymentsSorter);

        return new JScrollPane(paymentsTable);
    }

    private void loadPaymentsTable() {
        paymentsTableModel.setRowCount(0);

        ArrayList<String> payments = paymentDAO.getAllPayments();

        for (String p : payments) {
            String[] parts = p.split("\\|");

            if (parts.length >= 7) {
                paymentsTableModel.addRow(new Object[]{
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim()
                });
            }
        }

        applyPaymentFilter();
        setStatus("PAYMENTS loaded");
    }

    private void applyPaymentFilter() {
        if (paymentsSorter == null) {
            return;
        }

        String searchText = paymentSearchField.getText().trim();
        String selectedStatus = paymentStatusFilter.getSelectedItem().toString();

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
        }

        if (!selectedStatus.equals("All")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 4));
        }

        if (filters.isEmpty()) {
            paymentsSorter.setRowFilter(null);
        } else {
            paymentsSorter.setRowFilter(RowFilter.andFilter(filters));
        }

        setStatus("Payment filter applied");
    }

    private JTextArea createReadOnlyTextArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(10, 10, 10, 10));
        return area;
    }

    private JPanel createPropertiesTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        propertySearchField = new JTextField(20);
        propertySearchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        propertyTypeFilter = new JComboBox<>(new String[]{"All", "House", "Townhouse", "Flat"});
        propertyTypeFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel statusFilterLabel = new JLabel("Status:");
        statusFilterLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        propertyStatusFilter = new JComboBox<>(new String[]{"All", "Available", "Occupied"});
        propertyStatusFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton clearFilterBtn = new JButton("Clear");
        clearFilterBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearFilterBtn.setFocusPainted(false);

        filterPanel.add(searchLabel);
        filterPanel.add(propertySearchField);
        filterPanel.add(typeLabel);
        filterPanel.add(propertyTypeFilter);
        filterPanel.add(statusFilterLabel);
        filterPanel.add(propertyStatusFilter);
        filterPanel.add(clearFilterBtn);

        JScrollPane tableScrollPane = createPropertiesTable();

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        propertySearchField.addActionListener(e -> applyPropertyFilter());
        propertyTypeFilter.addActionListener(e -> applyPropertyFilter());
        propertyStatusFilter.addActionListener(e -> applyPropertyFilter());

        clearFilterBtn.addActionListener(e -> {
            propertySearchField.setText("");
            propertyTypeFilter.setSelectedIndex(0);
            propertyStatusFilter.setSelectedIndex(0);
            applyPropertyFilter();
        });

        return panel;
    }

    private JScrollPane createPropertiesTable() {
        String[] columns = {"ID", "Type", "Address", "Town", "Location", "Rent", "Status"};

        propertiesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        propertiesTable = new JTable(propertiesTableModel);
        propertiesTable.setRowHeight(25);
        propertiesTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        propertiesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        propertiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        propertiesTable.setRowSelectionAllowed(true);

        propertiesSorter = new TableRowSorter<>(propertiesTableModel);
        propertiesTable.setRowSorter(propertiesSorter);

        return new JScrollPane(propertiesTable);
    }

    private void loadPropertiesTable() {
        propertiesTableModel.setRowCount(0);

        ArrayList<String> properties = propertyDAO.getAllProperties();

        for (String p : properties) {
            String[] parts = p.split("\\|");

            if (parts.length >= 7) {
                String id = parts[0].trim();
                String type = parts[1].trim();
                String address = parts[2].trim();
                String town = parts[3].trim();
                String location = parts[4].trim();
                String rent = parts[5].trim();
                String status = parts[6].trim();

                propertiesTableModel.addRow(new Object[]{id, type, address, town, location, rent, status});
            } else {
                propertiesTableModel.addRow(new Object[]{p, "", "", "", "", "", ""});
            }
        }

        applyPropertyFilter();
        setStatus("PROPERTIES loaded");
    }

    private void applyPropertyFilter() {
        if (propertiesSorter == null) {
            return;
        }

        String searchText = propertySearchField.getText().trim();
        String selectedType = propertyTypeFilter.getSelectedItem().toString();
        String selectedStatus = propertyStatusFilter.getSelectedItem().toString();

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
        }

        if (!selectedType.equals("All")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedType) + "$", 1));
        }

        if (!selectedStatus.equals("All")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 5));
        }

        if (filters.isEmpty()) {
            propertiesSorter.setRowFilter(null);
        } else {
            propertiesSorter.setRowFilter(RowFilter.andFilter(filters));
        }

        setStatus("Property filter applied");
    }

    private Integer getSelectedPropertyId() {
        int selectedRow = propertiesTable.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        int modelRow = propertiesTable.convertRowIndexToModel(selectedRow);
        Object value = propertiesTableModel.getValueAt(modelRow, 0);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private JPanel createTenantsTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        tenantSearchField = new JTextField(20);
        tenantSearchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel statusFilterLabel = new JLabel("Status:");
        statusFilterLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        tenantStatusFilter = new JComboBox<>(new String[]{"All", "Active", "Blacklisted"});
        tenantStatusFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton clearFilterBtn = new JButton("Clear");
        clearFilterBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearFilterBtn.setFocusPainted(false);

        filterPanel.add(searchLabel);
        filterPanel.add(tenantSearchField);
        filterPanel.add(statusFilterLabel);
        filterPanel.add(tenantStatusFilter);
        filterPanel.add(clearFilterBtn);

        JScrollPane tableScrollPane = createTenantsTable();

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        tenantSearchField.addActionListener(e -> applyTenantFilter());
        tenantStatusFilter.addActionListener(e -> applyTenantFilter());

        clearFilterBtn.addActionListener(e -> {
            tenantSearchField.setText("");
            tenantStatusFilter.setSelectedIndex(0);
            applyTenantFilter();
        });

        return panel;
    }

    private JScrollPane createTenantsTable() {
        String[] columns = {"ID", "First Name", "Last Name", "Phone", "Email", "Status", "Financial Status"};

        tenantsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tenantsTable = new JTable(tenantsTableModel);
        tenantsTable.setRowHeight(25);
        tenantsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tenantsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tenantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tenantsTable.setRowSelectionAllowed(true);

        tenantsSorter = new TableRowSorter<>(tenantsTableModel);
        tenantsTable.setRowSorter(tenantsSorter);

        return new JScrollPane(tenantsTable);
    }

    private void applyTenantFilter() {
        if (tenantsSorter == null) {
            return;
        }

        String searchText = tenantSearchField.getText().trim();
        String selectedStatus = tenantStatusFilter.getSelectedItem().toString();

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
        }

        if (!selectedStatus.equals("All")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 5));
        }

        if (filters.isEmpty()) {
            tenantsSorter.setRowFilter(null);
        } else {
            tenantsSorter.setRowFilter(RowFilter.andFilter(filters));
        }

        setStatus("Tenant filter applied");
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        panel.setBackground(new Color(245, 245, 245));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panel.add(statusLabel, BorderLayout.WEST);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }

    private void loadTenantsTable() {
        tenantsTableModel.setRowCount(0);

        ArrayList<String> tenants = tenantDAO.getAllTenants();

        for (String t : tenants) {
            String[] parts = t.split("\\|");

            if (parts.length >= 5) {
                String id = parts[0].trim();
                String fullName = parts[1].trim();
                String phone = parts[2].trim();
                String email = parts[3].trim();
                String status = parts[4].trim();

                String financialStatus = paymentDAO.getTenantFinancialStatus(Integer.parseInt(id));

                String firstName = "";
                String lastName = "";

                String[] nameParts = fullName.split(" ", 2);
                if (nameParts.length >= 1) {
                    firstName = nameParts[0].trim();
                }
                if (nameParts.length == 2) {
                    lastName = nameParts[1].trim();
                }

                tenantsTableModel.addRow(new Object[]{
                    id,
                    firstName,
                    lastName,
                    phone,
                    email,
                    status,
                    financialStatus
                });
            } else {
                tenantsTableModel.addRow(new Object[]{t, "", "", "", "", "", ""});
            }
        }

        applyTenantFilter();
        setStatus("TENANTS loaded");
    }

    private Integer getSelectedTenantId() {
        int selectedRow = tenantsTable.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        int modelRow = tenantsTable.convertRowIndexToModel(selectedRow);
        Object value = tenantsTableModel.getValueAt(modelRow, 0);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidName(String value) {
        return value != null && value.trim().matches("[A-Za-z ]{2,50}");
    }

    private boolean isValidPhone(String value) {
        return value != null && value.trim().matches("\\+?[0-9]{7,15}");
    }

    private boolean isValidEmail(String value) {
        return value != null && value.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showValidationMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private String askRequiredText(String message, String fieldName) {
        String value = JOptionPane.showInputDialog(this, message);

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            showValidationMessage(fieldName + " cannot be empty.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private Integer getSelectedPaymentId() {
        int selectedRow = paymentsTable.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        int modelRow = paymentsTable.convertRowIndexToModel(selectedRow);
        Object value = paymentsTableModel.getValueAt(modelRow, 0);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private void exportInvoice() {
        try {
            Integer paymentId = getSelectedPaymentId();

            if (paymentId == null) {
                paymentId = askPositiveInteger("Enter payment ID:", "Payment ID");
                if (paymentId == null) {
                    return;
                }
            }

            Payment payment = paymentDAO.getPaymentById(paymentId);

            if (payment == null) {
                JOptionPane.showMessageDialog(this, "Payment not found.");
                setStatus("Payment not found");
                return;
            }

            Lease lease = leaseDAO.getLeaseById(payment.getLeaseId());

            try (FileWriter writer = new FileWriter("Invoice_" + paymentId + ".txt")) {
                writer.write("=========== TENANT INVOICE ===========\n\n");
                writer.write("Invoice Payment ID: " + payment.getPaymentId() + "\n");
                writer.write("Lease ID: " + payment.getLeaseId() + "\n");

                if (lease != null) {
                    writer.write("Tenant ID: " + lease.getTenantId() + "\n");
                    writer.write("Property ID: " + lease.getPropertyId() + "\n");
                }

                writer.write("Payment Date: " + payment.getPaymentDate() + "\n");
                writer.write("Amount Paid: " + payment.getAmountPaid() + "\n");
                writer.write("Payment Status: " + payment.getPaymentStatus() + "\n");
                writer.write("Penalty Amount: " + payment.getPenaltyAmount() + "\n");
                writer.write("Outstanding Balance: " + payment.getOutstandingBalance() + "\n");
            }

            JOptionPane.showMessageDialog(this, "Invoice exported successfully.");
            setStatus("Invoice exported successfully");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invoice export failed.");
            setStatus("Invoice export failed");
        }
    }

    private Integer askInteger(String message, String fieldName) {
        String value = JOptionPane.showInputDialog(this, message);

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            showValidationMessage(fieldName + " cannot be empty.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            showValidationMessage(fieldName + " must be a valid whole number.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }
    }

    private Integer askPositiveInteger(String message, String fieldName) {
        Integer value = askInteger(message, fieldName);

        if (value == null) {
            return null;
        }

        if (value <= 0) {
            showValidationMessage(fieldName + " must be greater than 0.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private Integer askNonNegativeInteger(String message, String fieldName) {
        Integer value = askInteger(message, fieldName);

        if (value == null) {
            return null;
        }

        if (value < 0) {
            showValidationMessage(fieldName + " cannot be negative.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private Integer askIntegerInRange(String message, String fieldName, int min, int max) {
        Integer value = askInteger(message, fieldName);

        if (value == null) {
            return null;
        }

        if (value < min || value > max) {
            showValidationMessage(fieldName + " must be between " + min + " and " + max + ".");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private Double askDouble(String message, String fieldName) {
        String value = JOptionPane.showInputDialog(this, message);

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            showValidationMessage(fieldName + " cannot be empty.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            showValidationMessage(fieldName + " must be a valid number.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }
    }

    private Double askPositiveDouble(String message, String fieldName) {
        Double value = askDouble(message, fieldName);

        if (value == null) {
            return null;
        }

        if (value <= 0) {
            showValidationMessage(fieldName + " must be greater than 0.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private Double askNonNegativeDouble(String message, String fieldName) {
        Double value = askDouble(message, fieldName);

        if (value == null) {
            return null;
        }

        if (value < 0) {
            showValidationMessage(fieldName + " cannot be negative.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        return value;
    }

    private LocalDate askDate(String message, String fieldName) {
        String value = JOptionPane.showInputDialog(this, message);

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isEmpty()) {
            showValidationMessage(fieldName + " cannot be empty.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            showValidationMessage(fieldName + " must be a valid date in format YYYY-MM-DD.");
            setStatus("Invalid " + fieldName.toLowerCase());
            return null;
        }
    }

    private String chooseTenantStatus(String currentStatus) {
        String[] options = {"Active", "Blacklisted"};
        JComboBox<String> statusBox = new JComboBox<>(options);

        if (currentStatus != null) {
            statusBox.setSelectedItem(currentStatus);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                statusBox,
                "Select Tenant Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return statusBox.getSelectedItem().toString();
        }

        return null;
    }

    private String choosePropertyStatus(String currentStatus) {
        String[] options = {"Available", "Occupied"};
        JComboBox<String> statusBox = new JComboBox<>(options);

        if (currentStatus != null) {
            statusBox.setSelectedItem(currentStatus);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                statusBox,
                "Select Property Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return statusBox.getSelectedItem().toString();
        }

        return null;
    }

    private void addHouse() {
        try {
            String address = askRequiredText("Enter address:", "Address");
            if (address == null) {
                return;
            }

            String town = askRequiredText("Enter town:", "Town");
            if (town == null) {
                return;
            }

            String location = askRequiredText("Enter suburb/location:", "Location");
            if (location == null) {
                return;
            }

            Double buildingSize = askPositiveDouble("Enter building size:", "Building size");
            if (buildingSize == null) {
                return;
            }

            Double marketValue = askPositiveDouble("Enter market value:", "Market value");
            if (marketValue == null) {
                return;
            }

            Double rent = askPositiveDouble("Enter monthly rent:", "Rent");
            if (rent == null) {
                return;
            }

            Double plotSize = askPositiveDouble("Enter plot size:", "Plot size");
            if (plotSize == null) {
                return;
            }

            House house = new House(0, buildingSize, address, town, location, marketValue, rent, "Available", plotSize);

            if (propertyDAO.addProperty(house)) {
                JOptionPane.showMessageDialog(this, "House added successfully.");
                setStatus("House added successfully");
                refreshDashboard();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add house.");
                setStatus("Failed to add house");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid house input.");
            setStatus("Invalid house input");
        }
    }

    private void addTownhouse() {
        try {
            String address = askRequiredText("Enter address:", "Address");
            if (address == null) {
                return;
            }

            String town = askRequiredText("Enter town:", "Town");
            if (town == null) {
                return;
            }

            String location = askRequiredText("Enter suburb/location:", "Location");
            if (location == null) {
                return;
            }

            Double buildingSize = askPositiveDouble("Enter building size:", "Building size");
            if (buildingSize == null) {
                return;
            }

            Double marketValue = askPositiveDouble("Enter market value:", "Market value");
            if (marketValue == null) {
                return;
            }

            Double rent = askPositiveDouble("Enter monthly rent:", "Rent");
            if (rent == null) {
                return;
            }

            String unitNumber = askRequiredText("Enter unit number:", "Unit number");
            if (unitNumber == null) {
                return;
            }

            boolean backyard = JOptionPane.showConfirmDialog(
                    this,
                    "Does it have a backyard?",
                    "Backyard",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION;

            Townhouse townhouse = new Townhouse(
                    0, buildingSize, address, town, location, marketValue, rent,
                    "Available", unitNumber, backyard
            );

            if (propertyDAO.addProperty(townhouse)) {
                JOptionPane.showMessageDialog(this, "Townhouse added successfully.");
                setStatus("Townhouse added successfully");
                refreshDashboard();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add townhouse.");
                setStatus("Failed to add townhouse");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid townhouse input.");
            setStatus("Invalid townhouse input");
        }
    }

    private void addFlat() {
        try {
            String address = askRequiredText("Enter address:", "Address");
            if (address == null) {
                return;
            }

            String town = askRequiredText("Enter town:", "Town");
            if (town == null) {
                return;
            }

            String location = askRequiredText("Enter suburb/location:", "Location");
            if (location == null) {
                return;
            }

            Double buildingSize = askPositiveDouble("Enter building size:", "Building size");
            if (buildingSize == null) {
                return;
            }

            Double marketValue = askPositiveDouble("Enter market value:", "Market value");
            if (marketValue == null) {
                return;
            }

            Double rent = askPositiveDouble("Enter monthly rent:", "Rent");
            if (rent == null) {
                return;
            }

            String unitNumber = askRequiredText("Enter unit number:", "Unit number");
            if (unitNumber == null) {
                return;
            }

            Integer floorLevel = askIntegerInRange("Enter floor level:", "Floor level", 0, 100);
            if (floorLevel == null) {
                return;
            }

            boolean elevator = JOptionPane.showConfirmDialog(
                    this,
                    "Elevator access?",
                    "Elevator",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION;

            boolean backyard = JOptionPane.showConfirmDialog(
                    this,
                    "Backyard available?",
                    "Backyard",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION;

            Flat flat = new Flat(
                    0, buildingSize, address, town, location, marketValue, rent,
                    "Available", unitNumber, floorLevel, elevator, backyard
            );

            if (propertyDAO.addProperty(flat)) {
                JOptionPane.showMessageDialog(this, "Flat added successfully.");
                setStatus("Flat added successfully");
                refreshDashboard();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add flat.");
                setStatus("Failed to add flat");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid flat input.");
            setStatus("Invalid flat input");
        }
    }

    private void addTenant() {
        try {
            String firstName = JOptionPane.showInputDialog(this, "Enter first name:");
            if (firstName == null) {
                return;
            }
            firstName = firstName.trim();

            if (!isValidName(firstName)) {
                showValidationMessage("First name must contain letters only and be 2 to 50 characters long.");
                setStatus("Invalid first name");
                return;
            }

            String lastName = JOptionPane.showInputDialog(this, "Enter last name:");
            if (lastName == null) {
                return;
            }
            lastName = lastName.trim();

            if (!isValidName(lastName)) {
                showValidationMessage("Last name must contain letters only and be 2 to 50 characters long.");
                setStatus("Invalid last name");
                return;
            }

            String idPassport = JOptionPane.showInputDialog(this, "Enter ID/Passport:");
            if (idPassport == null) {
                return;
            }
            idPassport = idPassport.trim();

            if (idPassport.isEmpty()) {
                showValidationMessage("ID/Passport cannot be empty.");
                setStatus("Invalid ID/Passport");
                return;
            }

            String phone = JOptionPane.showInputDialog(this, "Enter phone number:");
            if (phone == null) {
                return;
            }
            phone = phone.trim();

            if (!isValidPhone(phone)) {
                showValidationMessage("Phone number must contain only digits and be between 7 and 15 digits.");
                setStatus("Invalid phone number");
                return;
            }

            String email = JOptionPane.showInputDialog(this, "Enter email:");
            if (email == null) {
                return;
            }
            email = email.trim();

            if (!isValidEmail(email)) {
                showValidationMessage("Please enter a valid email address.");
                setStatus("Invalid email");
                return;
            }

            String status = chooseTenantStatus("Active");
            if (status == null) {
                setStatus("Tenant add cancelled");
                return;
            }

            Tenant tenant = new Tenant(0, firstName, lastName, idPassport, phone, email, status);

            if (tenantDAO.addTenant(tenant)) {
                JOptionPane.showMessageDialog(this, "Tenant added successfully.");
                setStatus("Tenant added successfully");
                loadTenantsTable();
                tabbedPane.setSelectedIndex(1);
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add tenant.");
                setStatus("Failed to add tenant");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid tenant input.");
            setStatus("Invalid tenant input");
        }
    }

    private void addLease() {
        try {
            Integer tenantId = askPositiveInteger("Enter tenant ID:", "Tenant ID");
            if (tenantId == null) {
                return;
            }

            Integer propertyId = askPositiveInteger("Enter property ID:", "Property ID");
            if (propertyId == null) {
                return;
            }

            LocalDate startDate = askDate("Enter start date (YYYY-MM-DD):", "Start date");
            if (startDate == null) {
                return;
            }

            LocalDate endDate = askDate("Enter end date (YYYY-MM-DD):", "End date");
            if (endDate == null) {
                return;
            }

            if (!endDate.isAfter(startDate)) {
                showValidationMessage("End date must be after start date.");
                setStatus("Invalid lease dates");
                return;
            }

            Double rent = askPositiveDouble("Enter monthly rent:", "Rent");
            if (rent == null) {
                return;
            }

            Double deposit = askPositiveDouble("Enter security deposit:", "Deposit");
            if (deposit == null) {
                return;
            }

            Integer dueDay = askIntegerInRange("Enter payment due day (1-31):", "Due day", 1, 31);
            if (dueDay == null) {
                return;
            }

            Integer grace = askNonNegativeInteger("Enter grace period in days:", "Grace period");
            if (grace == null) {
                return;
            }

            Double penaltyRate = askNonNegativeDouble("Enter late penalty rate (e.g. 0.10):", "Penalty rate");
            if (penaltyRate == null) {
                return;
            }

            Lease lease = new Lease(
                    0, tenantId, propertyId, startDate, endDate,
                    rent, deposit, dueDay, grace, penaltyRate, "Active"
            );

            if (leaseDAO.addLease(lease)) {
                JOptionPane.showMessageDialog(this, "Lease added successfully.");
                setStatus("Lease added successfully");
                refreshDashboard();
                loadLeasesTable();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add lease. Property may already be occupied.");
                setStatus("Failed to add lease");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid lease input.");
            setStatus("Invalid lease input");
        }
    }

    private void updateLease() {
        try {
            Integer leaseId = getSelectedLeaseId();

            if (leaseId == null) {
                leaseId = askPositiveInteger("Enter lease ID:", "Lease ID");
                if (leaseId == null) {
                    return;
                }
            }

            String[] options = {
                "End Date", "Rent", "Deposit", "Due Day", "Grace Period", "Penalty Rate", "Status"
            };

            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Select what to update:",
                    "Update Lease",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) {
                return;
            }

            boolean success = false;

            switch (choice) {
                case "End Date":
                    LocalDate endDate = askDate("Enter new end date (YYYY-MM-DD):", "End Date");
                    if (endDate == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseEndDate(leaseId, endDate);
                    break;

                case "Rent":
                    Double rent = askPositiveDouble("Enter new rent:", "Rent");
                    if (rent == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseRent(leaseId, rent);
                    break;

                case "Deposit":
                    Double deposit = askPositiveDouble("Enter new deposit:", "Deposit");
                    if (deposit == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseDeposit(leaseId, deposit);
                    break;

                case "Due Day":
                    Integer due = askIntegerInRange("Enter due day:", "Due Day", 1, 31);
                    if (due == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseDueDay(leaseId, due);
                    break;

                case "Grace Period":
                    Integer grace = askNonNegativeInteger("Enter grace period:", "Grace Period");
                    if (grace == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseGrace(leaseId, grace);
                    break;

                case "Penalty Rate":
                    Double rate = askNonNegativeDouble("Enter penalty rate:", "Penalty Rate");
                    if (rate == null) {
                        return;
                    }
                    success = leaseDAO.updateLeasePenaltyRate(leaseId, rate);
                    break;

                case "Status":
                    String status = (String) JOptionPane.showInputDialog(
                            this,
                            "Select status:",
                            "Status",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            new String[]{"Active", "Expired", "Terminated"},
                            "Active"
                    );
                    if (status == null) {
                        return;
                    }
                    success = leaseDAO.updateLeaseStatus(leaseId, status);
                    break;
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lease updated.");
                setStatus("Lease updated");
                loadLeasesTable();
                refreshDashboard();
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
                setStatus("Update failed");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating lease.");
            setStatus("Error updating lease");
        }
    }

    private void exportLeaseContract() {
        try {
            Integer leaseId = getSelectedLeaseId();

            if (leaseId == null) {
                leaseId = askPositiveInteger("Enter lease ID:", "Lease ID");
                if (leaseId == null) {
                    return;
                }
            }

            Lease lease = leaseDAO.getLeaseById(leaseId);
            if (lease == null) {
                JOptionPane.showMessageDialog(this, "Lease not found.");
                setStatus("Lease not found");
                return;
            }

            try (FileWriter writer = new FileWriter("Lease_" + leaseId + "_Contract.txt")) {
                writer.write("=========== LEASE CONTRACT ===========\n\n");
                writer.write("Lease ID: " + lease.getLeaseId() + "\n");
                writer.write("Tenant ID: " + lease.getTenantId() + "\n");
                writer.write("Property ID: " + lease.getPropertyId() + "\n");
                writer.write("Start Date: " + lease.getStartDate() + "\n");
                writer.write("End Date: " + lease.getEndDate() + "\n");
                writer.write("Monthly Rent: " + lease.getMonthlyRentAmount() + "\n");
                writer.write("Security Deposit: " + lease.getSecurityDeposit() + "\n");
                writer.write("Payment Due Day: " + lease.getPaymentDueDay() + "\n");
                writer.write("Grace Period: " + lease.getGracePeriod() + "\n");
                writer.write("Late Penalty Rate: " + lease.getLatePenaltyRate() + "\n");
                writer.write("Status: " + lease.getStatus() + "\n");
            }

            JOptionPane.showMessageDialog(this,
                    "Lease contract exported successfully.");
            setStatus("Lease contract exported");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed.");
            setStatus("Lease export failed");
        }
    }

    private Integer getSelectedLeaseId() {
        int selectedRow = leasesTable.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        int modelRow = leasesTable.convertRowIndexToModel(selectedRow);
        Object value = leasesTableModel.getValueAt(modelRow, 0);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private void addPaymentAuto() {
        try {
            Integer leaseId = askPositiveInteger("Enter lease ID:", "Lease ID");
            if (leaseId == null) {
                return;
            }

            Double amount = askPositiveDouble("Enter amount paid:", "Amount");
            if (amount == null) {
                return;
            }

            LocalDate paymentDate = askDate("Enter payment date (YYYY-MM-DD):", "Payment date");
            if (paymentDate == null) {
                return;
            }

            Payment payment = new Payment(0, leaseId, amount, paymentDate, "", 0, 0);

            if (paymentDAO.addPaymentAuto(payment)) {
                JOptionPane.showMessageDialog(this, "Payment recorded successfully. Penalty was calculated automatically.");
                setStatus("Payment recorded successfully");
                refreshDashboard();
                loadPaymentsTable();
                tabbedPane.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record payment.");
                setStatus("Failed to record payment");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid payment input.");
            setStatus("Invalid payment input");
        }
    }

    private void updateProperty() {
        try {
            Integer propertyId = getSelectedPropertyId();

            if (propertyId == null) {
                propertyId = askPositiveInteger("Enter property ID:", "Property ID");
                if (propertyId == null) {
                    return;
                }
            }

            String[] options = {"Address", "Town", "Location", "Market Value", "Rent", "Status"};

            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Select what to update:",
                    "Update Property",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) {
                setStatus("Property update cancelled");
                return;
            }

            boolean success = false;

            switch (choice) {
                case "Address":
                    String address = askRequiredText("Enter new address:", "Address");
                    if (address == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyAddress(propertyId, address);
                    break;

                case "Town":
                    String town = askRequiredText("Enter new town:", "Town");
                    if (town == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyTown(propertyId, town);
                    break;

                case "Location":
                    String location = askRequiredText("Enter new suburb/location:", "Location");
                    if (location == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyLocation(propertyId, location);
                    break;

                case "Market Value":
                    Double marketValue = askPositiveDouble("Enter new market value:", "Market Value");
                    if (marketValue == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyMarketValue(propertyId, marketValue);
                    break;

                case "Rent":
                    Double rent = askPositiveDouble("Enter new monthly rent:", "Rent");
                    if (rent == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyRent(propertyId, rent);
                    break;

                case "Status":
                    String status = choosePropertyStatus("Available");
                    if (status == null) {
                        return;
                    }
                    success = propertyDAO.updatePropertyStatus(propertyId, status);
                    break;
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Property updated successfully.");
                setStatus("Property updated successfully");
                refreshDashboard();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Property update failed.");
                setStatus("Property update failed");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid property update input.");
            setStatus("Invalid property update input");
        }
    }

    private void deleteProperty() {
        try {
            Integer propertyId = getSelectedPropertyId();

            if (propertyId == null) {
                propertyId = askPositiveInteger("Enter property ID to delete:", "Property ID");
                if (propertyId == null) {
                    return;
                }
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this property?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                setStatus("Property delete cancelled");
                return;
            }

            if (propertyDAO.deleteProperty(propertyId)) {
                JOptionPane.showMessageDialog(this, "Property deleted successfully.");
                setStatus("Property deleted successfully");
                refreshDashboard();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Property delete failed.");
                setStatus("Property delete failed");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid property delete input.");
            setStatus("Invalid property delete input");
        }
    }

    private void updateTenant() {
        try {
            Integer tenantId = getSelectedTenantId();

            if (tenantId == null) {
                tenantId = askPositiveInteger("Enter tenant ID:", "Tenant ID");
                if (tenantId == null) {
                    return;
                }
            }

            String[] options = {"First Name", "Last Name", "Phone", "Email", "Status"};

            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Select what to update:",
                    "Update Tenant",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) {
                setStatus("Update cancelled");
                return;
            }

            boolean success = false;

            switch (choice) {
                case "First Name":
                    String firstName = JOptionPane.showInputDialog(this, "Enter new first name:");
                    if (firstName == null || !isValidName(firstName.trim())) {
                        showValidationMessage("Invalid first name.");
                        return;
                    }
                    success = tenantDAO.updateTenantFirstName(tenantId, firstName.trim());
                    break;

                case "Last Name":
                    String lastName = JOptionPane.showInputDialog(this, "Enter new last name:");
                    if (lastName == null || !isValidName(lastName.trim())) {
                        showValidationMessage("Invalid last name.");
                        return;
                    }
                    success = tenantDAO.updateTenantLastName(tenantId, lastName.trim());
                    break;

                case "Phone":
                    String phone = JOptionPane.showInputDialog(this, "Enter new phone:");
                    if (phone == null || !isValidPhone(phone.trim())) {
                        showValidationMessage("Invalid phone number.");
                        return;
                    }
                    success = tenantDAO.updateTenantPhone(tenantId, phone.trim());
                    break;

                case "Email":
                    String email = JOptionPane.showInputDialog(this, "Enter new email:");
                    if (email == null || !isValidEmail(email.trim())) {
                        showValidationMessage("Invalid email.");
                        return;
                    }
                    success = tenantDAO.updateTenantEmail(tenantId, email.trim());
                    break;

                case "Status":
                    String status = chooseTenantStatus("Active");
                    if (status == null) {
                        return;
                    }
                    success = tenantDAO.updateTenantStatus(tenantId, status);
                    break;
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Tenant updated successfully.");
                setStatus("Tenant updated");
                loadTenantsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
                setStatus("Update failed");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating tenant.");
            setStatus("Error updating tenant");
        }
    }

    private void viewTenantLeaseHistory() {
        try {
            Integer tenantId = getSelectedTenantId();

            if (tenantId == null) {
                tenantId = askPositiveInteger("Enter tenant ID:", "Tenant ID");
                if (tenantId == null) {
                    return;
                }
            }

            ArrayList<String> history = leaseDAO.getLeasesByTenantId(tenantId);

            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No lease history found for tenant ID " + tenantId + ".");
                setStatus("No tenant lease history found");
                return;
            }

            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setMargin(new Insets(10, 10, 10, 10));

            area.append("=========== TENANT LEASE HISTORY ===========\n\n");
            area.append("Tenant ID: " + tenantId + "\n\n");

            for (String lease : history) {
                String[] parts = lease.split("\\|");

                if (parts.length >= 11) {
                    area.append("Lease ID: " + parts[0].trim() + "\n");
                    area.append("Property ID: " + parts[2].trim() + "\n");
                    area.append("Start Date: " + parts[3].trim() + "\n");
                    area.append("End Date: " + parts[4].trim() + "\n");
                    area.append("Monthly Rent: " + parts[5].trim() + "\n");
                    area.append("Deposit: " + parts[6].trim() + "\n");
                    area.append("Due Day: " + parts[7].trim() + "\n");
                    area.append("Grace Period: " + parts[8].trim() + "\n");
                    area.append("Penalty Rate: " + parts[9].trim() + "\n");
                    area.append("Status: " + parts[10].trim() + "\n");
                    area.append("--------------------------------------------\n");
                } else {
                    area.append(lease + "\n");
                }
            }

            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setPreferredSize(new Dimension(700, 400));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Tenant Lease History",
                    JOptionPane.INFORMATION_MESSAGE
            );

            setStatus("Tenant lease history loaded");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tenant lease history.");
            setStatus("Error loading tenant lease history");
        }
    }

    private void deleteTenant() {
        try {
            Integer tenantId = getSelectedTenantId();

            if (tenantId == null) {
                String input = JOptionPane.showInputDialog(this, "Enter tenant ID to delete:");
                if (input == null || input.trim().isEmpty()) {
                    setStatus("Tenant delete cancelled");
                    return;
                }
                tenantId = Integer.parseInt(input.trim());
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this tenant?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                setStatus("Tenant delete cancelled");
                return;
            }

            if (tenantDAO.deleteTenant(tenantId)) {
                JOptionPane.showMessageDialog(this, "Tenant deleted successfully.");
                setStatus("Tenant deleted successfully");
                loadTenantsTable();
                tabbedPane.setSelectedIndex(1);
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Tenant delete failed.");
                setStatus("Tenant delete failed");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid tenant delete input.");
            setStatus("Invalid tenant delete input");
        }
    }

    private void deleteLease() {
        try {
            Integer leaseId = askPositiveInteger("Enter lease ID to terminate:", "Lease ID");
            if (leaseId == null) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to terminate this lease?\nThe lease record will stay for history.",
                    "Confirm Termination",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                setStatus("Lease termination cancelled");
                return;
            }

            if (leaseDAO.terminateLease(leaseId)) {
                JOptionPane.showMessageDialog(this,
                        "Lease terminated successfully. History was preserved.");
                setStatus("Lease terminated successfully");
                refreshDashboard();
                loadLeasesTable();
                loadPropertiesTable();
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "Lease termination failed.");
                setStatus("Lease termination failed");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid lease termination input.");
            setStatus("Invalid lease termination input");
        }
    }

    private void deletePayment() {
        try {
            Integer paymentId = askPositiveInteger("Enter payment ID to delete:", "Payment ID");
            if (paymentId == null) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this payment?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                setStatus("Payment delete cancelled");
                return;
            }

            if (paymentDAO.deletePayment(paymentId)) {
                JOptionPane.showMessageDialog(this, "Payment deleted successfully.");
                setStatus("Payment deleted successfully");
                refreshDashboard();
                loadPaymentsTable();
                tabbedPane.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(this, "Payment delete failed.");
                setStatus("Payment delete failed");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid payment delete input.");
            setStatus("Invalid payment delete input");
        }
    }

    private void exportReport() {

        double totalRevenue = 0;
        double overdueTotal = 0;

        ArrayList<String> payments = paymentDAO.getAllPayments();

        for (String p : payments) {
            String[] parts = p.split("\\|");

            if (parts.length >= 7) {
                double amountPaid = Double.parseDouble(parts[2].trim());
                String status = parts[4].trim();
                double penalty = Double.parseDouble(parts[5].trim());
                double outstanding = Double.parseDouble(parts[6].trim());

                totalRevenue += amountPaid;

                if (status.equalsIgnoreCase("Late") || outstanding > 0) {
                    overdueTotal += outstanding;
                }
            }
        }

        int activeLeases = 0;
        int expiredLeases = 0;
        int terminatedLeases = 0;

        ArrayList<String> leases = leaseDAO.getAllLeases();

        for (String l : leases) {
            String[] parts = l.split("\\|");

            if (parts.length >= 1) {
                String status = parts[parts.length - 1].trim();

                if (status.equalsIgnoreCase("Active")) {
                    activeLeases++;
                } else if (status.equalsIgnoreCase("Expired")) {
                    expiredLeases++;
                } else if (status.equalsIgnoreCase("Terminated")) {
                    terminatedLeases++;
                }
            }
        }

        HashMap<Integer, Double> propertyRevenue = new HashMap<>();

        for (String p : payments) {
            String[] parts = p.split("\\|");

            if (parts.length >= 6) {
                int leaseId = Integer.parseInt(parts[1].replace("Lease:", "").trim());
                double amount = Double.parseDouble(parts[2].trim());

                Lease lease = leaseDAO.getLeaseById(leaseId);

                if (lease != null) {
                    int propertyId = lease.getPropertyId();
                    propertyRevenue.put(propertyId,
                            propertyRevenue.getOrDefault(propertyId, 0.0) + amount);
                }
            }
        }

        int bestPropertyId = -1;
        double maxRevenue = 0;

        for (int propId : propertyRevenue.keySet()) {
            if (propertyRevenue.get(propId) > maxRevenue) {
                maxRevenue = propertyRevenue.get(propId);
                bestPropertyId = propId;
            }
        }

        try (FileWriter writer = new FileWriter("Rental Status Report.csv")) {

            writer.write("=== PROPERTIES ===\n");
            for (String p : propertyDAO.getAllProperties()) {
                writer.write(p + "\n");
            }

            writer.write("\n=== TENANTS ===\n");
            for (String t : tenantDAO.getAllTenants()) {
                writer.write(t + "\n");
            }

            writer.write("\n=== LEASES ===\n");
            for (String l : leaseDAO.getAllLeases()) {
                writer.write(l + "\n");
            }

            writer.write("\n=== PAYMENTS ===\n");
            for (String p : paymentDAO.getAllPayments()) {
                writer.write(p + "\n");
            }

            writer.write("\n=== REPORT SUMMARY ===\n\n");

            writer.write("Total Revenue: " + totalRevenue + "\n");
            writer.write("Outstanding (Overdue): " + overdueTotal + "\n\n");

            writer.write("Lease Status:\n");
            writer.write("Active: " + activeLeases + "\n");
            writer.write("Expired: " + expiredLeases + "\n");
            writer.write("Terminated: " + terminatedLeases + "\n\n");

            writer.write("Most Profitable Property ID: " + bestPropertyId + "\n");
            writer.write("Revenue Generated: " + maxRevenue + "\n");

            JOptionPane.showMessageDialog(this, "Report exported successfully.");
            setStatus("Report exported successfully");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting report.");
            setStatus("Error exporting report");
        }
    }

    private void logout() {
        dispose();
        new LoginFrame();
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
