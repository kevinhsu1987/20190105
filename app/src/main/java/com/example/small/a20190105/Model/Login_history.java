package com.example.small.a20190105.Model;

public class Login_history {
    private String studentID;
    private String datetime;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Login_history(String studentID, String datetime) {
        this.studentID = studentID;
        this.datetime = datetime;
    }
}
