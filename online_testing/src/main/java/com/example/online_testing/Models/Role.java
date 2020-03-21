package com.example.online_testing.Models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Role_name")
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleNames roleName;

    public Role() {}

    public Role(RoleNames role_name) {
        this.roleName = role_name;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public RoleNames getRole_name() {
        return roleName;
    }

    public void setRole_name(RoleNames role_name) {
        this.roleName = role_name;
    }
}
