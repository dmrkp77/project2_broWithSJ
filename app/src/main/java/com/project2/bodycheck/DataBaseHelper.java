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
    private static final String CONTENTS1 = "CONTENTS1";
    private static final String CONTENTS2 = "CONTENTS2";
    private static final String CONTENTS3 = "CONTENTS3";
    private static final String CONTENTS4 = "CONTENTS4";
    private static final String CONTENTS5 = "CONTENTS5";

    //위의 테이블 추가시 버전 갱신 필요
    public DataBaseHelper(Context context) { super(context, DB_NAME, null, 2); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, IMAGE TEXT, CONTENTS1 TEXT, CONTENTS2 TEXT, CONTENTS3 TEXT, CONTENTS4 TEXT, CONTENTS5 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    //데이터 추가
    public boolean insertData(String date, String image, String contents1, String contents2, String contents3, String contents4, String contents5) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        contentValues.put(IMAGE, image);
        contentValues.put(CONTENTS1, contents1);
        contentValues.put(CONTENTS2, contents2);
        contentValues.put(CONTENTS3, contents3);
        contentValues.put(CONTENTS4, contents4);
        contentValues.put(CONTENTS5, contents5);
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

    /*
    //데이터 갱신
    public int updateData(String id, String temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMP, temp);
        return db.update(DB_TABLE, contentValues,"ID = ?", new String[] {id});
    }
    */
}
