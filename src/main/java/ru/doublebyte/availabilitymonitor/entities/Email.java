package ru.doublebyte.availabilitymonitor.entities;

public class Email {

    private Long id;
    private String address;

    ///////////////////////////////////////////////////////////////////////////

    public Email() {

    }

    public Email(String address) {
        this.address = address;
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
