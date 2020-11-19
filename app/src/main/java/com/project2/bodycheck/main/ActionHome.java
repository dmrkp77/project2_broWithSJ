package com.project2.bodycheck.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project2.bodycheck.PreferenceManager;
import com.project2.bodycheck.R;
import com.project2.bodycheck.calendar.CalendarActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

public class ActionHome extends Fragment {
    private final String TAG = "ActionHome";

    private final String KEY_HABIT_DONE = "habitDone";

    private final String KEY_SURVEY_DONE = "surveyDone";

    ContentActivity activity;

    public ActionHome(Context context) {
        this.activity = (ContentActivity) context;
    }

    ViewGroup viewGroup;
    private Button surveyButton;
    private Button surveyButton2;
    private Button calenderButton;
    private Button todolistButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef_Survey;
    private DocumentReference docRef_Habit;

    private long backKeyPressedTime = 0;
    private PreferenceManager util;
    private TextView textView_beforeSurveyDate;
    private TextView textView_surveyDate;
    private TextView textView_totalScore;
    int habitScore = 0, surveyScore = 0;

    int score1, score2;
    private PieChart pieChart;

    private BarChart barChart;

    //뷰 페이저
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    //뷰 페이저 자동 넘김
    private  int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 500;
    private final long PERIOD_MS = 3000;
    private final Handler handler = new Handler();
    private final Runnable Update = new Runnable() {
        @Override
        public void run() {
            //이미지 개수 만큼 숫자 조절 필요
            if(currentPage == 2) { currentPage = 0; }
            viewPager.setCurrentItem(currentPage++, true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.adapter = new ViewPagerAdapter(getChildFragmentManager());
        //페이지 넘김 시간 조절
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        CalculateHabitScore();
        setBarChart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        surveyButton = (Button) viewGroup.findViewById(R.id.home_surveyButton);
        surveyButton2 = (Button) viewGroup.findViewById(R.id.home_surveyButton2);
        calenderButton = (Button) viewGroup.findViewById(R.id.home_calenderBtn);
        todolistButton = (Button) viewGroup.findViewById(R.id.home_todolistBtn);

        textView_totalScore = (TextView) viewGroup.findViewById(R.id.home_scoreView);
        textView_surveyDate = (TextView) viewGroup.findViewById(R.id.home_surveyDate);
        textView_beforeSurveyDate = (TextView) viewGroup.findViewById(R.id.home_beforeSurveyDate);
        pieChart = (PieChart) viewGroup.findViewById(R.id.home_pieChart);
        barChart = (BarChart) viewGroup.findViewById(R.id.home_barChart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        docRef_Survey = db.collection("UserSurvey").document(firebaseUser.getEmail());
        docRef_Habit = db.collection("UserHabit").document(firebaseUser.getEmail());
        CalculateHabitScore();
        setBarChart();

        surveyButton.setOnClickListener(listener);
        surveyButton2.setOnClickListener(listener);
        calenderButton.setOnClickListener(listener);
        todolistButton.setOnClickListener(listener);

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        //뷰 페이저
        this.viewPager = (ViewPager) view.findViewById(R.id.home_viewPager);
        this.viewPager.setAdapter(adapter);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == surveyButton) {
                startActivity(new Intent(getContext(), SurveyActivity.class));
            }
            if (view == surveyButton2) {
                startActivity(new Intent(getContext(), SurveyActivity.class));
            }
            if(view == calenderButton){
                startActivity(new Intent(getContext(), CalendarActivity.class));
            }
            if(view == todolistButton) {
                startActivity(new Intent(getContext(), ToDoListChecking.class));
            }
        }
    };

    public void CalculateHabitScore() {
        docRef_Habit.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                String habitDone = documentSnapshot.getData().get(KEY_HABIT_DONE).toString();
                if (habitDone.equals("1")) {
                    int smokingScore = 0, supplementScore = 0, workoutScore = 0;

                    if (documentSnapshot.getData().get("smoking_check").toString().equals("YES"))
                        smokingScore = -10;
                    else smokingScore = 10;

                    int supplementCnt = 0;
                    if (documentSnapshot.getData().get("vitamin_check").toString().equals("1"))
                        supplementCnt++;
                    if (documentSnapshot.getData().get("omega_check").toString().equals("1"))
                        supplementCnt++;
                    if (documentSnapshot.getData().get("lutein_check").toString().equals("1"))
                        supplementCnt++;
                    if (documentSnapshot.getData().get("antioxidant_check").toString().equals("1"))
                        supplementCnt++;
                    if (documentSnapshot.getData().get("probiotics_check").toString().equals("1"))
                        supplementCnt++;
                    if (documentSnapshot.getData().get("etc_check").toString().equals("1"))
                        supplementCnt++;
                    supplementScore = supplementCnt * 5;

                    if (documentSnapshot.getData().get("workout_check").toString().equals("Never"))
                        workoutScore = 0;
                    else if (documentSnapshot.getData().get("workout_check").toString().equals("1~2times"))
                        workoutScore = 10;
                    else if (documentSnapshot.getData().get("workout_check").toString().equals("3~4times"))
                        workoutScore = 15;
                    else if (documentSnapshot.getData().get("workout_check").toString().equals("5times or more"))
                        workoutScore = 20;

                    habitScore = smokingScore + supplementScore + workoutScore;

//                    UpdateBoard();
                }
                score1 = habitScore;
                CalculateSurveyScore();
            }
        });

        return;
    }

    private void CalculateSurveyScore() {
        docRef_Survey.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                String surveyDone = documentSnapshot.getData().get(KEY_SURVEY_DONE).toString();
                if (surveyDone.equals("0")) {
//                    textView_todayBoard.setText("You have not conducted today's survey.\nPlease conduct it first.");
//                    textView_todayBoard.setTextSize(20);
//                    textView_totalScore.setText("The socre will be displayed after conducting a survey.");
//                    textView_totalScore.setTextSize(20);
//
//                    textView_beforeSurveyDate.setText("Please conduct a survey.");
                } else {
                    int goToBedScore = 0;
                    if (documentSnapshot.getData().get("goToBed").toString().equals("before 9pm"))
                        goToBedScore = 5;
                    else if (documentSnapshot.getData().get("goToBed").toString().equals("9pm - 11pm"))
                        goToBedScore = 4;
                    else if (documentSnapshot.getData().get("goToBed").toString().equals("11pm - 1am"))
                        goToBedScore = 3;
                    else if (documentSnapshot.getData().get("goToBed").toString().equals("1am - 3am"))
                        goToBedScore = 2;
                    else if (documentSnapshot.getData().get("goToBed").toString().equals("after 3am"))
                        goToBedScore = 1;

                    int sleepingTimeScore = 0;
                    if (documentSnapshot.getData().get("sleepingTime").toString().equals("less than 4 hours"))
                        sleepingTimeScore = 1;
                    else if (documentSnapshot.getData().get("sleepingTime").toString().equals("4 to 6 hours"))
                        sleepingTimeScore = 2;
                    else if (documentSnapshot.getData().get("sleepingTime").toString().equals("6 to 8 hours"))
                        sleepingTimeScore = 4;
                    else if (documentSnapshot.getData().get("sleepingTime").toString().equals("more than 8 hours"))
                        sleepingTimeScore = 5;

                    int breakfastScore = 0;
                    if (documentSnapshot.getData().get("breakfastTime").toString().equals("Didn't have"))
                        breakfastScore = 1;
                    else if (documentSnapshot.getData().get("breakfastTime").toString().equals("before 6am"))
                        breakfastScore = 4;
                    else if (documentSnapshot.getData().get("breakfastTime").toString().equals("6am - 8am"))
                        breakfastScore = 5;
                    else if (documentSnapshot.getData().get("breakfastTime").toString().equals("8am - 10am"))
                        breakfastScore = 3;
                    else if (documentSnapshot.getData().get("breakfastTime").toString().equals("after 10am"))
                        breakfastScore = 2;

                    int lunchScore = 0;
                    if (documentSnapshot.getData().get("lunchTime").toString().equals("Didn't have"))
                        lunchScore = 1;
                    else if (documentSnapshot.getData().get("lunchTime").toString().equals("before 11am"))
                        lunchScore = 4;
                    else if (documentSnapshot.getData().get("lunchTime").toString().equals("11am - 1pm"))
                        lunchScore = 5;
                    else if (documentSnapshot.getData().get("lunchTime").toString().equals("1pm - 3pm"))
                        lunchScore = 3;
                    else if (documentSnapshot.getData().get("lunchTime").toString().equals("after 3pm"))
                        lunchScore = 2;

                    int dinnerScore = 0;
                    if (documentSnapshot.getData().get("dinnerTime").toString().equals("Didn't have"))
                        dinnerScore = 1;
                    else if (documentSnapshot.getData().get("dinnerTime").toString().equals("before 5pm"))
                        dinnerScore = 4;
                    else if (documentSnapshot.getData().get("dinnerTime").toString().equals("5pm - 7pm"))
                        dinnerScore = 5;
                    else if (documentSnapshot.getData().get("dinnerTime").toString().equals("7pm - 9pm"))
                        dinnerScore = 4;
                    else if (documentSnapshot.getData().get("dinnerTime").toString().equals("after 9pm"))
                        dinnerScore = 2;

                    int midnightSnackScore = 0;
                    if (documentSnapshot.getData().get("midnightSnack").toString().equals("Yes"))
                        midnightSnackScore = 0;
                    else if (documentSnapshot.getData().get("midnightSnack").toString().equals("No"))
                        midnightSnackScore = 5;

                    int stressLevelScore = 0;
                    if (documentSnapshot.getData().get("stressLevel").toString().equals("Very Low"))
                        stressLevelScore = 10;
                    else if (documentSnapshot.getData().get("stressLevel").toString().equals("Low"))
                        stressLevelScore = 7;
                    else if (documentSnapshot.getData().get("stressLevel").toString().equals("So-so"))
                        stressLevelScore = 5;
                    else if (documentSnapshot.getData().get("stressLevel").toString().equals("High"))
                        stressLevelScore = 2;
                    else if (documentSnapshot.getData().get("stressLevel").toString().equals("Very High"))
                        stressLevelScore = 0;

                    surveyScore = goToBedScore + sleepingTimeScore + breakfastScore + lunchScore + dinnerScore + midnightSnackScore + stressLevelScore;

//                    UpdateBoard();

                    textView_beforeSurveyDate.setText("You did it on ");

                    String surveyDate = documentSnapshot.getData().get("surveyDate").toString();
                    String printSurveyDate = surveyDate.substring(5, 10);
                    textView_surveyDate.setText(printSurveyDate);

                }
                score2 = surveyScore;

                ArrayList<PieEntry> values = new ArrayList<>();
                values.add(new PieEntry((float) score1, "Habit"));
                values.add(new PieEntry((float) score2, "Survey"));

                PieDataSet pieDataSet = new PieDataSet(values, "");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Score");
                pieChart.animate();
            }
        });
        return;
    }

    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsNames;
    ArrayList<ChartDateData> chartDateDataArrayList = new ArrayList<>();

    private void setBarChart() {
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();
        fillChartDate();
        for(int i = 0; i < chartDateDataArrayList.size(); i++) {
            String dates = chartDateDataArrayList.get(i).getDates();
            int scores = chartDateDataArrayList.get(i).getScores();
            barEntryArrayList.add(new BarEntry(i, scores));
            labelsNames.add(dates);
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Dates");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Dates");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(270);
        barChart.invalidate();
    }

    private void fillChartDate() {
        chartDateDataArrayList.clear();
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
        chartDateDataArrayList.add(new ChartDateData("xx/xx", 50));
    }

//    public void UpdateBoard() {
//        int totalScore = surveyScore + habitScore;
//        textView_totalScore.setText(String.valueOf(totalScore));
//        textView_totalScore.setTextSize(24);
//
//        if (totalScore < 25)
//            textView_todayBoard.setText("Dangerous condition like hanging on a cliff.\nLove yourself!");
//        else if (totalScore < 50)
//            textView_todayBoard.setText("Standard physical condition.\nLet's try a little more!");
//        else if (totalScore < 75)
//            textView_todayBoard.setText("It is in a good shape.\nOnly one step to Perfection!");
//        else textView_todayBoard.setText("The best.\nAll you the god of health?");
//
//        textView_todayBoard.setTextSize(24);
//    }
}