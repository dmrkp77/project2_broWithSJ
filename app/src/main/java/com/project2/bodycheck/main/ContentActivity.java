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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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