package com.mechanicfinder.mechanicfindersystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppUser> appUser;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
