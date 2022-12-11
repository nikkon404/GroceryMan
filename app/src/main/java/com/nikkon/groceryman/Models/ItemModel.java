package com.nikkon.groceryman.Models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikkon.groceryman.Utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemModel {
    /*
    addItem(Item item) - add item to database
    updateItem(Item item) - update item in database
    deleteItem(int id) - delete item from database
    findItemById(int id) - get item from database
    findAllItems() - get all items from database
    findItemsByCategory(String ean) - get items by category from database
    searchItemsByTitle(String title) - search items by title from database
    findItemByEan(String ean) - get item by ean from database
     */

    Context context;

    // constructor
    public ItemModel(Context context) {
        this.context = context;
    }

    // add item
    public int addItem(Item item) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ean", item.getEan());
        values.put("title", item.getTitle());
        values.put("description", item.getDescription());
        values.put("upc", item.getUpc());
        values.put("brand", item.getBrand());
        values.put("model", item.getModel());
        values.put("category", item.getCategory());
        values.put("images", item.getBase64Image());
        values.put("elid", item.getElid());

        // datetime string of now
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        values.put("createdAt", Calendar.getInstance().getTimeInMillis());
        values.put("expdate", item.getExpdate().getTime());

        long result = db.insert("Grocery", null, values);
        db.close();
        //long to int
        return (int) result;

    }

    // update item
    public boolean updateItem(Item item) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ean", item.getEan());
        values.put("title", item.getTitle());
        values.put("description", item.getDescription());
        values.put("upc", item.getUpc());
        values.put("brand", item.getBrand());
        values.put("model", item.getModel());
        values.put("category", item.getCategory());

        // convert images array to string
        String images = item.getBase64Image();
        values.put("images", images);
        values.put("elid", item.getElid());
        values.put("expdate", item.getExpdate().getTime());


        long result = db.update("Grocery", values, "id = ?", new String[]{String.valueOf(item.getID())});
        db.close();

        return result != -1;
    }

    // delete item
    public boolean deleteItem(int id) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long result = db.delete("Grocery", "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return result != -1;
    }

    @SuppressLint("Range")
    private Item composeItem(Cursor cursor){
        Item item = new Item();
        item.setID(cursor.getInt(cursor.getColumnIndex("id")));
        item.setEan(cursor.getString(cursor.getColumnIndex("ean")));
        item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        item.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        item.setUpc(cursor.getString(cursor.getColumnIndex("upc")));
        item.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
        item.setModel(cursor.getString(cursor.getColumnIndex("model")));
        item.setCategory(cursor.getString(cursor.getColumnIndex("category")));

        item.setBase64Image(cursor.getString(cursor.getColumnIndex("images")));
        item.setElid(cursor.getString(cursor.getColumnIndex("elid")));

        item.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex("createdAt"))));
        item.setExpdate(new Date(cursor.getLong(cursor.getColumnIndex("expdate"))));

        return item;
    }

    // find item by id
    public Item findItemById(int id) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Grocery WHERE id = ?", new String[]{String.valueOf(id)});
        Item item = null;
        if (cursor.moveToFirst()) {
            item = composeItem(cursor);
        }
        cursor.close();
        db.close();

        return item;
    }

    // find all items
    public Item[] findAllItems() {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Grocery ORDER by expdate ASC", null);
        Item[] items = new Item[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            items[i] = composeItem(cursor);
            i++;
        }
        cursor.close();
       // db.close();

        return items;
    }

    // find items by category
    public Item[] findItemsByCategory(String category) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Grocery WHERE category = ?", new String[]{category});
        Item[] items = new Item[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            items[i] = composeItem(cursor);
            i++;
        }
        cursor.close();
        db.close();

        return items;
    }

    // search item by title
    public Item[] searchItemsByTitle(String title) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Grocery WHERE title LIKE ?", new String[]{"%" + title + "%"});
        Item[] items = new Item[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            items[i] = composeItem(cursor);
            i++;
        }
        cursor.close();
        db.close();

        return items;
    }

    // search item by ean
    public Item[] findItemByEan(String ean) {
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Grocery WHERE ean = ?", new String[]{ean});
        Item[] items = new Item[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            items[i] = composeItem(cursor);
            i++;
        }
        cursor.close();
        db.close();

        return items;
    }
}
