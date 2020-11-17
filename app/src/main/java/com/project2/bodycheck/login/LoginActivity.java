package com.project2.bodycheck.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project2.bodycheck.PreferenceManager;
import com.project2.bodycheck.R;
import com.project2.bodycheck.main.ContentActivity;


public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewMessage;
    private TextView textViewForgotPassword;
    private TextView textViewRegister;

    CheckBox autoLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore db;
    Boolean loginChecked;
    SharedPreferences pref;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    PreferenceManager util;
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autoLogin = (CheckBox) findViewById(R.id.login_autoLogin);
        loginButton = (Button) findViewById(R.id.login_button);
        editTextEmail = (EditText) findViewById(R.id.login_editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.login_editTextPassword);
        textViewMessage = (TextView) findViewById(R.id.login_textViewMessage);
        textViewForgotPassword = (TextView) findViewById(R.id.login_forgotPassword);
        textViewRegister = (TextView) findViewById(R.id.login_registerTextview);
        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        util = new PreferenceManager(LoginActivity.this);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

//        if (util.getString(LoginActivity.this, "회원탈퇴").equals("1")) {
//            util.setString(LoginActivity.this, "회원탈퇴", "0");
//            deleteUserInfo();
//            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    Toast.makeText(LoginActivity.this, "Delete your account successfully", Toast.LENGTH_LONG).show();
//                    editor.clear();
//                    editor.commit();
//                    firebaseAuth.signOut();
//                }
//            });
//        }

        // if autoLogin checked, get input
        if (setting.getBoolean("autoLogin",false)) {
            editTextEmail.setText(setting.getString("id",""));
            editTextPassword.setText(setting.getString("pw",""));
            autoLogin.setChecked(true);
            userLogin();
        }

        if (!setting.getString("id","").equals("")) {
            editTextEmail.setText(setting.getString("id",""));
            editTextPassword.setText(setting.getString("pw",""));
            autoLogin.setChecked(true);
        }

        loginButton.setOnClickListener(listener);
        textViewForgotPassword.setOnClickListener(listener);
        textViewRegister.setOnClickListener(listener);

        autoLogin.setOnCheckedChangeListener(listener2);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == loginButton) {
                userLogin();
            }
            if (view == textViewRegister) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
            if (view == textViewForgotPassword) {
                startActivity(new Intent(getApplicationContext(), FindActivity.class));
                finish();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener listener2 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                loginChecked = true;
            } else {
                // if unChecked, removeAll
                loginChecked = false;
                editor.clear();
                editor.commit();
            }
        }
    };


    private void userLogin() {
        PreferenceManager.setString(this, "id", editTextEmail.getText().toString().trim()); //id라는 키값으로 저장
        PreferenceManager.setString(this, "pw", editTextPassword.getText().toString().trim()); //pw라는 키값으로 저장

        String checkId = null;
        String checkPw = null;

        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        checkId = PreferenceManager.getString(this, "id");
        checkPw = PreferenceManager.getString(this, "pw");


        if (TextUtils.isEmpty(checkId)) {
            Toast.makeText(this, "Please enter the email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(checkPw)) {
            Toast.makeText(this, "Please enter the password.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Waiting Please...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(checkId, checkPw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            if (autoLogin.isChecked()) {
                                String ID = editTextEmail.getText().toString();
                                String PW = editTextPassword.getText().toString();

                                editor.putString("id", ID);
                                editor.putString("pw", PW);
                                editor.putBoolean("autoLogin", true);
                                editor.commit();
                            } else {
                                editor.clear();
                                editor.commit();
                            }
                            Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ContentActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to login.", Toast.LENGTH_LONG).show();
                            textViewMessage.setText("    Failure Type\n\n  -1- Incorrect password \n  -2- Server Error");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "Press \'Back\' button again to finish.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            super.onBackPressed();
        }
    }
}