package com.project2.bodycheck.main;

import android.app.Activity;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    private ArrayList<String> TEMP;

    private String searchText;

    private String calendar_ID;

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

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        searchText = curYearFormat.format(date) + curMonthFormat.format(date) + curDayFormat.format(date);

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        int temp_total = sharedPreferences.getInt("total", 0);
        int temp_check = sharedPreferences.getInt("check", 0);
        String temp_date = sharedPreferences.getString("date", "");

        if(!temp_date.equals(searchText)) {
            temp_total = 0;
            temp_check = 0;
        }

        TEMP = new ArrayList<String>();
        String temp_num1 = sharedPreferences.getString("pos1", "");
        TEMP.add(temp_num1);
        String temp_num2 = sharedPreferences.getString("pos2", "");
        TEMP.add(temp_num2);
        String temp_num3 = sharedPreferences.getString("pos3", "");
        TEMP.add(temp_num3);
        String temp_num4 = sharedPreferences.getString("pos4", "");
        TEMP.add(temp_num4);
        String temp_num5 = sharedPreferences.getString("pos5", "");
        TEMP.add(temp_num5);
        String temp_num6 = sharedPreferences.getString("pos6", "");
        TEMP.add(temp_num6);
        String temp_num7 = sharedPreferences.getString("pos7", "");
        TEMP.add(temp_num7);

        checkSetting(weekly);
        if(temp_total == 0 || temp_total != totalCount) {
            totalCount = 0;
            CHECK.clear();
            checkSetting(weekly);
        }
        else {
            CHECK.clear();
            for(int i = 0; i < temp_total; i++) { CHECK.add(TEMP.get(i)); }
            totalCount = temp_total;
            checkCount = temp_check;
        }
        searchData(weekly);

        today_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //달성도 판단 후 현재 날짜 달성도 기록 (데베 달력전용 테이블)
                int percentage = checkCount * 100 / totalCount;
                String image;
                if(percentage >= 70) { image = "3"; }
                else if(percentage >= 40) { image = "2"; }
                else { image = "1"; }

                boolean check = checkDB(searchText);
                if(check) { db.updateCalendarData(calendar_ID, image); }
                else { db.insertCalendarData(searchText, image); }

                ContentActivity.today_data = percentage;

                Toast.makeText(ToDoListChecking.this, String.valueOf(percentage), Toast.LENGTH_SHORT).show();
//                checking();
                finish();
            }
        });
    }

    private boolean checkDB(String text) {
        Cursor cursor = db.searchCalendarData(text);
        if(cursor.getCount() == 0) { return false; }
        else {
            while(cursor.moveToNext()) { calendar_ID = cursor.getString(0); }
            return true;
        }
    }

//    private void checking() {
//        Cursor cursor = db.viewCalendarData();
//        if(cursor.getCount() == 0) {  }
//        else {
//            while(cursor.moveToNext()) { Toast.makeText(ToDoListChecking.this, cursor.getString(2), Toast.LENGTH_SHORT).show(); }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int temp_total = totalCount;
        int temp_check = checkCount;
        String temp_date = searchText;

        String pos1 = "";
        String pos2 = "";
        String pos3 = "";
        String pos4 = "";
        String pos5 = "";
        String pos6 = "";
        String pos7 = "";

        if(totalCount >= 1) { pos1 = CHECK.get(0); }
        if(totalCount >= 2) { pos2 = CHECK.get(1); }
        if(totalCount >= 3) { pos3 = CHECK.get(2); }
        if(totalCount >= 4) { pos4 = CHECK.get(3); }
        if(totalCount >= 5) { pos5 = CHECK.get(4); }
        if(totalCount >= 6) { pos6 = CHECK.get(5); }
        if(totalCount >= 7) { pos7 = CHECK.get(6); }


        editor.putInt("total", temp_total);
        editor.putInt("check", temp_check);
        editor.putString("date", temp_date);

        editor.putString("pos1", pos1);
        editor.putString("pos2", pos2);
        editor.putString("pos3", pos3);
        editor.putString("pos4", pos4);
        editor.putString("pos5", pos5);
        editor.putString("pos6", pos6);
        editor.putString("pos7", pos7);

        editor.commit();
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
