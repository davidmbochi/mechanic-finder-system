package com.mechanicfinder.mechanicfindersystem.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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

    @ManyToMany(fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    @JoinTable(
            name = "mechanic_tasks",
            joinColumns = @JoinColumn(name = "mechanic_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
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
                task.getMechanics().add(this);
            }
        }

    }
}
