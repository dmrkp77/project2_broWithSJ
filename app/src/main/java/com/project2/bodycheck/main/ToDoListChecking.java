package com.project2.bodycheck.main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.project2.bodycheck.DataBaseHelper;
import com.project2.bodycheck.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ToDoListChecking extends Activity {

    private DataBaseHelper db;

    private CheckingListAdapter myAdapter = null;
    private ListView checkingList;
    private ArrayList<String> ID;

    //요일 판단
    private Calendar mCal;
    private int weekNum;
    private String weekly;
    private String OnDay = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_checking);

        mCal = Calendar.getInstance();
        weekNum = mCal.get(Calendar.DAY_OF_WEEK);
        weekly = String.valueOf(weekNum);

        db = new DataBaseHelper(this);
        myAdapter = new CheckingListAdapter(this);

        checkingList = findViewById(R.id.checking_listView);
        checkingList.setAdapter(myAdapter);

        searchData(weekly);
    }

    private void searchData(String text) {
        if(text.equals("1")) {
            Cursor cursor = db.searchSUN(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("2")) {
            Cursor cursor = db.searchMON(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("3")) {
            Cursor cursor = db.searchTUE(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("4")) {
            Cursor cursor = db.searchWED(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("5")) {
            Cursor cursor = db.searchTHU(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("6")) {
            Cursor cursor = db.searchFRI(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        else if(text.equals("7")) {
            Cursor cursor = db.searchSAT(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1)));
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }
}
