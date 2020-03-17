package com.example.online_testing;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne()
    @JoinColumn (name = "Role_ID")
    private Role role_ID;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email")
    private String email;

    public User() {}

    public User(Role role_ID, String username, String password, String email) {
        this.role_ID = role_ID;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Role getRole_ID() {
        return role_ID;
    }

    public void setRole_ID(Role role_ID) {
        this.role_ID = role_ID;
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
