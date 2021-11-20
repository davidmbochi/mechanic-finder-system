package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username")
    @Transient
    private String username;

    @Column(name = "password")
    @Transient
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "customer",
    cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private AppUser appUser;


    public void addAppointment(Appointment appointment){
        for (Appointment appointment1 : this.appointments) {
            if (appointment1.getId().equals(appointment.getId())){
                throw new RuntimeException("The appointment is invalid");
            }else {
                appointment.setCustomer(this);
                this.appointments.add(appointment);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Transient
    public String getCustomerImagePath(){
        return "/customer-images/"+id+"/"+profileImage;
    }

}
