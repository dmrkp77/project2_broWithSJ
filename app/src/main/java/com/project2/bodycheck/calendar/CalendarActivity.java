package com.project2.bodycheck.calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.project2.bodycheck.DataBaseHelper;
import com.project2.bodycheck.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends Activity {

    private Button moveLeft;
    private Button moveRight;

    //현재 연, 월 표시용
    private TextView tvDate;

    private CalendarGridViewAdapter myGridAdapter = null;
    private GridView gridView;

    //달력 계산용
    private Calendar mCal;
    //행렬 표시용
    private Integer check_count;
    //날짜 계산용
    private Integer blank_count;
    DataBaseHelper db;

    private String searchText;

    private int current_year;
    private int current_month;

    private int temp_set_year;
    private int temp_set_month;

    private int prev_count;
    private int next_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        db = new DataBaseHelper(this);

        check_count = 0;
        blank_count = 0;

        moveLeft = findViewById(R.id.move_left);
        moveRight = findViewById(R.id.move_right);
        tvDate = findViewById(R.id.tv_date);
        gridView = findViewById(R.id.gridview);
        myGridAdapter = new CalendarGridViewAdapter(this);
        gridView.setAdapter(myGridAdapter);

        //오늘 날짜 세팅
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연, 월 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        //현재 날짜 텍스트 적용
        tvDate.setText(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");
        searchText = curYearFormat.format(date) + curMonthFormat.format(date);

        mCal = Calendar.getInstance();
        //이번달 1일 요일 판단
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

        current_year = mCal.get(Calendar.YEAR);
        current_month = mCal.get(Calendar.MONTH);

        temp_set_year = current_year;
        temp_set_month = current_month;
        if(temp_set_month == 0) {
            temp_set_year -= 1;
            temp_set_month = 11;
        }
        else { temp_set_month -= 1; }
        mCal.set(temp_set_year, temp_set_month, 1);
        prev_count = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        mCal.set(current_year, current_month, 1);

        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일, 요일 매칭 시키기 위해 공백 추가
        for(int i = 1; i < dayNum; i++) {
            int temp = prev_count - dayNum + i + 1;
            String index = String.valueOf(temp);
            myGridAdapter.addItem(new CalendarGridViewData(index, "0", "0"));
            check_count += 1;
            blank_count += 1;
        }

        //1일 부터 날짜 추가
        setCalendarDate(current_month + 1);

        //왼쪽 버튼
        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGridAdapter.clear();
                check_count = 0;
                blank_count = 0;

                if(current_month == 0) {
                    current_year -= 1;
                    current_month = 11;
                }
                else { current_month -= 1; }
                mCal.set(current_year, current_month, 1);

                temp_set_year = current_year;
                temp_set_month = current_month;
                if(temp_set_month == 0) {
                    temp_set_year -= 1;
                    temp_set_month = 11;
                }
                else { temp_set_month -= 1; }
                mCal.set(temp_set_year, temp_set_month, 1);
                prev_count = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                mCal.set(current_year, current_month, 1);

                int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
                for(int i = 1; i < dayNum; i++) {
                    int temp = prev_count - dayNum + i + 1;
                    String index = String.valueOf(temp);
                    myGridAdapter.addItem(new CalendarGridViewData(index, "0", "0"));
                    check_count += 1;
                    blank_count += 1;
                }
                String temp_year = String.valueOf(current_year);
                String temp_month;
                if(current_month < 9) { temp_month = "0" + String.valueOf(current_month + 1); }
                else { temp_month = String.valueOf(current_month + 1); }
                tvDate.setText(temp_year + "년 " + temp_month + "월");
                searchText = temp_year + temp_month;
                setCalendarDate(current_month + 1);
            }
        });

        //오른쪽 버튼
        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGridAdapter.clear();
                check_count = 0;
                blank_count = 0;
                prev_count = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                if(current_month == 11) {
                    current_year += 1;
                    current_month = 0;
                }
                else { current_month += 1; }
                mCal.set(current_year, current_month, 1);

                int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
                for(int i = 1; i < dayNum; i++) {
                    int temp = prev_count - dayNum + i + 1;
                    String index = String.valueOf(temp);
                    myGridAdapter.addItem(new CalendarGridViewData(index, "0", "0"));
                    check_count += 1;
                    blank_count += 1;
                }
                String temp_year = String.valueOf(current_year);
                String temp_month;
                if(current_month < 9) { temp_month = "0" + String.valueOf(current_month + 1); }
                else { temp_month = String.valueOf(current_month + 1); }
                tvDate.setText(temp_year + "년 " + temp_month + "월");
                searchText = temp_year + temp_month;
                setCalendarDate(current_month + 1);
            }
        });
    }

    private String tempText;

    //해당 월에 표시할 일 수 계산
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for(int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            if(i < 10) { tempText = searchText + "0" + (i + 1); }
            else { tempText = searchText + (i + 1); }
            searchData(tempText, String.valueOf(i + 1));
            check_count += 1;
        }
        next_count = 1;
        //빈칸 채우기용
        for(int i = check_count; i < 42; i++) {
            String index = String.valueOf(next_count);
            myGridAdapter.addItem(new CalendarGridViewData(index, "0", "0"));
            check_count += 1;
            next_count += 1;
        }
        check_count = 0;
    }

    private int textViewCount = -1;
    private String tempImage = "";

    //데이터베이스 조회하기 (일자별 일정 조회)
    private void searchData(String text, String index) {
        Cursor cursor = db.searchCalendarData(text);

        textViewCount = 0;
        tempImage = "0";

        if(cursor.getCount() == 0) {
            //Toast.makeText(CalendarActivity.this, "저장된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()) { tempImage = cursor.getString(2); }
        }

        myGridAdapter.addItem(new CalendarGridViewData(index, "1", tempImage));
        myGridAdapter.notifyDataSetChanged();
    }
}
