package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
@Entity
@Table(name = "service")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

//    @ManyToOne(fetch = FetchType.EAGER,
//    cascade = CascadeType.ALL)
//    private Mechanic mechanic;

    public Task(String taskName, String description, BigDecimal cost, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
    }
}
