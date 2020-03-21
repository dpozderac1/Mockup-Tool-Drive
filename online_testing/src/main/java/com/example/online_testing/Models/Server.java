package com.example.online_testing.Models;

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
    @NotEmpty(message = "URL cannot be null or empty")
    @URL
    private String url;

    @Column(name = "Port")
    @NotNull
    private int port;

    @Column(name = "Status")
    @NotNull
    private int status;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "User_ID")
    private User user_ID;

    public Server() {}

    public Server(String url, int port, int status, User user_ID) {
        this.url = url;
        this.port = port;
        this.status = status;
        this.user_ID = user_ID;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(User user_ID) {
        this.user_ID = user_ID;
    }
}
