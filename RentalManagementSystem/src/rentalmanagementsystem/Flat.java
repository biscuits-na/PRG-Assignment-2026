package rentalmanagementsystem;

public class Flat extends Property {

    private String unitNumber;
    private int floorLevel;
    private boolean elevatorAccess;
    private boolean backyard;

    public Flat() {
    }

    public Flat(int propertyId, double buildingSize, String address, String town, String location,
            double marketValue, double monthlyRentalCost, String availabilityStatus,
            String unitNumber, int floorLevel, boolean elevatorAccess, boolean backyard) {

        super(propertyId, buildingSize, address, town, location, marketValue, monthlyRentalCost, availabilityStatus);

        this.unitNumber = unitNumber;
        this.floorLevel = floorLevel;
        this.elevatorAccess = elevatorAccess;
        this.backyard = backyard;
    }

    @Override
    public String getPropertyType() {
        return "Flat";
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public int getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(int floorLevel) {
        this.floorLevel = floorLevel;
    }

    public boolean isElevatorAccess() {
        return elevatorAccess;
    }

    public void setElevatorAccess(boolean elevatorAccess) {
        this.elevatorAccess = elevatorAccess;
    }

    public boolean isBackyard() {
        return backyard;
    }

    public void setBackyard(boolean backyard) {
        this.backyard = backyard;
    }
}
