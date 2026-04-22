package rentalmanagementsystem;

public abstract class Property {

    private int propertyId;
    private double buildingSize;
    private String address;
    private String town;
    private String location;
    private double marketValue;
    private double monthlyRentalCost;
    private String availabilityStatus;

    public Property() {
    }

    public Property(int propertyId, double buildingSize, String address, String town, String location,
            double marketValue, double monthlyRentalCost, String availabilityStatus) {
        this.propertyId = propertyId;
        this.buildingSize = buildingSize;
        this.address = address;
        this.location = location;
        this.marketValue = marketValue;
        this.monthlyRentalCost = monthlyRentalCost;
        this.availabilityStatus = availabilityStatus;
    }

    public abstract String getPropertyType();

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public double getBuildingSize() {
        return buildingSize;
    }

    public void setBuildingSize(double buildingSize) {
        this.buildingSize = buildingSize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public double getMonthlyRentalCost() {
        return monthlyRentalCost;
    }

    public void setMonthlyRentalCost(double monthlyRentalCost) {
        this.monthlyRentalCost = monthlyRentalCost;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return "ID: " + propertyId + ", Type: " + getPropertyType()
                + ", Address: " + address + ", Rent: " + monthlyRentalCost
                + ", Status: " + availabilityStatus;
    }
}
