package rentalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TenantDAO {

    public boolean addTenant(Tenant tenant) {
        String sql = "INSERT INTO tenants(first_name, last_name, id_passport, phone_number, email_address, current_status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenant.getFirstName());
            ps.setString(2, tenant.getLastName());
            ps.setString(3, tenant.getIdPassport());
            ps.setString(4, tenant.getPhoneNumber());
            ps.setString(5, tenant.getEmailAddress());
            ps.setString(6, tenant.getCurrentStatus());

            return ps.executeUpdate() > 0;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Duplicate tenant record: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error adding tenant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTenantFirstName(int id, String firstName) {
    String sql = "UPDATE tenants SET first_name = ? WHERE tenant_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, firstName);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error updating first name: " + e.getMessage());
        return false;
    }
}

public boolean updateTenantLastName(int id, String lastName) {
    String sql = "UPDATE tenants SET last_name = ? WHERE tenant_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, lastName);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error updating last name: " + e.getMessage());
        return false;
    }
}

public boolean updateTenantPhone(int id, String phone) {
    String sql = "UPDATE tenants SET phone_number = ? WHERE tenant_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, phone);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error updating phone: " + e.getMessage());
        return false;
    }
}

public boolean updateTenantEmail(int id, String email) {
    String sql = "UPDATE tenants SET email_address = ? WHERE tenant_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, email);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error updating email: " + e.getMessage());
        return false;
    }
}

public boolean updateTenantStatus(int id, String status) {
    String sql = "UPDATE tenants SET current_status = ? WHERE tenant_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, status);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Error updating status: " + e.getMessage());
        return false;
    }
}
    
    public ArrayList<String> getAllTenants() {
        ArrayList<String> tenants = new ArrayList<>();
        String sql = "SELECT * FROM tenants";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tenants.add(
                        rs.getInt("tenant_id") + " | "
                        + rs.getString("first_name") + " "
                        + rs.getString("last_name") + " | "
                        + rs.getString("phone_number") + " | "
                        + rs.getString("email_address") + " | "
                        + rs.getString("current_status")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching tenants: " + e.getMessage());
        }

        return tenants;
    }

    public boolean updateTenant(int tenantId, String firstName, String lastName,
            String phoneNumber, String emailAddress, String currentStatus) {
        String sql = "UPDATE tenants SET first_name=?, last_name=?, phone_number=?, email_address=?, current_status=? WHERE tenant_id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phoneNumber);
            ps.setString(4, emailAddress);
            ps.setString(5, currentStatus);
            ps.setInt(6, tenantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating tenant: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTenant(int tenantId) {
        String sql = "DELETE FROM tenants WHERE tenant_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tenantId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error deleting tenant: " + e.getMessage());
            return false;
        }
    }
}
