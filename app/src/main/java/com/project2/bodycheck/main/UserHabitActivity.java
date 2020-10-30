package com.project2.bodycheck.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project2.bodycheck.PreferenceManager;
import com.project2.bodycheck.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserHabitActivity extends AppCompatActivity {
    private final String TAG = "UserHabitActivity";

    private final String KEY_HABIT_SCORE = "habitScore";
    private final String KEY_HABIT_DONE ="habitDone";
    private final String KEY_SMOKING = "smoking_check";
    private final String KEY_WORKOUT = "workout_check";
    private final String KEY_VITAMIN = "vitamin_check";
    private final String KEY_OMEGA = "omega_check";
    private final String KEY_LUTEIN = "lutein_check";
    private final String KEY_ANTIOXIDANT = "antioxidant_check";
    private final String KEY_PROBIOTICS = "probiotics_check";
    private final String KEY_ETC = "etc_check";

    private RadioGroup radioGroup_smoking;
    private RadioButton smokingRadioButton;
    private RadioButton smokingRadioButton_YES;
    private RadioButton smokingRadioButton_NO;
    private String smokingText;

    private RadioGroup radioGroup_workout;
    private RadioButton workoutRadioButton;
    private RadioButton workoutRadioButton_Never;
    private RadioButton workoutRadioButton_2times;
    private RadioButton workoutRadioButton_4times;
    private RadioButton workoutRadioButton_5times;
    private String workoutText;

    private ImageButton backButton;
    private Button saveButton;

    private CheckBox vitaminCheckBox;
    private CheckBox omegaCheckBox;
    private CheckBox luteinCheckBox;
    private CheckBox antioxidantCheckBox;
    private CheckBox probioticsCheckBox;
    private CheckBox etcCheckBox;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    private String smokedone;
    private String workoutdone;

    Map<String, Object> habit = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_habit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("UserHabit").document(firebaseUser.getEmail());

        radioGroup_smoking = (RadioGroup) findViewById(R.id.userhabit_ques_smoking_RG);
        smokingRadioButton_YES = (RadioButton) findViewById(R.id.userhabit_Ans_smoking_YES);
        smokingRadioButton_NO = (RadioButton) findViewById(R.id.userhabit_Ans_smoking_NO);

        radioGroup_workout = (RadioGroup) findViewById(R.id.userhabit_ques_workout_RG);
        workoutRadioButton_Never = (RadioButton) findViewById(R.id.userhabit_Ans_workout_Never);
        workoutRadioButton_2times = (RadioButton) findViewById(R.id.userhabit_Ans_workout_Seldom);
        workoutRadioButton_4times = (RadioButton) findViewById(R.id.userhabit_Ans_workout_Often);
        workoutRadioButton_5times = (RadioButton) findViewById(R.id.userhabit_Ans_workout_Always);

        vitaminCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_Vitamin);
        omegaCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_Omega3);
        luteinCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_Lutein);
        antioxidantCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_Antioxidant);
        probioticsCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_Probiotics);
        etcCheckBox = (CheckBox) findViewById(R.id.userhabit_Ans_supplement_etc);

        backButton = (ImageButton) findViewById(R.id.userhabit_backButton);
        saveButton = (Button) findViewById(R.id.userhabit_saveButton);

        loadUserHabit();

        backButton.setOnClickListener(listener);
        saveButton.setOnClickListener(listener);


    }

    @Override
    public void onStop() {
        super.onStop();
        //갑자기 멈추었을 때 설문정보 저장
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == backButton) {
                finish();
            }
            if (view == saveButton) {
                smokingCheck();
                suppleCheck();
                workOutCheck();

                habit.put(KEY_HABIT_DONE,"1");
                docRef.update(habit);
                Toast.makeText(getApplicationContext(), "Your habit has been saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private void loadUserHabit() {
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    smokedone = documentSnapshot.getData().get(KEY_SMOKING).toString();
                    if (smokedone.equals("YES"))
                        smokingRadioButton_YES.setChecked(true);
                    else if (smokedone.equals("NO"))
                        smokingRadioButton_NO.setChecked(true);

                    if (documentSnapshot.getData().get(KEY_VITAMIN).toString().equals("1")) vitaminCheckBox.setChecked(true);
                    else vitaminCheckBox.setChecked(false);

                    if (documentSnapshot.getData().get(KEY_OMEGA).toString().equals("1")) omegaCheckBox.setChecked(true);
                    else omegaCheckBox.setChecked(false);

                    if (documentSnapshot.getData().get(KEY_LUTEIN).toString().equals("1")) luteinCheckBox.setChecked(true);
                    else luteinCheckBox.setChecked(false);

                    if (documentSnapshot.getData().get(KEY_ANTIOXIDANT).toString().equals("1")) antioxidantCheckBox.setChecked(true);
                    else antioxidantCheckBox.setChecked(false);

                    if (documentSnapshot.getData().get(KEY_PROBIOTICS).toString().equals("1")) probioticsCheckBox.setChecked(true);
                    else probioticsCheckBox.setChecked(false);

                    if (documentSnapshot.getData().get(KEY_ETC).toString().equals("1")) etcCheckBox.setChecked(true);
                    else etcCheckBox.setChecked(false);

                    workoutdone = documentSnapshot.getData().get(KEY_WORKOUT).toString();
                    if (workoutdone.equals("Never"))
                        workoutRadioButton_Never.setChecked(true);
                    else if (workoutdone.equals("1~2times"))
                        workoutRadioButton_2times.setChecked(true);
                    else if (workoutdone.equals("3~4times"))
                        workoutRadioButton_4times.setChecked(true);
                    else if (workoutdone.equals("5times or more"))
                        workoutRadioButton_5times.setChecked(true);
            }
        });
    }


    private void smokingCheck() {
        int smokingId = radioGroup_smoking.getCheckedRadioButtonId();
        smokingRadioButton = (RadioButton) findViewById(smokingId);
        smokingText = smokingRadioButton.getText().toString();

        habit.put("smoking_check", smokingText);
    }

    private void suppleCheck() {
        if (vitaminCheckBox.isChecked()) habit.put("vitamin_check", "1");
        else habit.put("vitamin_check", "0");

        if (omegaCheckBox.isChecked()) habit.put("omega_check", "1");
        else habit.put("omega_check", "0");

        if (luteinCheckBox.isChecked()) habit.put("lutein_check", "1");
        else habit.put("lutein_check", "0");

        if (antioxidantCheckBox.isChecked()) habit.put("antioxidant_check", "1");
        else habit.put("antioxidant_check", "0");

        if (probioticsCheckBox.isChecked()) habit.put("probiotics_check", "1");
        else habit.put("probiotics_check", "0");

        if (etcCheckBox.isChecked()) habit.put("etc_check", "1");
        else habit.put("etc_check", "0");
    }

    private void workOutCheck() {
        int workoutId = radioGroup_workout.getCheckedRadioButtonId();
        workoutRadioButton = (RadioButton) findViewById(workoutId);
        workoutText = workoutRadioButton.getText().toString();

        habit.put("workout_check", workoutText);
    }
}