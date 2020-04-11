package com.example.online_testing.Models;

import com.example.online_testing.Models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Entity
@Table(name = "User")
public class User {


    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne()
    @JoinColumn (name = "Role_ID")
    private Role roleID;

    @Column(name = "Username", unique = true)
    @NotEmpty(message = "Username cannot be null or empty")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;


    @Column(name = "Email")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @Transient
    private int idRole;

    public User() {}

    public User(Role role_ID, String username, String email) {
        this.roleID = role_ID;
        this.username = username;
        this.email = email;
    }

    public User(String username, String email,int idRole) {
        this.idRole = idRole;
        this.username = username;
        this.email = email;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Role getRoleID() {
        return roleID;
    }

    public void setRoleID(Role roleID) {
        this.roleID = roleID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
}
