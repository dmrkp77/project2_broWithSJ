package com.project2.bodycheck.main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private Button today_check;

    //요일 판단
    private Calendar mCal;
    private int weekNum;
    private String weekly;
    private String OnDay = "1";

    //달성도 판단
    private ArrayList<String> CHECK;
    private int totalCount;
    private int checkCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_checking);

        mCal = Calendar.getInstance();
        weekNum = mCal.get(Calendar.DAY_OF_WEEK);
        weekly = String.valueOf(weekNum);

        totalCount = 0;
        checkCount = 0;

        db = new DataBaseHelper(this);
        myAdapter = new CheckingListAdapter(this);

        today_check = findViewById(R.id.button_today_check);

        checkingList = findViewById(R.id.checking_listView);
        checkingList.setAdapter(myAdapter);
        checkingList.setOnItemClickListener(new ListViewItemClickListener());

        checkSetting(weekly);
        searchData(weekly);

        today_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //달성도 판단 후 현재 날짜 달성도 기록 (데베 달력전용 테이블)
            }
        });
    }

    private void checkSetting(String text) {
        CHECK = new ArrayList<String>();

        if(text.equals("1")) {
            Cursor cursor = db.searchSUN(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("2")) {
            Cursor cursor = db.searchMON(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("3")) {
            Cursor cursor = db.searchTUE(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("4")) {
            Cursor cursor = db.searchWED(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("5")) {
            Cursor cursor = db.searchTHU(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("6")) {
            Cursor cursor = db.searchFRI(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        else if(text.equals("7")) {
            Cursor cursor = db.searchSAT(OnDay);
            if(cursor.getCount() == 0) {
                //none
            } else {
                while(cursor.moveToNext()) {
                    CHECK.add("0");
                    totalCount += 1;
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private int count;

    private void searchData(String text) {
        count = 0;

        if(text.equals("1")) {
            Cursor cursor = db.searchSUN(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("2")) {
            Cursor cursor = db.searchMON(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("3")) {
            Cursor cursor = db.searchTUE(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("4")) {
            Cursor cursor = db.searchWED(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("5")) {
            Cursor cursor = db.searchTHU(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("6")) {
            Cursor cursor = db.searchFRI(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        else if(text.equals("7")) {
            Cursor cursor = db.searchSAT(OnDay);
            if(cursor.getCount() == 0) {
                Toast.makeText(ToDoListChecking.this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while(cursor.moveToNext()) {
                    myAdapter.addItem(new CheckingListData(cursor.getString(1), CHECK.get(count)));
                    count += 1;
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private Integer selectedPos = -1;

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            selectedPos = position;
            if(CHECK.get(selectedPos).equals("0")) {
                CHECK.set(selectedPos, "1");
                myAdapter.clear();
                searchData(weekly);
                checkCount += 1;
            }
            else {
                CHECK.set(selectedPos, "0");
                myAdapter.clear();
                searchData(weekly);
                checkCount -= 1;
            }
        }
    }
}
