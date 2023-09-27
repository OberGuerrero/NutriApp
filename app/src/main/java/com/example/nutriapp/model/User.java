package com.example.nutriapp.model;

public class User {
    private int id;
    private String name;
    private String telephone;
    private String email;
    private String pass;

    // Constructor
    public User(String name, String telephone, String email, String pass) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.pass = pass;
    }

    // Getters y setters (puedes generarlos automáticamente en Android Studio)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

