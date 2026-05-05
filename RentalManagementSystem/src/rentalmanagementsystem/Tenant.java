package rentalmanagementsystem;

public class Tenant {
    private int tenantId;
    private String firstName;
    private String lastName;
    private String idPassport;
    private String phoneNumber;
    private String emailAddress;
    private String currentStatus;

    public Tenant() {
    }

    public Tenant(int tenantId, String firstName, String lastName, String idPassport,
                  String phoneNumber, String emailAddress, String currentStatus) {
        this.tenantId = tenantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idPassport = idPassport;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.currentStatus = currentStatus;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdPassport() {
        return idPassport;
    }

    public void setIdPassport(String idPassport) {
        this.idPassport = idPassport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        return tenantId + " - " + firstName + " " + lastName + " (" + currentStatus + ")";
    }
}