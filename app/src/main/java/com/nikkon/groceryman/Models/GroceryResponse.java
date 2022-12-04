package com.nikkon.groceryman.Models;


//     GroceryResponse data = Converter.fromJsonString(jsonString);

import com.fasterxml.jackson.annotation.JsonProperty;



public class GroceryResponse {
    private String code;
    private long total;
    private long offset;
    private Item[] items;

    @JsonProperty("code")
    public String getCode() { return code; }
    @JsonProperty("code")
    public void setCode(String value) { this.code = value; }

    @JsonProperty("total")
    public long getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(long value) { this.total = value; }

    @JsonProperty("offset")
    public long getOffset() { return offset; }
    @JsonProperty("offset")
    public void setOffset(long value) { this.offset = value; }

    @JsonProperty("items")
    public Item[] getItems() { return items; }
    @JsonProperty("items")
    public void setItems(Item[] value) { this.items = value; }
}

// Item.java


