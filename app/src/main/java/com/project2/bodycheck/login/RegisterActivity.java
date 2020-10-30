package com.project2.bodycheck.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.project2.bodycheck.main.ActionHome;
import com.project2.bodycheck.R;
import com.project2.bodycheck.User;
import com.project2.bodycheck.main.ContentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final String KEY_HABIT_DONE ="habitDone";
    private final String KEY_SMOKING = "smoking_check";
    private final String KEY_WORKOUT = "workout_check";
    private final String KEY_VITAMIN = "vitamin_check";
    private final String KEY_OMEGA = "omega_check";
    private final String KEY_LUTEIN = "lutein_check";
    private final String KEY_ANTIOXIDANT = "antioxidant_check";
    private final String KEY_PROBIOTICS = "probiotics_check";
    private final String KEY_ETC = "etc_check";

    final String KEY_SURVEY_DONE = "surveyDone";
    final String KEY_SURVEY_DATE = "surveyDate";
    final String KEY_SURVEY_QUES1 = "goToBed";
    final String KEY_SURVEY_QUES2 = "sleepingTime";
    final String KEY_SURVEY_QUES3 = "breakfastTime";
    final String KEY_SURVEY_QUES4 = "lunchTime";
    final String KEY_SURVEY_QUES5 = "dinnerTime";
    final String KEY_SURVEY_QUES6 = "midnightSnack";
    final String KEY_SURVEY_QUES7 = "stressLevel";
    //define view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private TextView textViewLogin;

    // TextView textviewMessage;
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    //define firebase object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Map<String,Object> userHabit = new HashMap<>();
    private Map<String,Object> userSurvey = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.register_editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.register_editTextPassword);
        editTextUsername = (EditText) findViewById(R.id.register_editTextName);
        textViewLogin = (TextView) findViewById(R.id.register_textViewLogin);
//        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonRegister = (Button) findViewById(R.id.register_button);
        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(listener);
        textViewLogin.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == buttonRegister) {
                registerUser();
            }
            if (view == textViewLogin) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }
    };


    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter the Email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the Password.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter the Username", Toast.LENGTH_SHORT).show();
        }
        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String username = editTextUsername.getText().toString().trim();
                            String email = editTextEmail.getText().toString().trim();
                            String password = editTextPassword.getText().toString().trim();
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            final Date date = new Date();
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd   HH:mm:ss");
                            String formatDate = sdfNow.format(date);

                            User user = new User(formatDate, username, email, password, firebaseUser.getUid(), "0", "0");
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("User").document(email).set(user);

                            userHabit.put(KEY_HABIT_DONE,"0"); userHabit.put(KEY_SMOKING,"0"); userHabit.put(KEY_WORKOUT,"0");
                            userHabit.put(KEY_VITAMIN,"0"); userHabit.put(KEY_OMEGA,"0"); userHabit.put(KEY_LUTEIN,"0");
                            userHabit.put(KEY_ANTIOXIDANT,"0"); userHabit.put(KEY_PROBIOTICS,"0"); userHabit.put(KEY_ETC,"0");
                            db.collection("UserHabit").document(email).set(userHabit);

                            userSurvey.put(KEY_SURVEY_DONE,"0"); userSurvey.put(KEY_SURVEY_DATE, "0"); userSurvey.put(KEY_SURVEY_QUES1,"0"); userSurvey.put(KEY_SURVEY_QUES2,"0");
                            userSurvey.put(KEY_SURVEY_QUES3,"0"); userSurvey.put(KEY_SURVEY_QUES4,"0"); userSurvey.put(KEY_SURVEY_QUES5,"0");userSurvey.put(KEY_SURVEY_QUES6,"0");
                            userSurvey.put(KEY_SURVEY_QUES7,"0"); userSurvey.put("listExample", Arrays.asList("west_coast","sorcal"));
                            db.collection("UserSurvey").document(email).set(userSurvey);

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            //에러발생시
                            Toast.makeText(RegisterActivity.this, "Register Error\n - Email already registered\n - Password at least 6 digits\n - Server Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
