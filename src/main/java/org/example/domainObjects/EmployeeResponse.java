package org.example.domainObjects;

public class EmployeeResponse {
    private Long id;
    private String name;
    private Long roleId;

    public EmployeeResponse(Long id, String name, Long roleId) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
