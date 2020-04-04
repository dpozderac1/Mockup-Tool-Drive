package com.example.demo.Models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Role")
public class Role {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name="Role_name",length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleNames roleName;

    @OneToMany(mappedBy = "roleID",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<User> users;

    public Role(RoleNames roleName) {
        //this.id = id;
        this.roleName = roleName;
    }

    public Role() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public RoleNames getRole_name() {
        return roleName;
    }

    public void setRole_name(RoleNames role_name) {
        this.roleName = role_name;
    }

    /*public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }*/

}
