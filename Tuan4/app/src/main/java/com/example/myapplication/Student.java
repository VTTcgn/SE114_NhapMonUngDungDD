package com.example.myapplication;

import java.io.Serializable;

public class Student extends Person implements Serializable {
    private String studentYear;

    public Student(){
        super(0,"");
    }
    public Student(int ID, String studentName, String studentYear) {
        super(ID, studentName);
        this.studentYear= studentYear;
    }

    public int getStudentID() {
        return getID();
    }

    public void setStudentID(int ID) {
        setID(ID);
    }

    public String getStudentName() {
        return getName();
    }

    public void setStudentName(String studentName) {
        setName(studentName);
    }

    public String getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(String studentYear) {
        this.studentYear = studentYear;
    }
}
