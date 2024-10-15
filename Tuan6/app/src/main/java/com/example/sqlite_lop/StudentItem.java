package com.example.sqlite_lop;

public class StudentItem {
    private int id; // ID của sinh viên
    private String name;
    private String dob;

    public StudentItem(int id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }
}
