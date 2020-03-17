package com.example.online_testing;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "Online_test")
public class Online_test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Tests")
    private String tests;

    @Column(name = "Test_results")
    private Blob test_results;

    @ManyToOne()
    @JoinColumn(name = "Server_ID")
    private Server server_ID;

    @ManyToOne()
    @JoinColumn(name = "User_ID")
    private User user_ID;

    @OneToOne()
    @JoinColumn(name = "GSPEC_Document_ID")
    private GSPEC_Document gspec_document_ID;

    public Online_test() {}

    public Online_test(String tests, Blob test_results, Server server_ID, User user_ID, GSPEC_Document gspec_document_ID) {
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

    public GSPEC_Document getGspec_document_ID() {
        return gspec_document_ID;
    }

    public void setGspec_document_ID(GSPEC_Document gspec_document_ID) {
        this.gspec_document_ID = gspec_document_ID;
    }
}

