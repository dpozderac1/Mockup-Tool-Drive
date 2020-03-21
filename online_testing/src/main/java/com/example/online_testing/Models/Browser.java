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
    @NotEmpty(message = "Browser name cannot be null or empty")
    @Size(min = 5, max = 20, message = "Browser name must be between 5 and 20 characters")
    private String name;

    @Column(name = "Version")
    @NotEmpty(message = "Version cannot be null or empty")
    @Size(min = 4, max = 15, message = "Version must be between 4 and 15 characters")
    private String version;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "Server_ID")
    private Server server_ID;

    public Browser() {}

    public Browser(String name, Server server_ID, String version) {
        this.name = name;
        this.server_ID = server_ID;
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

    public Server getServer_ID() {
        return server_ID;
    }

    public void setServer_ID(Server server_ID) {
        this.server_ID = server_ID;
    }
}
