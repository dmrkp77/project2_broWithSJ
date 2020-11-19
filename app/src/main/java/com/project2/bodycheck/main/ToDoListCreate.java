package com.project2.bodycheck.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.project2.bodycheck.DataBaseHelper;
import com.project2.bodycheck.R;

public class ToDoListCreate extends Activity {

    private TextView sun;
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;
    private TextView sat;
    private EditText createTitle;
    private Button createConfirm;

    private DataBaseHelper db;

    private String SunSet = "0";
    private String MonSet = "0";
    private String TueSet = "0";
    private String WedSet = "0";
    private String ThuSet = "0";
    private String FriSet = "0";
    private String SatSet = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_create);

        db = new DataBaseHelper(this);

        sun = findViewById(R.id.create_SUN);
        mon = findViewById(R.id.create_MON);
        tue = findViewById(R.id.create_TUE);
        wed = findViewById(R.id.create_WED);
        thu = findViewById(R.id.create_THU);
        fri = findViewById(R.id.create_FRI);
        sat = findViewById(R.id.create_SAT);
        createTitle = findViewById(R.id.create_title);
        createConfirm = findViewById(R.id.create_confirm);

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SunSet.equals("0")) {
                    sun.setBackgroundResource(R.drawable.oval);
                    SunSet = "1";
                }
                else {
                    sun.setBackgroundResource(0);
                    SunSet = "0";
                }
            }
        });

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MonSet.equals("0")) {
                    mon.setBackgroundResource(R.drawable.oval);
                    MonSet = "1";
                }
                else {
                    mon.setBackgroundResource(0);
                    MonSet = "0";
                }
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TueSet.equals("0")) {
                    tue.setBackgroundResource(R.drawable.oval);
                    TueSet = "1";
                }
                else {
                    tue.setBackgroundResource(0);
                    TueSet = "0";
                }
            }
        });

        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WedSet.equals("0")) {
                    wed.setBackgroundResource(R.drawable.oval);
                    WedSet = "1";
                }
                else {
                    wed.setBackgroundResource(0);
                    WedSet = "0";
                }
            }
        });

        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ThuSet.equals("0")) {
                    thu.setBackgroundResource(R.drawable.oval);
                    ThuSet = "1";
                }
                else {
                    thu.setBackgroundResource(0);
                    ThuSet = "0";
                }
            }
        });

        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FriSet.equals("0")) {
                    fri.setBackgroundResource(R.drawable.oval);
                    FriSet = "1";
                }
                else {
                    fri.setBackgroundResource(0);
                    FriSet = "0";
                }
            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SatSet.equals("0")) {
                    sat.setBackgroundResource(R.drawable.oval);
                    SatSet = "1";
                }
                else {
                    sat.setBackgroundResource(0);
                    SatSet = "0";
                }
            }
        });

        createConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = createTitle.getText().toString();

                if(!title.equals("")) {
                    db.insertListData(title, SunSet, MonSet, TueSet, WedSet, ThuSet, FriSet, SatSet);
                    Toast.makeText(ToDoListCreate.this, "Data Added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ToDoListCreate.this, "Please enter title", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
