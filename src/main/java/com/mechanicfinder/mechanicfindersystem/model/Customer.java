package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "first name cannot be empty")
    @Length(min = 3, max = 15)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "last name cannot be empty")
    @Length(min = 3, max = 15)
    private String lastName;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "email cannot be empty")
    @Email(regexp = "^(.+)@(\\S+)$")
    @Length(min = 15, max = 30)
    private String email;

    @Column(name = "phone_number", unique = true)
    @NotEmpty(message = "phone number cannot be empty")
    @Length(min = 10,max = 10)
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
