package com.nikkon.groceryman.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikkon.groceryman.Utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShoppingModel {
    /*
    addShopping(Shopping Shopping) - add Shopping to database
    updateShoppingById(Shopping Shopping) - update Shopping in database
    deleteShoppingById(int id) - delete Shopping from database
    findShoppingById(int id) - get Shopping from database
    findAllShoppings() - get all Shoppings from database
     */

    private Context context;

    public ShoppingModel(Context context){
        this.context = context;
    }

    // create new
    public boolean addShopping(Shopping shopping){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", shopping.getTitle());
        // current datetime
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        values.put("createdAt", datetime);
        long result = db.insert("Shopping", null, values);
        db.close();
        return result != -1;
    }

    // update
    public boolean updateShoppingById(Shopping shopping){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", shopping.getTitle());
        long result = db.update("Shopping", values, "id = ?", new String[]{String.valueOf(shopping.getId())});
        db.close();
        return result != -1;
    }

    // delete
    public boolean deleteShoppingById(int id){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long result = db.delete("Shopping", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result != -1;
    }

    // get all
    public Shopping[] findAllShoppings(){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Shopping", null);
        Shopping[] shoppings = new Shopping[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()){
            Shopping shopping = new Shopping();
            shopping.setId(cursor.getInt(0));
            shopping.setTitle(cursor.getString(1));
            shopping.setCreatedAt(cursor.getString(2));
            shoppings[i] = shopping;
            i++;
        }
        cursor.close();
        db.close();
        return shoppings;
    }

    // get by id
    public Shopping findShoppingById(int id){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Shopping WHERE id = ?", new String[]{String.valueOf(id)});
        Shopping shopping = new Shopping();
        if (cursor.moveToNext()){
            shopping.setId(cursor.getInt(0));
            shopping.setTitle(cursor.getString(1));
            shopping.setCreatedAt(cursor.getString(2));
        }
        cursor.close();
        db.close();
        return shopping;
    }
}
