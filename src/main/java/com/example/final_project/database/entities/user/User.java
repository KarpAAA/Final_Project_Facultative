package com.example.final_project.database.entities.user;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

public class User {
    //nec fields
    private final String login;
    private final String password;
    private final String name;
    private final Role role;
    private final String email;


    private int age;
    private final Date registrationDate;
    private final String surname;
    private final String phone;
    private byte[] photo;
    private Blocked_State blocked_state;
    private final int balance;


    public User(String login, String password, String name, Role role, String email,
                int age, Date registrationDate, String surname, String phone, byte[] photo, Blocked_State blocked_state, int balance) {

        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.email = email;
        this.age = age;
        this.registrationDate = registrationDate;
        this.blocked_state = blocked_state;
        this.surname = surname;
        this.phone = phone;
        this.balance = balance;
        if (photo != null) {
            this.photo = photo;

        } else {
            try {
                this.photo =
                        (new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\img\\defaultUserPhoto.jpg"))
                                .readAllBytes();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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

    public String getPassword() {
        return password;
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
}
