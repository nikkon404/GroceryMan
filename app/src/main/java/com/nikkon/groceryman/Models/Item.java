package com.nikkon.groceryman.Models;

import com.fasterxml.jackson.annotation.*;

public class Item {
    private int id;
    private String ean;
    private String title;
    private String description;
    private String upc;
    private String brand;
    private String model;
    private String category;
    private String[] images;
    private String elid;
    private String createdAt;

    @JsonProperty("id")
    public int getID() { return id; }
    @JsonProperty("id")
    public void setID(int value) { this.id = value; }

    @JsonProperty("ean")
    public String getEan() { return ean; }
    @JsonProperty("ean")
    public void setEan(String value) { this.ean = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("upc")
    public String getUpc() { return upc; }
    @JsonProperty("upc")
    public void setUpc(String value) { this.upc = value; }

    @JsonProperty("brand")
    public String getBrand() { return brand; }
    @JsonProperty("brand")
    public void setBrand(String value) { this.brand = value; }

    @JsonProperty("model")
    public String getModel() { return model; }
    @JsonProperty("model")
    public void setModel(String value) { this.model = value; }

    @JsonProperty("category")
    public String getCategory() { return category; }
    @JsonProperty("category")
    public void setCategory(String value) { this.category = value; }

    @JsonProperty("images")
    public String[] getImages() { return images; }
    @JsonProperty("images")
    public void setImages(String[] value) { this.images = value; }

    @JsonProperty("elid")
    public String getElid() { return elid; }
    @JsonProperty("elid")
    public void setElid(String value) { this.elid = value; }

    @JsonProperty("createdAt")
    public String getCreatedAt() { return createdAt; }
    @JsonProperty("createdAt")
    public void setCreatedAt(String value) { this.createdAt = value; }
}
