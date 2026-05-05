package rentalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;

public class PaymentDAO {

    private final LeaseDAO leaseDAO = new LeaseDAO();

    public boolean addPaymentAuto(Payment payment) {
        Lease lease = leaseDAO.getLeaseById(payment.getLeaseId());

        if (lease == null) {
            System.out.println("Lease not found.");
            return false;
        }

        LocalDate paymentDate = payment.getPaymentDate();
        LocalDate dueDate = LocalDate.of(paymentDate.getYear(), paymentDate.getMonth(), lease.getPaymentDueDay());
        LocalDate lastGraceDate = dueDate.plusDays(lease.getGracePeriod());

        double penalty = 0;

        if (paymentDate.isAfter(lastGraceDate)) {
            penalty = lease.getMonthlyRentAmount() * lease.getLatePenaltyRate();
        }

        double totalDue = lease.getMonthlyRentAmount() + penalty;
        double outstanding = totalDue - payment.getAmountPaid();

        if (outstanding < 0) {
            outstanding = 0;
        }

        String paymentStatus;

        if (outstanding > 0) {
            paymentStatus = "Pending";
        } else if (paymentDate.isAfter(lastGraceDate)) {
            paymentStatus = "Late";
        } else {
            paymentStatus = "Paid";
        }

        String sql = "INSERT INTO payments(lease_id, amount_paid, payment_date, payment_status, penalty_amount, outstanding_balance) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getLeaseId());
            ps.setDouble(2, payment.getAmountPaid());
            ps.setDate(3, java.sql.Date.valueOf(payment.getPaymentDate()));
            ps.setString(4, paymentStatus);
            ps.setDouble(5, penalty);
            ps.setDouble(6, outstanding);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error adding payment: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getAllPayments() {
        ArrayList<String> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_id";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(
                        rs.getInt("payment_id") + " | "
                        + rs.getInt("lease_id") + " | "
                        + rs.getDouble("amount_paid") + " | "
                        + rs.getDate("payment_date").toLocalDate() + " | "
                        + rs.getString("payment_status") + " | "
                        + rs.getDouble("penalty_amount") + " | "
                        + rs.getDouble("outstanding_balance")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching payments: " + e.getMessage());
        }

        return payments;
    }

    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("lease_id"),
                        rs.getDouble("amount_paid"),
                        rs.getDate("payment_date").toLocalDate(),
                        rs.getString("payment_status"),
                        rs.getDouble("penalty_amount"),
                        rs.getDouble("outstanding_balance")
                );
            }

        } catch (Exception e) {
            System.out.println("Error getting payment: " + e.getMessage());
        }

        return null;
    }

    public String getTenantFinancialStatus(int tenantId) {
        String sql = "SELECT COUNT(*) "
                + "FROM payments p "
                + "JOIN leases l ON p.lease_id = l.lease_id "
                + "WHERE l.tenant_id = ? AND p.outstanding_balance > 0";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tenantId);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return "Owing";
            }

        } catch (Exception e) {
            System.out.println("Error getting tenant financial status: " + e.getMessage());
        }

        return "Clear";
    }

    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }
}
