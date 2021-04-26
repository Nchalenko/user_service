package user_service.model;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    public String firstName;

    @Column(nullable = false)
    public String lastName;

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    protected User() {
    }

    public User(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        super();

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + this.firstName + '\'' +
                ", lastName=" + this.lastName +
                ", email=" + this.email +
                '}';


    }
}
