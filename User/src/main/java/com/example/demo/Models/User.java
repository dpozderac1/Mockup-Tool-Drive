package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Entity
@Table(name="User")
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name="Role_id")
    private Role role_id;

    @Column(name="Name")
    @NotEmpty(message = "Name cannot be empty!")
    @Size(min=3,max=255,message = "Name must be between 3 and 255 characters long!")
    private String name;

    @Column(name="Surname")
    @NotEmpty(message = "Surname cannot be empty!")
    @Size(min=3,max=255,message = "Surname must be between 3 and 255 characters long!")
    private String surname;


    @Column(name="Username",unique = true)
    @NotEmpty(message = "Username cannot be empty!")
    @Size(min=5,max=50,message = "Username must be between 5 and 50 characters long!")
    private String username;



    @Documented
    @Constraint(validatedBy = PasswordConstraintValidator.class)
    @Target({ FIELD, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    public @interface ValidPassword {

        String message() default "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 number and 1 symbol!";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Column(name="Password")
    @NotEmpty(message = "Password cannot be empty!")
    @Size(min=8,message = "Password must be at least 8 characters long!")
    @ValidPassword
    private String password;

    @Column(name="Email",unique = true)
    @NotEmpty(message = "Email cannot be empty!")
    @Email
    private String email;


    public User(Role role_id, String name, String surname, String username, String password, String email) {
        this.role_id = role_id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="User_Project",
            joinColumns = {@JoinColumn(name="User_ID",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="Project_ID",referencedColumnName = "id")}
    )
    //@JsonManagedReference
    private List<Project> projects=new ArrayList<Project>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole_id() {
        return role_id;
    }

    public void setRole_id(Role role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
