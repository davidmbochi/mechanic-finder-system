package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "first_namme")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(name = "location")
    private String location;

    @OneToMany(fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    public Mechanic(String firstName, String lastName, String email, Availability availability, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.availability = availability;
        this.location = location;
    }

    public void addTask(Task task){
        for (Task theTask : this.tasks) {
            if (theTask.getId().equals(task.getId())){
                throw new RuntimeException("The Task already exists");
            }else {
                this.tasks.add(task);
            }
        }

    }
}
