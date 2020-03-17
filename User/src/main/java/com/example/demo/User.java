package com.example.demo;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn (name="Role_id")
    private Role role_id;

    @Column(name="Name")
    private String name;

    @Column(name="Surname")
    private String surname;

    @Column(name="Username")
    private String username;

    @Column(name="Password")
    private String password;

    @Column(name="Email")
    private String email;


    public User(Role role_id, String name, String surname, String username, String password, String email) {
        this.role_id = role_id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="User_Project",
            joinColumns = {@JoinColumn(name="User_ID",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="Project_ID",referencedColumnName = "id")}
    )
    private List<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole_id() {
        return role_id;
    }

    public void setRole_id(Role role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
