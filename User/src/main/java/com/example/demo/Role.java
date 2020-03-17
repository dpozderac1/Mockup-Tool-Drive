package com.example.demo;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name="Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Role_name",length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleNames roleName;

    public Role(RoleNames role_name) {
        this.roleName = role_name;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleNames getRole_name() {
        return roleName;
    }

    public void setRole_name(RoleNames role_name) {
        this.roleName = role_name;
    }
}
