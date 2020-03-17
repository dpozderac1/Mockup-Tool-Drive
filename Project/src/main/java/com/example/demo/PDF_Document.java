package com.example.demo;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "PDF_Document")
public class PDF_Document {

    public PDF_Document() {
    }

    public PDF_Document(Mockup mockupId, String name, Blob file, Date date_created, Date date_modified, Date accessed_date) {
        this.mockupId = mockupId;
        this.name = name;
        this.file = file;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.accessed_date = accessed_date;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Mockup getMockupId() {
        return mockupId;
    }

    public void setMockupId(Mockup mockupId) {
        this.mockupId = mockupId;
    }

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;

    @ManyToOne()
    @JoinColumn (name = "Mockup_id")
    private Mockup mockupId;

    @Column(name = "Name")
    private String name;

    @Column(name = "File")
    private Blob file;

    @Column(name = "Date_created")
    private Date date_created;

    @Column(name = "Date_modified")
    private Date date_modified;

    @Column(name = "Accessed_date")
    private Date accessed_date;
}
