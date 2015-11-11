package com.dekwin.autorep.entities;

/**
 * Created by dekst on 07.11.2015.
 */
public class Responsible {

    private int id;
    private String name;
    private String surname;


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Responsible(int id, String name, String surname) {
        this.name = name;
        this.id = id;
        this.surname = surname;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString(){
        return this.getSurname() + " " + this.getName();
    }

}
