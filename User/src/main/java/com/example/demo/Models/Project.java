package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="Project")
public class Project {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    public Project() {
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinTable(
            name="User_Project",
            joinColumns = {@JoinColumn(name="Project_ID",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="User_ID",referencedColumnName = "id")}
    )
    @JsonBackReference
    private List<User> users=new ArrayList<User>();

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
