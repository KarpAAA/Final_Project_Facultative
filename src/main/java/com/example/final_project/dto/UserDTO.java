package com.example.final_project.dto;


import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;

import java.io.*;
import java.sql.Date;
import java.util.Base64;
import java.util.Objects;

public class UserDTO {
    //nec fields
    private final String login;
    private final String name;
    private final Role role;
    private final String email;


    private int age;
    private final Date registrationDate;
    private final String surname;
    private final String phone;
    private byte[] photo;
    private Blocked_State blocked_state;
    private String base64String;
    private final int balance;

    public UserDTO(String login, String name, Role role, String email,
                   int age, Date registrationDate, String surname, String phone, byte[] photo, Blocked_State blocked_state, int balance) {

        this.login = login;
        this.name = name;
        this.role = role;
        this.email = email;
        this.age = age;
        this.registrationDate = registrationDate;
        this.blocked_state = blocked_state;
        this.surname = surname;
        this.phone = phone;
        this.photo = photo;
        this.balance = balance;


        base64String = Base64.getEncoder().encodeToString(this.photo);
    }

    public void setPhoto(InputStream photo) {

        try {
            this.photo = photo.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setAge(int age) {
        this.age = age;
    }

    public Blocked_State getBlocked_state() {
        return blocked_state;
    }

    public String getLogin() {
        return login;
    }



    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public int getBalance() {
        return balance;
    }

    public String getBase64String() {
        return base64String;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(login, userDTO.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
