package com.example.final_project.database.entities.user;

import java.sql.Date;

public class UserBuilder {
    //nec fields
    private String login;
    private String password;
    private String name;
    private Role role;
    private String email;

    private int age;

    private Date registrationDate;
    private Blocked_State blocked_state;
    private String surname;
    private String phone;
    private byte[] photo;

    public UserBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public UserBuilder setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public UserBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public UserBuilder setBlocked_state(Blocked_State blocked_state) {
        this.blocked_state = blocked_state;
        return this;
    }

    public User getUser(){
        return new User(login,password,name,role,email,age,registrationDate,surname,phone,photo,blocked_state );
    }
}
