package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Version")
public class Version {

    public Version() {
    }

    public Version(Project projectId, @NotNull VersionNames version_name) {
        this.projectId = projectId;
        this.version_name = version_name;
    }

    public Long getId() {
        return ID;
    }

    public void setId(Long id) {
        ID = id;
    }

    public VersionNames getVersion_name() {
        return version_name;
    }

    public void setVersion_name(VersionNames version_name) {
        this.version_name = version_name;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    @javax.persistence.Id
    @GeneratedValue
    private Long ID;

    @ManyToOne()
    @JoinColumn (name = "Project_id")
    private Project projectId;

    @Column(name = "Version_name", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private VersionNames version_name;

}
