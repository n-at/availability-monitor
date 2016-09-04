package ru.doublebyte.availabilitymonitor.entities;

import javax.persistence.*;

@Entity
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address", nullable = false, length = 250)
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

    public String getAddress() {
        return address;
    }
}
