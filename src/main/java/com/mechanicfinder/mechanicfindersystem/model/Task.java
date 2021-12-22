package com.mechanicfinder.mechanicfindersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
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
    @Length(min = 10, max = 50)
    private String taskName;

    @Column(name = "description")
    @NotEmpty(message = "Task description cannot be empty")
    @Length(min = 15, max = 100)
    private String description;

    @Column(name = "cost")
    @Min(value = 200, message = "minimum value is 200")
    @Max(value = 5000, message = "maximum value is 5000")
    @NotNull(message = "hourly payment cannot be empty")
    private BigDecimal hourlyPaymentRate;


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

    @PreRemove
    public void removeTaskFromMechanic(){
        for (Mechanic mechanic : mechanics) {
            mechanic.getTasks().remove(this);
        }
    }

    public void setHourlyPaymentRate(BigDecimal hourlyPaymentRate) {
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
                '}';
    }
}
