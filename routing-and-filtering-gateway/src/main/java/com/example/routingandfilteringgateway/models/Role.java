package com.example.routingandfilteringgateway.models;

public class Role {
    private Long ID;
    private RoleNames roleName;

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

}
