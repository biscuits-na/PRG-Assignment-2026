package rentalmanagementsystem;

public class Townhouse extends Property {
    private String unitNumber;
    private boolean backyard;

    public Townhouse() {
    }

    public Townhouse(int propertyId, double buildingSize, String address, String town, String location,
                     double marketValue, double monthlyRentalCost, String availabilityStatus,
                     String unitNumber, boolean backyard) {
        super(propertyId, buildingSize, address, town, location, marketValue, monthlyRentalCost, availabilityStatus);
        this.unitNumber = unitNumber;
        this.backyard = backyard;
    }

    @Override
    public String getPropertyType() {
        return "Townhouse";
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public boolean isBackyard() {
        return backyard;
    }

    public void setBackyard(boolean backyard) {
        this.backyard = backyard;
    }
}