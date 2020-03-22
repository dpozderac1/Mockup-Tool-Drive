package com.example.online_testing.Models;


import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Browser")
public class Browser {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Name")
    //@NotEmpty(message = "Browser name cannot be null or empty")
    @NotNull
    @Size(min = 5, max = 20, message = "Browser name must be between 5 and 20 characters")
    private String name;

    @Column(name = "Version")
    //@NotEmpty(message = "Version cannot be null or empty")
    @NotNull
    @Size(min = 4, max = 15, message = "Version must be between 4 and 15 characters")
    private String version;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "Server_ID")
    private Server serverID;

    @Transient
    private int idServer;

    public Browser() {}

    public Browser(String name, Server server_ID, String version) {
        this.name = name;
        this.serverID = server_ID;
        this.version = version;
    }

    public Browser(String name, int idServer, String version) {
        this.name = name;
        this.idServer = idServer;
        this.version = version;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Server getServerID() {
        return serverID;
    }

    public void setServerID(Server serverID) {
        this.serverID = serverID;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }
}
