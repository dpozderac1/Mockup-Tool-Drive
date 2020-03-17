package com.example.demo;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Project() {
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="User_Project",
            joinColumns = {@JoinColumn(name="Project_ID",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="User_ID",referencedColumnName = "id")}
    )
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
