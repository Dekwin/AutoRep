package com.dekwin.autorep.entities;

/**
 * Created by dekst on 05.11.2015.
 */
public class Work {
    private int id;
    private String name;
    private float price;
    private int repairsId;
    private boolean isSelected = false;

    public int getRepairsId() {
        return repairsId;
    }

    public void setRepairsId(int repairsId) {
        this.repairsId = repairsId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Work(int id, String name, float price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public Work(int id, String name, float price, int repairsId) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.repairsId = repairsId;
    }

    public void isSelected(boolean select) {
        this.isSelected = select;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

}
