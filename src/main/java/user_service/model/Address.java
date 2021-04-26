package user_service.model;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "address_1", nullable = false)
    public String address1;

    @Column(name = "address_2", nullable = false)
    public String address2;

    @Column(nullable = false)
    public String city;

    @Column(nullable = false)
    public String state;

    @Column(nullable = false)
    public String zip;

    @Column(nullable = false)
    public String country;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    protected Address() {}

    public Address(
        String address1,
        String address2,
        String city,
        String state,
        String zip,
        String country
    ) {
        super();

        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Long getUserId()
    {
        return this.user.id;
    }

}
