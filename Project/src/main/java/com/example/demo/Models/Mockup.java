package com.example.demo.Models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "Mockup")
public class Mockup {

    public Mockup() {
    }

    public Mockup(Version versionId, String name, Blob file, Date date_created, Date date_modified, Date accessed_date) {
        this.versionId = versionId;
        this.name = name;
        this.file = file;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.accessed_date = accessed_date;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public Date getAccessed_date() {
        return accessed_date;
    }

    public void setAccessed_date(Date accessed_date) {
        this.accessed_date = accessed_date;
    }

    public Version getVersionId() {
        return versionId;
    }

    public void setVersionId(Version versionId) {
        this.versionId = versionId;
    }

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "Version_id")
    private Version versionId;

    @Column(name = "Name")
    @NotEmpty(message = "Document name should not be empty!")
    @Size(min = 1, max = 255, message = "Number of characters should be less than 255")
    private String name;

    @Column(name = "File")
    @NotNull
    private Blob file;

    @Column(name = "Date_created")
    @PastOrPresent(message = "The date should be in the past or present date!")
    private Date date_created;

    @Column(name = "Date_modified")
    @PastOrPresent(message = "The date should be in the past or present date!")
    private Date date_modified;

    @Column(name = "Accessed_date")
    @PastOrPresent(message = "The date should be in the past or present date!")
    private Date accessed_date;

}
