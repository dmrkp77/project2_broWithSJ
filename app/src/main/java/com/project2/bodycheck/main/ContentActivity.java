package com.project2.bodycheck.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project2.bodycheck.R;
import com.project2.bodycheck.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContentActivity extends AppCompatActivity {
    private static final String TAG = "ContentActivity";

    public FragmentManager manager = getSupportFragmentManager();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;
    public ActionHome actionHome;
    public ActionUser actionUser;

    static int data1;
    static String data1_month;
    static String data1_day;
    static int data2;
    static String data2_month;
    static String data2_day;
    static int data3;
    static String data3_month;
    static String data3_day;
    static int data4;
    static String data4_month;
    static String data4_day;
    static int data5;
    static String data5_month;
    static String data5_day;
    static int data6;
    static String data6_month;
    static String data6_day;

    static int today_data;
    static String today_month;
    static String today_day;
    String today;
    private String searchText;

    static int pieScore1;
    static int pieScore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SharedPreferences barData = getSharedPreferences("barData", MODE_PRIVATE);
        int bar1 = barData.getInt("bar1", 0);
        String bar1_month = barData.getString("bar1_month", "xx");
        String bar1_day = barData.getString("bar1_day", "xx");
        int bar2 = barData.getInt("bar2", 0);
        String bar2_month = barData.getString("bar2_month", "xx");
        String bar2_day = barData.getString("bar2_day", "xx");
        int bar3 = barData.getInt("bar3", 0);
        String bar3_month = barData.getString("bar3_month", "xx");
        String bar3_day = barData.getString("bar3_day", "xx");
        int bar4 = barData.getInt("bar4", 0);
        String bar4_month = barData.getString("bar4_month", "xx");
        String bar4_day = barData.getString("bar4_day", "xx");
        int bar5 = barData.getInt("bar5", 0);
        String bar5_month = barData.getString("bar5_month", "xx");
        String bar5_day = barData.getString("bar5_day", "xx");
        int bar6 = barData.getInt("bar6", 0);
        String bar6_month = barData.getString("bar6_month", "xx");
        String bar6_day = barData.getString("bar6_day", "xx");

        int check_today = barData.getInt("check_today", 0);
        String check_month = barData.getString("check_month", "xx");
        String check_day = barData.getString("check_day", "xx");
        String check_date = barData.getString("check_date", "");

        data1 = bar1;
        data1_month = bar1_month;
        data1_day = bar1_day;
        data2 = bar2;
        data2_month = bar2_month;
        data2_day = bar2_day;
        data3 = bar3;
        data3_month = bar3_month;
        data3_day = bar3_day;
        data4 = bar4;
        data4_month = bar4_month;
        data4_day = bar4_day;
        data5 = bar5;
        data5_month = bar5_month;
        data5_day = bar5_day;
        data6 = bar6;
        data6_month = bar6_month;
        data6_day = bar6_day;

        today_data = check_today;
        today_month = check_month;
        today_day = check_day;
        today = check_date;

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        searchText = curYearFormat.format(date) + curMonthFormat.format(date) + curDayFormat.format(date);

        if(!today.equals(searchText)) {
            data1 = data2;
            data1_month = data2_month;
            data1_day = data2_day;
            data2 = data3;
            data2_month = data3_month;
            data2_day = data3_day;
            data3 = data4;
            data3_month = data4_month;
            data3_day = data4_day;
            data4 = data5;
            data4_month = data5_month;
            data4_day = data5_day;
            data5 = data6;
            data5_month = data6_month;
            data5_day = data6_day;
            data6 = today_data;
            data6_month = today_month;
            data6_day = today_day;

            today_data = 0;
            today_month = curMonthFormat.format(date);
            today_day = curDayFormat.format(date);
            today = searchText;
        }

        SharedPreferences pieData = getSharedPreferences("pieData", MODE_PRIVATE);
        int pie1 = pieData.getInt("pie1", 0);
        int pie2 = pieData.getInt("pie2", 0);
        pieScore1 = pie1;
        pieScore2 = pie2;

        actionHome = new ActionHome(this);
        actionUser = new ActionUser(this);

        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        manager.beginTransaction().replace(R.id.content_layout, actionHome).commit();

        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.action_home: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionHome).commitAllowingStateLoss();
                                return true;
                            }

                            case R.id.action_user: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionUser).commitAllowingStateLoss();
                                return true;
                            }

                            case R.id.action_logout: {
                                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
                                getSupportFragmentManager().beginTransaction().remove(fragment);
                                Confirm_logout();
                            }

                            default:
                                return false;

                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences barData = getSharedPreferences("barData", MODE_PRIVATE);
        SharedPreferences.Editor barEditor = barData.edit();

        int bar1 = data1;
        String bar1_month = data1_month;
        String bar1_day = data1_day;
        int bar2 = data2;
        String bar2_month = data2_month;
        String bar2_day = data2_day;
        int bar3 = data3;
        String bar3_month = data3_month;
        String bar3_day = data3_day;
        int bar4 = data4;
        String bar4_month = data4_month;
        String bar4_day = data4_day;
        int bar5 = data5;
        String bar5_month = data5_month;
        String bar5_day = data5_day;
        int bar6 = data6;
        String bar6_month = data6_month;
        String bar6_day = data6_day;

        int check_today = today_data;
        String check_month = today_month;
        String check_day = today_day;
        String check_date = today;

        barEditor.putInt("bar1", bar1);
        barEditor.putString("bar1_month", bar1_month);
        barEditor.putString("bar1_day", bar1_day);
        barEditor.putInt("bar2", bar2);
        barEditor.putString("bar2_month", bar2_month);
        barEditor.putString("bar2_day", bar2_day);
        barEditor.putInt("bar3", bar3);
        barEditor.putString("bar3_month", bar3_month);
        barEditor.putString("bar3_day", bar3_day);
        barEditor.putInt("bar4", bar4);
        barEditor.putString("bar4_month", bar4_month);
        barEditor.putString("bar4_day", bar4_day);
        barEditor.putInt("bar5", bar5);
        barEditor.putString("bar5_month", bar5_month);
        barEditor.putString("bar5_day", bar5_day);
        barEditor.putInt("bar6", bar6);
        barEditor.putString("bar6_month", bar6_month);
        barEditor.putString("bar6_day", bar6_day);

        barEditor.putInt("check_today", check_today);
        barEditor.putString("check_month", check_month);
        barEditor.putString("check_day", check_day);
        barEditor.putString("check_date", check_date);

        barEditor.commit();

        SharedPreferences pieData = getSharedPreferences("pieData", MODE_PRIVATE);
        SharedPreferences.Editor pieEditor = pieData.edit();

        int pie1 = pieScore1;
        int pie2 = pieScore2;

        pieEditor.putInt("pie1", pie1);
        pieEditor.putInt("pie2", pie2);

        pieEditor.commit();
    }

    public void Confirm_logout() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ContentActivity.this);
        alert_confirm.setMessage("All you sure you want to Logout?").setCancelable(true).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                    }
                });
        alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });
        alert_confirm.create();
        alert_confirm.show();
    }

    public void Logout() {

        editor.putBoolean("autoLogin", false);
        editor.commit();
        firebaseAuth.signOut();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.action_home == seletedItemId) {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis();
                Toast.makeText(ContentActivity.this, "Press \'Back\' button again to finish.", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        } else if (R.id.action_user == seletedItemId) {
            manager.beginTransaction().replace(R.id.content_layout, actionHome).commitAllowingStateLoss();
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        } else {
            super.onBackPressed();
        }
    }
}