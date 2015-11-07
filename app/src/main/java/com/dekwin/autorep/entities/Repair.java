package com.dekwin.autorep.entities;

/**
 * Created by dekst on 05.11.2015.
 */
public class Repair {
    private int id;
    private String name;
    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public Repair(int id, String name){
        this.name=name;
        this.id=id;

    }


    public void setName(String name){
        this.name=name;
    }
}
