package com.example.online_testing;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "GSPEC_Document")
public class GSPEC_Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Name")
    private String name;

    @Column(name = "File")
    private Blob file;

    public GSPEC_Document() {}

    public GSPEC_Document(String name, Blob file) {
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
