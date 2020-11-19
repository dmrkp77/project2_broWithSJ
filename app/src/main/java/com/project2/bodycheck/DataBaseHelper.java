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

    private static final String TODO_TABLE = "Todo_Table";
    private static final String TODO_ID = "TODO_ID";
    private static final String CONTENTS = "CONTENTS";
    private static final String SUN = "SUN";
    private static final String MON = "MON";
    private static final String TUE = "TUE";
    private static final String WED = "WED";
    private static final String THU = "THU";
    private static final String FRI = "FRI";
    private static final String SAT = "SAT";

    //위의 테이블 추가시 버전 갱신 필요
    public DataBaseHelper(Context context) { super(context, DB_NAME, null, 5); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, IMAGE TEXT)");
        db.execSQL("create table " + TODO_TABLE + " (TODO_ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTENTS TEXT, SUN TEXT, MON TEXT, TUE TEXT, WED TEXT, THU TEXT, FRI TEXT, SAT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    //캘린더 데이터 추가
    public boolean insertCalendarData(String date, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        contentValues.put(IMAGE, image);
        long result = db.insert(DB_TABLE, null, contentValues);

        if(result == -1) { return false; }
        else { return true; }
    }

    //리스트 데이터 추가
    public boolean insertListData(String contents, String sun, String mon, String tue, String wed, String thu, String fri, String sat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTENTS, contents);
        contentValues.put(SUN, sun);
        contentValues.put(MON, mon);
        contentValues.put(TUE, tue);
        contentValues.put(WED, wed);
        contentValues.put(THU, thu);
        contentValues.put(FRI, fri);
        contentValues.put(SAT, sat);
        long result = db.insert(TODO_TABLE, null, contentValues);

        if(result == -1) { return false; }
        else { return true; }
    }

    //캘린더 데이터 탐색
    public Cursor searchCalendarData(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE + " WHERE " + DATE + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //리스트 데이터 탐색(요일별)
    public Cursor searchSUN(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + SUN + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchMON(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + MON + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchTUE(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + TUE + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchWED(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + WED + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchTHU(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + THU + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchFRI(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + FRI + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor searchSAT(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TODO_TABLE + " WHERE " + SAT + " Like '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //리스트 전체 탐색
    public Cursor viewListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TODO_TABLE, null);
        return cursor;
    }

    //캘린더 데이터 삭제
    public int deleteCalendarData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, "ID = ?", new String[] {id});
    }

    //리스트 데이터 삭제
    public int deleteListData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE, "TODO_ID = ?", new String[] {id});
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
