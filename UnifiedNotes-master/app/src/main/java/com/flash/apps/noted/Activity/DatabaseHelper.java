package com.flash.apps.noted.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Book";
    public static final String TABLE_NAME = "Book_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "LOCATION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LOCATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,location);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        Log.d("Inserted?", String.valueOf(result));
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " +COL_2+ " , "+COL_3+" from "+TABLE_NAME,null);
        return res;
    }

    public Cursor search(String BooKName) {
        SQLiteDatabase db = this.getReadableDatabase();

        //String[] columnNames = new String[] {COL_1,COL_2};
        //String whereClause = "COL_2=1";

        //Cursor res = db.query(DATABASE_NAME, columnNames, whereClause, null, null, null, null);
        String sql = "select " +COL_2+"  from "+TABLE_NAME+" WHERE "+COL_2+" = "+"'BookName'";

        Cursor res = db.rawQuery(sql,null);
        return res;

    }

    public boolean updateData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        db.update(TABLE_NAME, contentValues, "name = '%+name+%'",new String[] { name });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

}