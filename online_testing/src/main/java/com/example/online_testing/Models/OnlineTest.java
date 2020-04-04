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
    @NotEmpty(message = "Online test name cannot be null or empty!")
    @NotNull
    @Size(min = 5, max = 30, message = "Online test name must be between 5 and 30 characters!")
    private String tests;

    @Column(name = "Test_results")
    @NotNull
    private Blob test_results;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Server_ID")
    private Server serverID;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "User_ID")
    private User userID;

    @OneToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GSPEC_Document_ID")
    private GSPECDocument gspecDocumentID;

    @Transient
    private int idServer;

    @Transient
    private int idUser;

    @Transient
    private int idGspecDocument;

    public OnlineTest() {}

    public OnlineTest(String tests, Blob test_results, Server server_ID, User user_ID, GSPECDocument gspec_document_ID) {
        this.tests = tests;
        this.test_results = test_results;
        this.serverID = server_ID;
        this.userID = user_ID;
        this.gspecDocumentID = gspec_document_ID;
    }

    public OnlineTest(String tests, Blob test_results, int idServer, int idUser, int idGspecDocument) {
        this.tests = tests;
        this.test_results = test_results;
        this.idServer = idServer;
        this.idUser = idUser;
        this.idGspecDocument = idGspecDocument;
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

    public Server getServerID() {
        return serverID;
    }

    public void setServerID(Server serverID) {
        this.serverID = serverID;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public GSPECDocument getGspecDocumentID() {
        return gspecDocumentID;
    }

    public void setGspecDocumentID(GSPECDocument gspecDocumentID) {
        this.gspecDocumentID = gspecDocumentID;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGspecDocument() {
        return idGspecDocument;
    }

    public void setIdGspecDocument(int idGspecDocument) {
        this.idGspecDocument = idGspecDocument;
    }
}

