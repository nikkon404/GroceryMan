package com.nikkon.groceryman.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Item implements Serializable {
    private int id;
    private String ean;
    private String title;
    private String description;
    private String upc;
    private String brand;
    private String model;
    private String category;
    private ArrayList<String> images;
    private String elid;
    private Date createdAt;
    private Date expdate;
    private String base64;

    public int getID() { return id; }
    public void setID(int value) { this.id = value; }

    public String getEan() { return ean; }
    public void setEan(String value) { this.ean = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getUpc() { return upc; }
    public void setUpc(String value) { this.upc = value; }

    public String getBrand() { return brand; }
    public void setBrand(String value) { this.brand = value; }

    public String getModel() { return model; }
    public void setModel(String value) { this.model = value; }

    public String getCategory() { return category; }
    public void setCategory(String value) { this.category = value; }

    public ArrayList<String> getImages() { return images; }
    public void setImages(ArrayList<String> value) { this.images = value; }

    public String getElid() { return elid; }
    public void setElid(String value) { this.elid = value; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date value) { this.createdAt = value; }
    //empty constructor
    public Item() {
    }

    public String getBase64Image(){
       return base64;
    }

    public void setBase64Image(String base64){
        this.base64 = base64;
    }

    //Item from hashmaps
    public Item(HashMap<String, Object> item) {
//        this.id = (int) item.get("id");
        this.ean = (String) item.get("ean");
        this.title = (String) item.get("title");
        this.description = (String) item.get("description");
        this.upc = (String) item.get("upc");
        this.brand = (String) item.get("brand");
        this.model = (String) item.get("model");
        this.category = (String) item.get("category");
        this.images = (ArrayList<String>) item.get("images");
        this.elid = (String) item.get("elid");
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }

    //get days before expiration
    public int getDaysBeforeExpiration(){
        Date today = new Date();
        long diff = expdate.getTime() - today.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
