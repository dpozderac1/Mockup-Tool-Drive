package com.example.online_testing;


import javax.persistence.*;

@Entity
@Table(name = "Browser")
public class Browser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Name")
    private String name;

    @ManyToOne()
    @JoinColumn (name = "Server_ID")
    private Server server_ID;

    public Browser() {}

    public Browser(String name, Server server_ID) {
        this.name = name;
        this.server_ID = server_ID;
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

    public Server getServer_ID() {
        return server_ID;
    }

    public void setServer_ID(Server server_ID) {
        this.server_ID = server_ID;
    }
}
