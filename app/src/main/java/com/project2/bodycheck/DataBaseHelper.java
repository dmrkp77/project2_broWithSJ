package com.project2.bodycheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Checking.db";
    private static final String DB_TABLE = "Checking_Table";

    private static final String ID = "ID";
    private static final String DATE = "DATE";
    private static final String IMAGE = "IMAGE";

    //위의 테이블 추가시 버전 갱신 필요
    public DataBaseHelper(Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, IMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    //데이터 추가
    public boolean insertData(String date, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        contentValues.put(IMAGE, image);
        long result = db.insert(DB_TABLE, null, contentValues);

        if(result == -1) { return false; }
        else { return true; }
    }

    //데이터 탐색
    public Cursor searchData(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + DB_TABLE + " WHERE " + DATE + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //데이터 삭제
    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, "ID = ?", new String[] {id});
    }

    //데이터 갱신
    /*
    public int updateData(String id, String temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMP, temp);
        return db.update(DB_TABLE, contentValues,"ID = ?", new String[] {id});
    }
    */
}
