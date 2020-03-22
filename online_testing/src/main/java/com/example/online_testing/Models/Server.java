package com.example.online_testing.Models;

import com.example.online_testing.Repositories.RoleRepository;
import com.example.online_testing.Repositories.UserRepository;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Server")
public class Server {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "URL")
    //@NotEmpty(message = "URL cannot be null or empty")
    @NotNull
    @URL
    private String url;

    @Column(name = "Port")
    @NotNull
    private int port;

    @Column(name = "Status")
    @NotNull
    private String status;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "User_ID")
    private User userID;

    public Server() {}

    public Server(String url, int port, String status, User user_ID) {
        this.url = url;
        this.port = port;
        this.status = status;
        this.userID = user_ID;
    }

    public Server(String url, int port, String status) {
        this.url = url;
        this.port = port;
        this.status = status;
    }
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }
}
