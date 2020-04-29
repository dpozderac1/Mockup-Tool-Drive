package com.example.systemevents;

import com.sun.istack.NotNull;

import javax.persistence.*;
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
}
