package com.inti.student.neverforget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ListDatabase extends SQLiteOpenHelper {

    private static final String DatabaseName = "TaskList";
    private static final int DatabaseVersion = 1;
    public static final String DatabaseTableName="Task";
    public static final String DatabaseColumn = "TaskName";
    public static final String DatabaseColumn2 = "DueDate";

    public ListDatabase(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL, %s TEXT NOT NULL);",DatabaseTableName, DatabaseColumn, DatabaseColumn2);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s",DatabaseTableName);
        db.execSQL(query);
        onCreate(db);

    }

    public void insertNewTask(String task, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseColumn,task);
        values.put(DatabaseColumn2,date);
        db.insertWithOnConflict(DatabaseTableName,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseTableName + " WHERE " + DatabaseColumn + " = '" + taskID + "'";
        db.execSQL(query);
    }

    public Cursor readTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseTableName;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
