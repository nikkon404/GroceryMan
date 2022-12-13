package com.nikkon.groceryman.Models;
import java.util.ArrayList;
import java.util.HashMap;


public class GroceryResponse {
    private String code;
    private long total;
    private long offset;
    private Item[] items;

    public String getCode() { return code; }



    public Item[] getItems() { return items; }

    //GroceryResponse from hashmap
    public GroceryResponse(HashMap<String, Object> map) {
        this.code = (String) map.get("code");
        this.total = (int) map.get("total");
        this.offset = (int) map.get("offset");

        //loop through the itemsArray array and create an item object for each item
        ArrayList itemsArray = (ArrayList) map.get("items");
        //List of Items
           Item[] items = new Item[itemsArray.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item((HashMap<String, Object>) itemsArray.get(i));
        }
        this.items = items;

    }
}


