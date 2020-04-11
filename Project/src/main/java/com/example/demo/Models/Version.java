package com.example.demo.Models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Version")
public class Version {

    public Version() {
    }

    public Version(Project projectId, @NotNull VersionNames version_name) {
        this.projectId = projectId;
        this.versionName = version_name;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public VersionNames getVersion_name() {
        return versionName;
    }

    public void setVersion_name(VersionNames version_name) {
        this.versionName = version_name;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "Project_id")
    private Project projectId;

    @Column(name = "Version_name", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private VersionNames versionName;

}
