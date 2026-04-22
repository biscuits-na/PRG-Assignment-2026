package rentalmanagementsystem;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int leaseId;
    private double amountPaid;
    private LocalDate paymentDate;
    private String paymentStatus;
    private double penaltyAmount;
    private double outstandingBalance;

    public Payment() {
    }

    public Payment(int paymentId, int leaseId, double amountPaid, LocalDate paymentDate,
                   String paymentStatus, double penaltyAmount, double outstandingBalance) {
        this.paymentId = paymentId;
        this.leaseId = leaseId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.penaltyAmount = penaltyAmount;
        this.outstandingBalance = outstandingBalance;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }
}