package com.dekwin.autorep.entities;

/**
 * Created by igor on 07.11.15.
 */
public class Organization {
    private int id;
    private String name;
    private String account;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Organization(int id, String name, String account, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.account = account;
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
