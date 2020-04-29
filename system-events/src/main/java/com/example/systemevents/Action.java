package com.example.systemevents;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name="Action")
public class Action {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name="Microservice")
    private String microservice;

    /*@Column(name="User")
    private int user;*/

    @Column(name="Type")
    private String type;

    @Column(name="Resource")
    private String resource;

    @Column(name="ResponseType")
    private String responseType;

    @Column(name="Timestamp")
    private Timestamp timestamp;

    public Action(String microservice, String type, String resource, String responseType, Timestamp timestamp) {
        this.microservice=microservice;
        this.type=type;
        this.resource=resource;
        this.responseType=responseType;
        this.timestamp=timestamp;
    }

    public Action(){
        
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getMicroservice() {
        return microservice;
    }

    public void setMicroservice(String microservice) {
        this.microservice = microservice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
