package com.example.online_testing.Models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Entity
@Table(name = "OnlineTest")
public class OnlineTest {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Tests")
    @NotEmpty(message = "Online test name cannot be null or empty")
    @Size(min = 5, max = 30, message = "Online test name must be between 5 and 30 characters")
    private String tests;

    @Column(name = "Test_results")
    @NotNull
    private Blob test_results;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Server_ID")
    private Server server_ID;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "User_ID")
    private User user_ID;

    @OneToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GSPEC_Document_ID")
    private GSPECDocument gspec_document_ID;

    public OnlineTest() {}

    public OnlineTest(String tests, Blob test_results, Server server_ID, User user_ID, GSPECDocument gspec_document_ID) {
        this.tests = tests;
        this.test_results = test_results;
        this.server_ID = server_ID;
        this.user_ID = user_ID;
        this.gspec_document_ID = gspec_document_ID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public Blob getTest_results() {
        return test_results;
    }

    public void setTest_results(Blob test_results) {
        this.test_results = test_results;
    }

    public Server getServer_ID() {
        return server_ID;
    }

    public void setServer_ID(Server server_ID) {
        this.server_ID = server_ID;
    }

    public User getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(User user_ID) {
        this.user_ID = user_ID;
    }

    public GSPECDocument getGspec_document_ID() {
        return gspec_document_ID;
    }

    public void setGspec_document_ID(GSPECDocument gspec_document_ID) {
        this.gspec_document_ID = gspec_document_ID;
    }
}

