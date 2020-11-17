package com.project2.bodycheck.main;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project2.bodycheck.PreferenceManager;
import com.project2.bodycheck.R;
import com.project2.bodycheck.login.LoginActivity;


import javax.annotation.Nullable;

public class ActionUser extends Fragment {
    private final String TAG = "ActionUser";

    ContentActivity activity;
    ViewGroup viewGroup;

    public ActionUser(Context context) {
        this.activity = (ContentActivity) context;
    }

    public int withdrawal_check = 0;

    private TextView userName;
    private TextView registerDate;
    private ImageView imageWithDrawal;
    private Button buttonUserInfo;
    private Button buttonUserHabit;
    private Button buttonToDoList;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    PreferenceManager util;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);
        util = new PreferenceManager(activity);
        userName = (TextView) viewGroup.findViewById(R.id.userinfo_userName);
        registerDate = (TextView) viewGroup.findViewById(R.id.userinfo_registerDate);
        imageWithDrawal = (ImageView) viewGroup.findViewById(R.id.userinfo_withdrawal);
        buttonUserInfo = (Button) viewGroup.findViewById(R.id.userinfo_infoModify);
        buttonUserHabit = (Button) viewGroup.findViewById(R.id.userinfo_userHabit);
        buttonToDoList = (Button) viewGroup.findViewById(R.id.userinfo_toDoList);

        setting = activity.getSharedPreferences("setting", 0);
        editor = setting.edit();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User").document(firebaseUser.getEmail());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (withdrawal_check == 1) return;
                userName.setText("Hello, " + documentSnapshot.getData().get("username").toString());

                String date = documentSnapshot.getData().get("registerDate").toString();
                String printDate = date.substring(2, 10);
                registerDate.setText("Register Date : " + printDate);
            }
        });

        imageWithDrawal.setOnClickListener(listener);
        buttonUserInfo.setOnClickListener(listener);
        buttonUserHabit.setOnClickListener(listener);
        buttonToDoList.setOnClickListener(listener);
        return viewGroup;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == buttonUserInfo) {
                activity.startActivity(new Intent(getContext(), UserInfoActivity.class));
            }
            if (view == buttonUserHabit) {
                activity.startActivity(new Intent(getContext(), UserHabitActivity.class));
            }
            if (view == buttonToDoList) {
                activity.startActivity(new Intent(getContext(), TodoListActivity.class));
            }
            if(view == imageWithDrawal){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("All you sure you want to delete your account?").setCancelable(true).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteUserInfo();
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Delete your account successfully", Toast.LENGTH_LONG).show();

                                        editor.clear();
                                        editor.commit();
                                        activity.Logout();
                                    }
                                });
                            }
                        });

                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Canceled", Toast.LENGTH_LONG).show();
                    }
                });
                alert_confirm.show();
            }

        }
    };

    public void deleteUserInfo() {
        withdrawal_check = 1;

        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "'User'Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting 'User'Document", e);
                    }
                });

        db.collection("UserHabit").document(firebaseUser.getEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "'UserHabit'Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting 'UserHabit'document", e);
                    }
                });

        db.collection("UserSurvey").document(firebaseUser.getEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "'UserSurvey'Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting 'UserSurvey'document", e);
                    }
                });
    }
}