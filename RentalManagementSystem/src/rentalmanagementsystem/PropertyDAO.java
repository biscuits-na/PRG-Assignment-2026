package rentalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PropertyDAO {

    public boolean addProperty(Property property) {
        String sql = "INSERT INTO properties(property_type, building_size, address, town, location, market_value, "
                + "monthly_rental_cost, availability_status, plot_size, unit_number, floor_level, elevator_access, backyard) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, property.getPropertyType());
            ps.setDouble(2, property.getBuildingSize());
            ps.setString(3, property.getAddress());
            ps.setString(4, property.getTown());
            ps.setString(5, property.getLocation());
            ps.setDouble(6, property.getMarketValue());
            ps.setDouble(7, property.getMonthlyRentalCost());
            ps.setString(8, property.getAvailabilityStatus());

            if (property instanceof House) {
                House h = (House) property;
                ps.setDouble(9, h.getPlotSize());
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.INTEGER);
                ps.setBoolean(12, false);
                ps.setBoolean(13, false);

            } else if (property instanceof Townhouse) {
                Townhouse t = (Townhouse) property;
                ps.setNull(9, java.sql.Types.DOUBLE);
                ps.setString(10, t.getUnitNumber());
                ps.setNull(11, java.sql.Types.INTEGER);
                ps.setBoolean(12, false);
                ps.setBoolean(13, t.isBackyard());

            } else if (property instanceof Flat) {
                Flat f = (Flat) property;
                ps.setNull(9, java.sql.Types.DOUBLE);
                ps.setString(10, f.getUnitNumber());
                ps.setInt(11, f.getFloorLevel());
                ps.setBoolean(12, f.isElevatorAccess());
                ps.setBoolean(13, f.isBackyard());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error adding property: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getAllProperties() {
        ArrayList<String> properties = new ArrayList<>();
        String sql = "SELECT * FROM properties ORDER BY property_id";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                properties.add(
                        rs.getInt("property_id") + " | "
                        + rs.getString("property_type") + " | "
                        + rs.getString("address") + " | "
                        + rs.getString("town") + " | "
                        + rs.getString("location") + " | "
                        + rs.getDouble("monthly_rental_cost") + " | "
                        + rs.getString("availability_status")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching properties: " + e.getMessage());
        }

        return properties;
    }

    public boolean updateAvailability(int propertyId, String status) {
        String sql = "UPDATE properties SET availability_status = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property availability: " + e.getMessage());
            return false;
        }
    }

    public boolean isPropertyAvailable(int propertyId) {
        String sql = "SELECT availability_status FROM properties WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, propertyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("availability_status").equalsIgnoreCase("Available");
            }

        } catch (Exception e) {
            System.out.println("Error checking property availability: " + e.getMessage());
        }

        return false;
    }

    public boolean updatePropertyBasic(int propertyId, String address, String location,
            double marketValue, double monthlyRentalCost, String status) {
        String sql = "UPDATE properties SET address = ?, location = ?, market_value = ?, "
                + "monthly_rental_cost = ?, availability_status = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            ps.setString(2, location);
            ps.setDouble(3, marketValue);
            ps.setDouble(4, monthlyRentalCost);
            ps.setString(5, status);
            ps.setInt(6, propertyId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProperty(int propertyId) {
        String sql = "DELETE FROM properties WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error deleting property: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyAddress(int propertyId, String address) {
        String sql = "UPDATE properties SET address = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property address: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyTown(int propertyId, String town) {
        String sql = "UPDATE properties SET town = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, town);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property town: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyLocation(int propertyId, String location) {
        String sql = "UPDATE properties SET location = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property location: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyMarketValue(int propertyId, double marketValue) {
        String sql = "UPDATE properties SET market_value = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, marketValue);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property market value: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyRent(int propertyId, double monthlyRentalCost) {
        String sql = "UPDATE properties SET monthly_rental_cost = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, monthlyRentalCost);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property rent: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePropertyStatus(int propertyId, String status) {
        String sql = "UPDATE properties SET availability_status = ? WHERE property_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, propertyId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating property status: " + e.getMessage());
            return false;
        }
    }
}
