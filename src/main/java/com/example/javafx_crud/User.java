package com.example.javafx_crud;

public class User {
    public int id;
    public String name;
    public String mobile;
    public String course;

    public User(int i, String n, String m, String c){
        this.name = n;
        this.mobile = m;
        this.course = c;
        this.id = i;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public String getMobile() {
        return mobile;
    }
}
