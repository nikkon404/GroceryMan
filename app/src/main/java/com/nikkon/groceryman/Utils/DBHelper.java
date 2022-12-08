package com.nikkon.groceryman.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "grocery.db";
    private static final int DB_VERSION = 1;

    static final String[] TABLES = {"Grocery", "Todo"};
    static final String[] COLS_GROCERY = {
            "id INTEGER PRIMARY KEY AUTOINCREMENT",
            "ean TEXT NOT NULL",
            "title TEXT NOT NULL",
            "description TEXT",
            "upc TEXT",
            "brand TEXT",
            "model TEXT",
            "category TEXT",
            "images TEXT",
            "elid TEXT",
            "expdate INTEGER",
            "createdAt INTEGER"};

    static final String[] COLS_TODO = {
            "id INTEGER PRIMARY KEY AUTOINCREMENT",
            "title TEXT NOT NULL",
            "createdAt TEXT"
    };

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public String getTableCreationSQL(String tableName, String[] columns) {
        StringBuilder statement = new StringBuilder("create table " + tableName + " (");
        for (int i = 0; i < columns.length; i++) {
            statement.append(columns[i]);
            if (i < columns.length - 1) {
                statement.append(", ");
            }
        }
        statement.append(");");
        return statement.toString();
    }

    // create tables in database
    public void createTables(SQLiteDatabase db, String[] tableNames, String[][] columns) {
        for (int i = 0; i < tableNames.length; i++) {
            db.execSQL(getTableCreationSQL(tableNames[i], columns[i]));
        }
    }

    // drop tables in database
    public void dropTables(SQLiteDatabase db, String[] tableNames) {
        for (String tableName : tableNames) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase, TABLES, new String[][]{COLS_GROCERY , COLS_TODO});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTables(sqLiteDatabase, TABLES);
        onCreate(sqLiteDatabase);
    }

}
