package rentalmanagementsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Lease {
    private int leaseId;
    private int tenantId;
    private int propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double monthlyRentAmount;
    private double securityDeposit;
    private int paymentDueDay;
    private int gracePeriod;
    private double latePenaltyRate;
    private String status;

    public Lease() {
    }

    public Lease(int leaseId, int tenantId, int propertyId, LocalDate startDate, LocalDate endDate,
                 double monthlyRentAmount, double securityDeposit, int paymentDueDay,
                 int gracePeriod, double latePenaltyRate, String status) {
        this.leaseId = leaseId;
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRentAmount = monthlyRentAmount;
        this.securityDeposit = securityDeposit;
        this.paymentDueDay = paymentDueDay;
        this.gracePeriod = gracePeriod;
        this.latePenaltyRate = latePenaltyRate;
        this.status = status;
    }

    public long calculateLeaseDurationInMonths() {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }

    public double calculateLatePenalty(int daysLate) {
        if (daysLate <= gracePeriod) {
            return 0;
        }
        return monthlyRentAmount * latePenaltyRate;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getMonthlyRentAmount() {
        return monthlyRentAmount;
    }

    public void setMonthlyRentAmount(double monthlyRentAmount) {
        this.monthlyRentAmount = monthlyRentAmount;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public int getPaymentDueDay() {
        return paymentDueDay;
    }

    public void setPaymentDueDay(int paymentDueDay) {
        this.paymentDueDay = paymentDueDay;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public double getLatePenaltyRate() {
        return latePenaltyRate;
    }

    public void setLatePenaltyRate(double latePenaltyRate) {
        this.latePenaltyRate = latePenaltyRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}