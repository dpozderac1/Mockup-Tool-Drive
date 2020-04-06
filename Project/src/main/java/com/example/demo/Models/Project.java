package com.example.demo.Models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Project")
public class Project {

    public Project() {
    }

    public Project(String name, Date date_created, Date date_modified, Integer priority) {
        this.name = name;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.priority = priority;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Name")
    @NotEmpty(message = "Document name should not be empty!")
    @Size(min = 1, max = 255, message = "Number of characters should be less than 255")
    private String name;

    @Column(name = "Date_created")
    @PastOrPresent(message = "The date should be in the past or present date!")
    private Date date_created;

    @Column(name = "Date_modified")
    @PastOrPresent(message = "The date should be in the past or present date!")
    private Date date_modified;

    @Column(name = "Priority")
    @NotNull
    private Integer priority;

}
