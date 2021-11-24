package com.mechanicfinder.mechanicfindersystem.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "service_name", unique = true)
    @NotEmpty(message = "Task name cannot be empty")
    private String taskName;

    @Column(name = "description")
    @NotEmpty(message = "Task description cannot be empty")
    private String description;

    @Column(name = "cost")
    private BigDecimal hourlyPaymentRate;

    @Column(name = "task_duration")
    private Long duration;


    @ManyToMany(fetch = FetchType.EAGER,
    cascade ={
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },mappedBy = "tasks")
    private List<Mechanic> mechanics;

    public Task(String taskName, String description, BigDecimal hourlyPaymentRate) {
        this.taskName = taskName;
        this.description = description;
        this.hourlyPaymentRate = hourlyPaymentRate;
    }

    public void addMechanic(Mechanic mechanic){
        if (mechanic != null){
            this.mechanics.add(mechanic);
            mechanic.getTasks().add(this);
        }else {
            throw  new RuntimeException("Mechanic can not be empty");
        }
    }

    public BigDecimal getServiceTotalCost(){
        long hourlyPaymentRate = this.hourlyPaymentRate.longValue();
        long totalCharge = hourlyPaymentRate * this.duration;
        return BigDecimal.valueOf(totalCharge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", hourlyPaymentRate=" + hourlyPaymentRate +
                ", duration=" + duration +
                '}';
    }
}
