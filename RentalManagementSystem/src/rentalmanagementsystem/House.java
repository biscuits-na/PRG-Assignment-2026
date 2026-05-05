package rentalmanagementsystem;

public class House extends Property {
    private double plotSize;

    public House() {
    }

    public House(int propertyId, double buildingSize, String address, String town, String location,
                 double marketValue, double monthlyRentalCost, String availabilityStatus,
                 double plotSize) {
        super(propertyId, buildingSize, address, town, location, marketValue, monthlyRentalCost, availabilityStatus);
        this.plotSize = plotSize;
    }

    @Override
    public String getPropertyType() {
        return "House";
    }

    public double getPlotSize() {
        return plotSize;
    }

    public void setPlotSize(double plotSize) {
        this.plotSize = plotSize;
    }
}