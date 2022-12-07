package com.nikkon.groceryman.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikkon.groceryman.Utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoModel{
    /*
    addTodo(Todo todo) - add todo to database
    updateTodoById(Todo todo) - update todo in database
    deleteTodoById(int id) - delete todo from database
    findTodoById(int id) - get todo from database
    findAllTodos() - get all todos from database
     */

    private Context context;

    public TodoModel(Context context){
        this.context = context;
    }

    // create new
    public boolean addTodo(Todo todo){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", todo.getTitle());
        // current datetime
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        values.put("createdAt", datetime);
        long result = db.insert("Todo", null, values);
        db.close();
        return result != -1;
    }

    // update
    public boolean updateTodoById(Todo todo){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", todo.getTitle());
        long result = db.update("Todo", values, "id = ?", new String[]{String.valueOf(todo.getId())});
        db.close();
        return result != -1;
    }

    // delete
    public boolean deleteTodoById(int id){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long result = db.delete("Todo", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result != -1;
    }

    // get all
    public Todo[] findAllTodos(){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo", null);
        Todo[] todos = new Todo[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()){
            Todo todo = new Todo();
            todo.setId(cursor.getInt(0));
            todo.setTitle(cursor.getString(1));
            todo.setCreatedAt(cursor.getString(2));
            todos[i] = todo;
            i++;
        }
        cursor.close();
        db.close();
        return todos;
    }

    // get by id
    public Todo findTodoById(int id){
        DBHelper dbhelper = new DBHelper(this.context);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo WHERE id = ?", new String[]{String.valueOf(id)});
        Todo todo = new Todo();
        if (cursor.moveToNext()){
            todo.setId(cursor.getInt(0));
            todo.setTitle(cursor.getString(1));
            todo.setCreatedAt(cursor.getString(2));
        }
        cursor.close();
        db.close();
        return todo;
    }
}
