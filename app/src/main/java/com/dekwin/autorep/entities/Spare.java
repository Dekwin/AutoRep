package com.dekwin.autorep.entities;

/**
 * Created by dekst on 28.10.2015.
 */
public class Spare {
    private int id;
    private String name;
    private float price;

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public Spare(int id, String name, float price){
        this.name=name;
        this.id=id;
        this.price=price;
    }


    public void setName(String name){
        this.name=name;
    }

    public void setPrice(float price){
        this.price=price;
    }

    public float getPrice(){
        return price;
    }
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";


}
