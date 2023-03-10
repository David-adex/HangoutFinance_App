package com.example.hangoutfinance_app;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String firstname, age, surname,email;
    public Map<String, Boolean> stars = new HashMap<>();

    public String getFirstname() {
        return firstname;
    }

    public String getAge() {
        return age;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public User(){

    }
    public User(String firstname, String age, String surname, String email){
        this.firstname=firstname;
        this.age=age;
        this.surname=surname;
        this.email=email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstname", firstname);
        result.put("age", age);
        result.put("surname", surname);
        result.put("email", email);
        result.put("stars", stars);

        return result;
    }
}
