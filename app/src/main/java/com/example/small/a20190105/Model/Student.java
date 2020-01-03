package com.example.small.a20190105.Model;

public class Student {
    private String username;
    private String studentID;
    private String email;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Student(String username, String studentID, String email, String password) {
        this.username = username;
        this.studentID = studentID;
        this.email = email;
        this.password = password;
    }
}
