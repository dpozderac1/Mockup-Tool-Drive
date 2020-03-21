package com.example.online_testing.Models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Entity
@Table(name = "GSPECDocument")
public class GSPECDocument {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Name")
    @NotEmpty(message = "GSPEC document name cannot be null or empty")
    @Size(min = 5, max = 30, message = "GSPEC document name must be between 5 and 30 characters")
    private String name;

    @Column(name = "File")
    @NotNull
    private Blob file;

    public GSPECDocument() {}

    public GSPECDocument(String name, Blob file) {
        this.name = name;
        this.file = file;
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

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }
}
