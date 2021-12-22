package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "mechanic")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name",nullable = false)
    @NotEmpty(message = "first name cannot be empty")
    @Length(min = 3, max = 15)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    @NotEmpty(message = "last name cannot be empty")
    @Length(min = 3, max = 15)
    private String lastName;

    @Column(name = "email",nullable = false, unique = true)
    @Email(regexp = "^(.+)@(\\S+)$")
    @NotEmpty(message = "email cannot be empty")
    @Length(min = 15, max = 30)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    private Availability availability;

    @Column(name = "location")
    private String location;

    @Column(name = "phone_number", unique = true)
    @Length(min = 10,max = 10)
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "username")
    @Transient
    private String username;

    @Column(name = "password")
    @Transient
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus applicationStatus;

    @Column(name = "profileImage")
    private String profileImage;

    @Column(name = "qualification")
    private String qualification;

    @OneToMany(mappedBy = "mechanic",
    cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    @JoinTable(
            name = "mechanic_tasks",
            joinColumns = @JoinColumn(name = "mechanic_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    private AppUser appUser;

    public Mechanic(String firstName,
                    String lastName,
                    String email,
                    Availability availability,
                    String location,
                    String phoneNumber,
                    ApplicationStatus applicationStatus,
                    LocalDate localDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.availability = availability;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.applicationStatus = applicationStatus;
        this.createdAt =localDate;
    }

    @Transient
    public String getProfileImagePath(){

        return "/mechanic-images/"+id+"/"+this.profileImage;
    }

    @Transient
    public String getQualificationDocumentPath(){
        return "/mechanic-qualifications/"+id+"/"+this.qualification;
    }

    public void addTask(Task task){
        for (Task theTask : this.tasks) {
            if (theTask.getId().equals(task.getId())){
                throw new RuntimeException("The Task already exists");
            }else {
                task.getMechanics().add(this);
                this.tasks.add(task);
            }
        }

    }

    public void addAppointment(Appointment appointment){
        for (Appointment appointment1 : this.appointments) {
            if (appointment1.getId().equals(appointment.getId())){
                throw new RuntimeException("The appointment is invalid");
            }else {
                appointment.setMechanic(this);
                this.appointments.add(appointment);
            }
        }
    }

//    public String getApplicationStatus() {
//        return String.valueOf(applicationStatus);
//    }
//
//    public String getAvailability() {
//        return String.valueOf(availability);
//    }

    public String getStringApplicationStatus(){
        return String.valueOf(applicationStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mechanic mechanic = (Mechanic) o;
        return Objects.equals(email, mechanic.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", availability=" + availability +
                ", location='" + location + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
