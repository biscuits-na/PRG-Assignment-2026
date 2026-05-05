package rentalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;

public class LeaseDAO {

    private final PropertyDAO propertyDAO = new PropertyDAO();

    public boolean addLease(Lease lease) {
        if (!propertyDAO.isPropertyAvailable(lease.getPropertyId())) {
            System.out.println("Property is already occupied.");
            return false;
        }

        String sql = "INSERT INTO leases(tenant_id, property_id, start_date, end_date, monthly_rent_amount, "
                + "security_deposit, payment_due_day, grace_period, late_penalty_rate, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lease.getTenantId());
            ps.setInt(2, lease.getPropertyId());
            ps.setDate(3, java.sql.Date.valueOf(lease.getStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(lease.getEndDate()));
            ps.setDouble(5, lease.getMonthlyRentAmount());
            ps.setDouble(6, lease.getSecurityDeposit());
            ps.setInt(7, lease.getPaymentDueDay());
            ps.setInt(8, lease.getGracePeriod());
            ps.setDouble(9, lease.getLatePenaltyRate());
            ps.setString(10, lease.getStatus());

            boolean inserted = ps.executeUpdate() > 0;

            if (inserted) {
                propertyDAO.updateAvailability(lease.getPropertyId(), "Occupied");
            }

            return inserted;

        } catch (Exception e) {
            System.out.println("Error adding lease: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getAllLeases() {
        ArrayList<String> leases = new ArrayList<>();
        String sql = "SELECT * FROM leases ORDER BY lease_id";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                leases.add(
                        rs.getInt("lease_id") + " | "
                        + rs.getInt("tenant_id") + " | "
                        + rs.getInt("property_id") + " | "
                        + rs.getDate("start_date").toLocalDate() + " | "
                        + rs.getDate("end_date").toLocalDate() + " | "
                        + rs.getDouble("monthly_rent_amount") + " | "
                        + rs.getDouble("security_deposit") + " | "
                        + rs.getInt("payment_due_day") + " | "
                        + rs.getInt("grace_period") + " | "
                        + rs.getDouble("late_penalty_rate") + " | "
                        + rs.getString("status")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching leases: " + e.getMessage());
        }

        return leases;
    }

    public Lease getLeaseById(int leaseId) {
        String sql = "SELECT * FROM leases WHERE lease_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, leaseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Lease(
                        rs.getInt("lease_id"),
                        rs.getInt("tenant_id"),
                        rs.getInt("property_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("monthly_rent_amount"),
                        rs.getDouble("security_deposit"),
                        rs.getInt("payment_due_day"),
                        rs.getInt("grace_period"),
                        rs.getDouble("late_penalty_rate"),
                        rs.getString("status")
                );
            }

        } catch (Exception e) {
            System.out.println("Error getting lease: " + e.getMessage());
        }

        return null;
    }

    public boolean updateLeaseStatus(int leaseId, String status) {
        String sql = "UPDATE leases SET status=? WHERE lease_id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, leaseId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating lease status: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLeaseRent(int id, double value) {
        return updateField("monthly_rent_amount", value, id);
    }

    public boolean updateLeaseDeposit(int id, double value) {
        return updateField("security_deposit", value, id);
    }

    public boolean updateLeaseDueDay(int id, int value) {
        return updateField("payment_due_day", value, id);
    }

    public boolean updateLeaseGrace(int id, int value) {
        return updateField("grace_period", value, id);
    }

    public boolean updateLeasePenaltyRate(int id, double value) {
        return updateField("late_penalty_rate", value, id);
    }

    public boolean updateLeaseEndDate(int id, LocalDate date) {
        String sql = "UPDATE leases SET end_date=? WHERE lease_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date));
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean updateField(String column, Object value, int id) {
        String sql = "UPDATE leases SET " + column + "=? WHERE lease_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, value);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteLease(int leaseId) {
        Lease lease = getLeaseById(leaseId);
        if (lease == null) {
            System.out.println("Lease not found.");
            return false;
        }

        String checkSql = "SELECT COUNT(*) FROM payments WHERE lease_id = ?";
        String deleteSql = "DELETE FROM leases WHERE lease_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            // 🔹 Step 1: Check if payments exist for this lease
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, leaseId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cannot delete lease: payments are attached to this lease.");
                return false;
            }

            // 🔹 Step 2: Delete lease if safe
            PreparedStatement ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, leaseId);

            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                propertyDAO.updateAvailability(lease.getPropertyId(), "Available");
            }

            return deleted;

        } catch (Exception e) {
            System.out.println("Error deleting lease: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getLeasesByTenantId(int tenantId) {
        ArrayList<String> leases = new ArrayList<>();
        String sql = "SELECT * FROM leases WHERE tenant_id = ? ORDER BY start_date DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tenantId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                leases.add(
                        rs.getInt("lease_id") + " | "
                        + rs.getInt("tenant_id") + " | "
                        + rs.getInt("property_id") + " | "
                        + rs.getDate("start_date").toLocalDate() + " | "
                        + rs.getDate("end_date").toLocalDate() + " | "
                        + rs.getDouble("monthly_rent_amount") + " | "
                        + rs.getDouble("security_deposit") + " | "
                        + rs.getInt("payment_due_day") + " | "
                        + rs.getInt("grace_period") + " | "
                        + rs.getDouble("late_penalty_rate") + " | "
                        + rs.getString("status")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching tenant lease history: " + e.getMessage());
        }

        return leases;
    }

    public boolean terminateLease(int leaseId) {
        Lease lease = getLeaseById(leaseId);

        if (lease == null) {
            System.out.println("Lease not found.");
            return false;
        }

        String sql = "UPDATE leases SET status = ? WHERE lease_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "Terminated");
            ps.setInt(2, leaseId);

            boolean updated = ps.executeUpdate() > 0;

            if (updated) {
                propertyDAO.updateAvailability(lease.getPropertyId(), "Available");
            }

            return updated;

        } catch (Exception e) {
            System.out.println("Error terminating lease: " + e.getMessage());
            return false;
        }
    }
}
