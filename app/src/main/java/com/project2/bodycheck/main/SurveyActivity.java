package com.project2.bodycheck.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project2.bodycheck.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {
    private final String TAG = "SurveyActivity";

    final String KEY_SURVEY_DONE = "surveyDone";
    final String KEY_SURVEY_DATE = "surveyDate";
    final String KEY_SURVEY_QUES1 = "goToBed";
    final String KEY_SURVEY_QUES2 = "sleepingTime";
    final String KEY_SURVEY_QUES3 = "breakfastTime";
    final String KEY_SURVEY_QUES4 = "lunchTime";
    final String KEY_SURVEY_QUES5 = "dinnerTime";
    final String KEY_SURVEY_QUES6 = "midnightSnack";
    final String KEY_SURVEY_QUES7 = "stressLevel";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    private RadioGroup radioGroup1;
    private RadioButton rg1_checkedRadioButton;
    private RadioButton rg1_radioButton1;
    private RadioButton rg1_radioButton2;
    private RadioButton rg1_radioButton3;
    private RadioButton rg1_radioButton4;
    private RadioButton rg1_radioButton5;
    private String rg1Text;

    private RadioGroup radioGroup2;
    private RadioButton rg2_checkedRadioButton;
    private RadioButton rg2_radioButton1;
    private RadioButton rg2_radioButton2;
    private RadioButton rg2_radioButton3;
    private RadioButton rg2_radioButton4;
    private String rg2Text;

    private RadioGroup radioGroup3;
    private RadioButton rg3_checkedRadioButton;
    private RadioButton rg3_radioButton1;
    private RadioButton rg3_radioButton2;
    private RadioButton rg3_radioButton3;
    private RadioButton rg3_radioButton4;
    private RadioButton rg3_radioButton5;
    private String rg3Text;

    private RadioGroup radioGroup4;
    private RadioButton rg4_checkedRadioButton;
    private RadioButton rg4_radioButton1;
    private RadioButton rg4_radioButton2;
    private RadioButton rg4_radioButton3;
    private RadioButton rg4_radioButton4;
    private RadioButton rg4_radioButton5;
    private String rg4Text;

    private RadioGroup radioGroup5;
    private RadioButton rg5_checkedRadioButton;
    private RadioButton rg5_radioButton1;
    private RadioButton rg5_radioButton2;
    private RadioButton rg5_radioButton3;
    private RadioButton rg5_radioButton4;
    private RadioButton rg5_radioButton5;
    private String rg5Text;

    private RadioGroup radioGroup6;
    private RadioButton rg6_checkedRadioButton;
    private RadioButton rg6_radioButton1;
    private RadioButton rg6_radioButton2;
    private String rg6Text;

    private RadioGroup radioGroup7;
    private RadioButton rg7_checkedRadioButton;
    private RadioButton rg7_radioButton1;
    private RadioButton rg7_radioButton2;
    private RadioButton rg7_radioButton3;
    private RadioButton rg7_radioButton4;
    private RadioButton rg7_radioButton5;
    private String rg7Text;

    private ImageButton imageButton_Back;
    private Button buttonSubmit;
    private Button buttonCancel;
    private Map<String,Object> survey = new HashMap<>();

    private Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("UserSurvey").document(firebaseUser.getEmail());

        radioGroup1 = (RadioGroup)findViewById(R.id.survey_goToBedTime_RG);
        rg1_radioButton1 = (RadioButton)findViewById(R.id.survey_goToBedTime_before9pm);
        rg1_radioButton2 = (RadioButton)findViewById(R.id.survey_goToBedTime_between9_11pm);
        rg1_radioButton3 = (RadioButton)findViewById(R.id.survey_goToBedTime_between11_1am);
        rg1_radioButton4 = (RadioButton)findViewById(R.id.survey_goToBedTime_between1_3am);
        rg1_radioButton5 = (RadioButton)findViewById(R.id.survey_goToBedTime_after3am);

        radioGroup2 = (RadioGroup) findViewById(R.id.survey_sleepingTime_RG);
        rg2_radioButton1 = (RadioButton)findViewById(R.id.survey_sleepingTime_less4);
        rg2_radioButton2 = (RadioButton)findViewById(R.id.survey_sleepingTime_between4_6);
        rg2_radioButton3 = (RadioButton)findViewById(R.id.survey_sleepingTime_between6_8);
        rg2_radioButton4 = (RadioButton)findViewById(R.id.survey_sleepingTime_more8);

        radioGroup3 = (RadioGroup)findViewById(R.id.survey_breakfastTime_RG);
        rg3_radioButton1 = (RadioButton)findViewById(R.id.survey_breakfastTime_none);
        rg3_radioButton2 = (RadioButton)findViewById(R.id.survey_breakfastTime_before6);
        rg3_radioButton3 = (RadioButton)findViewById(R.id.survey_breakfastTime_between6_8am);
        rg3_radioButton4 = (RadioButton)findViewById(R.id.survey_breakfastTime_between8_10am);
        rg3_radioButton5 = (RadioButton)findViewById(R.id.survey_breakfastTime_after10am);

        radioGroup4 = (RadioGroup)findViewById(R.id.survey_lunchTime_RG);
        rg4_radioButton1 = (RadioButton)findViewById(R.id.survey_lunchTime_none);
        rg4_radioButton2 = (RadioButton)findViewById(R.id.survey_lunchTime_before11am);
        rg4_radioButton3 = (RadioButton)findViewById(R.id.survey_lunchTime_between11_1pm);
        rg4_radioButton4 = (RadioButton)findViewById(R.id.survey_lunchTime_between1_3pm);
        rg4_radioButton5 = (RadioButton)findViewById(R.id.survey_lunchTime_after3pm);

        radioGroup5 = (RadioGroup)findViewById(R.id.survey_dinnerTime_RG);
        rg5_radioButton1 = (RadioButton)findViewById(R.id.survey_dinnerTime_none);
        rg5_radioButton2 = (RadioButton)findViewById(R.id.survey_dinnerTime_before5pm);
        rg5_radioButton3 = (RadioButton)findViewById(R.id.survey_dinnerTime_between5_7pm);
        rg5_radioButton4 = (RadioButton)findViewById(R.id.survey_dinnerTime_between7_9pm);
        rg5_radioButton5 = (RadioButton)findViewById(R.id.survey_dinnerTime_after9pm);

        radioGroup6 = (RadioGroup)findViewById(R.id.survey_midnightSnack_RG);
        rg6_radioButton1 = (RadioButton)findViewById(R.id.survey_midnightSnack_yes);
        rg6_radioButton2 = (RadioButton)findViewById(R.id.survey_midnightSnack_no);

        radioGroup7 = (RadioGroup)findViewById(R.id.survey_stressIndex_RG);
        rg7_radioButton1 = (RadioButton)findViewById(R.id.survey_stressIndex_veryLow);
        rg7_radioButton2 = (RadioButton)findViewById(R.id.survey_stressIndex_low);
        rg7_radioButton3 = (RadioButton)findViewById(R.id.survey_stressIndex_soso);
        rg7_radioButton4 = (RadioButton)findViewById(R.id.survey_stressIndex_high);
        rg7_radioButton5 = (RadioButton)findViewById(R.id.survey_stressIndex_veryHigh);

        imageButton_Back = (ImageButton) findViewById(R.id.survey_backButton);
        buttonSubmit = (Button) findViewById(R.id.survey_submitButton);
        buttonCancel = (Button) findViewById(R.id.survey_cancelButton);

        loadUserSurvey();

        imageButton_Back.setOnClickListener(listener);
        buttonSubmit.setOnClickListener(listener);
        buttonCancel.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == imageButton_Back) {
                SurveyActivity.super.onBackPressed();
            }

            if (view == buttonSubmit) {
                survey.put(KEY_SURVEY_DONE, "1");

                String currentDate = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());

                survey.put(KEY_SURVEY_DATE, currentDate);
                ques1Check(); ques2Check(); ques3Check(); ques4Check(); ques5Check(); ques6Check(); ques7Check();

                docRef.update(survey);
                Toast.makeText(getApplicationContext(), "Your survey has been saved.", Toast.LENGTH_SHORT).show();
                finish();
            }

            if (view == buttonCancel) {
                SurveyActivity.super.onBackPressed();
            }
        }
    };


    private void loadUserSurvey(){
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String ques1Done = documentSnapshot.getData().get(KEY_SURVEY_QUES1).toString();
                if (ques1Done.equals(rg1_radioButton1.getText().toString())) rg1_radioButton1.setChecked(true);
                else if(ques1Done.equals(rg1_radioButton2.getText().toString())) rg1_radioButton2.setChecked(true);
                else if(ques1Done.equals(rg1_radioButton3.getText().toString())) rg1_radioButton3.setChecked(true);
                else if(ques1Done.equals(rg1_radioButton4.getText().toString())) rg1_radioButton4.setChecked(true);
                else if(ques1Done.equals(rg1_radioButton5.getText().toString())) rg1_radioButton5.setChecked(true);

                String ques2Done = documentSnapshot.getData().get(KEY_SURVEY_QUES2).toString();
                if (ques2Done.equals(rg2_radioButton1.getText().toString())) rg2_radioButton1.setChecked(true);
                else if(ques2Done.equals(rg2_radioButton2.getText().toString())) rg2_radioButton2.setChecked(true);
                else if(ques2Done.equals(rg2_radioButton3.getText().toString())) rg2_radioButton3.setChecked(true);
                else if(ques2Done.equals(rg2_radioButton4.getText().toString())) rg2_radioButton4.setChecked(true);

                String ques3Done = documentSnapshot.getData().get(KEY_SURVEY_QUES3).toString();
                if (ques3Done.equals(rg3_radioButton1.getText().toString())) rg1_radioButton1.setChecked(true);
                else if(ques3Done.equals(rg3_radioButton2.getText().toString())) rg3_radioButton2.setChecked(true);
                else if(ques3Done.equals(rg3_radioButton3.getText().toString())) rg3_radioButton3.setChecked(true);
                else if(ques3Done.equals(rg3_radioButton4.getText().toString())) rg3_radioButton4.setChecked(true);
                else if(ques3Done.equals(rg3_radioButton5.getText().toString())) rg3_radioButton5.setChecked(true);

                String ques4Done = documentSnapshot.getData().get(KEY_SURVEY_QUES4).toString();
                if (ques4Done.equals(rg4_radioButton1.getText().toString())) rg4_radioButton1.setChecked(true);
                else if(ques4Done.equals(rg4_radioButton2.getText().toString())) rg4_radioButton2.setChecked(true);
                else if(ques4Done.equals(rg4_radioButton3.getText().toString())) rg4_radioButton3.setChecked(true);
                else if(ques4Done.equals(rg4_radioButton4.getText().toString())) rg4_radioButton4.setChecked(true);
                else if(ques4Done.equals(rg4_radioButton5.getText().toString())) rg4_radioButton5.setChecked(true);

                String ques5Done = documentSnapshot.getData().get(KEY_SURVEY_QUES5).toString();
                if (ques5Done.equals(rg5_radioButton1.getText().toString())) rg5_radioButton1.setChecked(true);
                else if(ques5Done.equals(rg5_radioButton2.getText().toString())) rg5_radioButton2.setChecked(true);
                else if(ques5Done.equals(rg5_radioButton3.getText().toString())) rg5_radioButton3.setChecked(true);
                else if(ques5Done.equals(rg5_radioButton4.getText().toString())) rg5_radioButton4.setChecked(true);
                else if(ques5Done.equals(rg5_radioButton5.getText().toString())) rg5_radioButton5.setChecked(true);

                String ques6Done = documentSnapshot.getData().get(KEY_SURVEY_QUES6).toString();
                if (ques6Done.equals(rg6_radioButton1.getText().toString())) rg6_radioButton1.setChecked(true);
                else if(ques6Done.equals(rg6_radioButton2.getText().toString())) rg6_radioButton2.setChecked(true);

                String ques7Done = documentSnapshot.getData().get(KEY_SURVEY_QUES7).toString();
                if (ques7Done.equals(rg7_radioButton1.getText().toString())) rg7_radioButton1.setChecked(true);
                else if(ques7Done.equals(rg7_radioButton2.getText().toString())) rg7_radioButton2.setChecked(true);
                else if(ques7Done.equals(rg7_radioButton3.getText().toString())) rg7_radioButton3.setChecked(true);
                else if(ques7Done.equals(rg7_radioButton4.getText().toString())) rg7_radioButton4.setChecked(true);
                else if(ques7Done.equals(rg7_radioButton5.getText().toString())) rg7_radioButton5.setChecked(true);
            }
        });
    }

    private void ques1Check(){
        int ansId = radioGroup1.getCheckedRadioButtonId();
        rg1_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg1Text = rg1_checkedRadioButton.getText().toString();

        survey.put("goToBed", rg1Text);
    }

    private void ques2Check(){
        int ansId = radioGroup2.getCheckedRadioButtonId();
        rg2_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg2Text = rg2_checkedRadioButton.getText().toString();

        survey.put("sleepingTime", rg2Text);
    }

    private void ques3Check(){
        int ansId = radioGroup3.getCheckedRadioButtonId();
        rg3_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg3Text = rg3_checkedRadioButton.getText().toString();

        survey.put("breakfastTime", rg3Text);
    }
    private void ques4Check(){
        int ansId = radioGroup4.getCheckedRadioButtonId();
        rg4_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg4Text = rg4_checkedRadioButton.getText().toString();

        survey.put("lunchTime", rg4Text);
    }
    private void ques5Check(){
        int ansId = radioGroup5.getCheckedRadioButtonId();
        rg5_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg5Text = rg5_checkedRadioButton.getText().toString();

        survey.put("dinnerTime", rg5Text);
    }
    private void ques6Check(){
        int ansId = radioGroup6.getCheckedRadioButtonId();
        rg6_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg6Text = rg6_checkedRadioButton.getText().toString();

        survey.put("midnightSnack", rg6Text);
    }
    private void ques7Check(){
        int ansId = radioGroup7.getCheckedRadioButtonId();
        rg7_checkedRadioButton = (RadioButton) findViewById(ansId);
        rg7Text = rg7_checkedRadioButton.getText().toString();

        survey.put("stressLevel", rg7Text);
    }

    private void Confirm_Cancel() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SurveyActivity.this);
        alert_confirm.setMessage("All of modified information will be erased.\nAll you sure you want to exit?").setCancelable(true).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SurveyActivity.super.onBackPressed();
                    }
                });

        alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SurveyActivity.this, "Canceled", Toast.LENGTH_LONG).show();
            }
        });
        alert_confirm.show();
    }

    @Override
    public void onBackPressed(){
        Confirm_Cancel();
    }
}