package model;
public class Address {
    private int addressId;
    private String customerEmail;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public Address() {
    }

    public Address(int addressId, String customerEmail, String street, String city, String state, String country, String zipCode) {
        this.addressId = addressId;
        this.customerEmail = customerEmail;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    // Setters
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setCountry(String country) { this.country = country; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    // Getters
    public int getAddressId() { return addressId; }
    public String getCustomerEmail() { return customerEmail; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public String getZipCode() { return zipCode; }
}