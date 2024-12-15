package org.example.domainObjects;

public enum Roles {
    ADMIN(1,"Admin"),
    USER(2,"User"),
    MANAGER(3,"Manager");

    private final long id;
    private final String name;

    // Constructor to set the ID for each role
    Roles(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // Getter method for the ID
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Optionally, you can add a static method to retrieve an enum by its integer value
    public static Roles fromValue(long id) {
        for (Roles role : Roles.values()) {
            if (role.getId() == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + id);
    }

    // Optionally, you can add a static method to retrieve an enum by its name
    public static Roles fromName(String name) {
        for (Roles role : Roles.values()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unexpected name: " + name);
    }
}
