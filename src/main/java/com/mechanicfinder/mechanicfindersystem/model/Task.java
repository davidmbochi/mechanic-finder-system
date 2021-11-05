package com.mechanicfinder.mechanicfindersystem.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "duration")
    private Duration duration;

    @ManyToMany(fetch = FetchType.EAGER,
    cascade ={
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },mappedBy = "tasks")
    private List<Mechanic> mechanics;

    public Task(String taskName, String description, BigDecimal cost, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
    }

    public void addMechanic(Mechanic mechanic){
        if (mechanic != null){
            this.mechanics.add(mechanic);
            mechanic.getTasks().add(this);
        }else {
            throw  new RuntimeException("Mechanic can not be empty");
        }
    }
}
