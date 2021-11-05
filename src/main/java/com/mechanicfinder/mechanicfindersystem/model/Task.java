package com.mechanicfinder.mechanicfindersystem.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne(fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    private Mechanic mechanic;
}
