package com.anhnlp.models;

public class Employee {
    private int id;
    private String name;
    private String emaill;
    private String phone;
    private String username;
    private String password;
    private boolean saveinfo;

    public Employee() {
    }

    public Employee(int id, String name, String emaill, String phone, String username, String password, boolean saveinfo) {
        this.id = id;
        this.name = name;
        this.emaill = emaill;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.saveinfo = saveinfo;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmaill() {
        return emaill;
    }

    public void setEmaill(String emaill) {
        this.emaill = emaill;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSaveinfo() {
        return saveinfo;
    }

    public void setSaveinfo(boolean saveinfo) {
        this.saveinfo = saveinfo;
    }
}
