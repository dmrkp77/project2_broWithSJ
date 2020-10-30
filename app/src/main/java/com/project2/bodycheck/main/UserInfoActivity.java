package com.project2.bodycheck.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project2.bodycheck.R;

public class UserInfoActivity extends AppCompatActivity {
    private final String TAG = "UserInfoModifyActivity";

    private final String KEY_USERNAME = "username";
    private final String KEY_PASSWORD = "password";
    private final String KEY_REGISTERDATE = "registerDate";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    private EditText username;
    private EditText password;
    private TextView registerDate;
    private Button saveButton;
    private ImageButton backButton;

    private String oldName;
    private String newName;
    private String oldPassword;
    private String newPassword;
    private Boolean nameUpCheck = false;
    private Boolean pwUpCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User").document(firebaseUser.getEmail());

        username = (EditText) findViewById(R.id.infomodify_userNameEditText);
        password = (EditText) findViewById(R.id.infomodify_passwordEditText);
        registerDate = (TextView) findViewById(R.id.infomodify_registerDate);
        saveButton = (Button) findViewById(R.id.infomodify_saveButton);
        backButton = (ImageButton) findViewById(R.id.infomodify_backButton);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            username.setText(documentSnapshot.getString(KEY_USERNAME));
                            password.setText(documentSnapshot.getString(KEY_PASSWORD));
                            registerDate.setText(documentSnapshot.getString(KEY_REGISTERDATE));

                            oldName = documentSnapshot.getString(KEY_USERNAME);
                            oldPassword = documentSnapshot.getString(KEY_PASSWORD);
                        } else {
                            Toast.makeText(getApplicationContext(), "User Info doesn't exist.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "User Info Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                   }
                });

        backButton.setOnClickListener(listener);
        saveButton.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == backButton) {
                finish();
            }
            if (view == saveButton) {
                usernameUpdate();       //username 변경 메소드, 변경 값 없을시 nameUpCheck값 true로 변환
                passwordUpdate();       //password 변경 메소드, 변경 값 없을시 pwUpCheck값 true로 변환

                //바뀐 값이 없을시 toast message 출력
                if (nameUpCheck == true && pwUpCheck == true) {
                    Toast.makeText(getApplicationContext(), "Nothing has changed.", Toast.LENGTH_SHORT).show();
                }

                //Boolean값 초기화
                nameUpCheck = false;
                pwUpCheck = false;
            }
        }
    };

    protected void usernameUpdate() {
        if (!oldName.equals(username.getText().toString())) {
            if (username.getText().toString().length() < 2) {
                Toast.makeText(getApplicationContext(), "Please enter at least 2 letters.", Toast.LENGTH_SHORT).show();
            } else {
                docRef  //데이터베이스에도 바뀐 username으로 업데이트
                        .update("username", username.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, " \"User Name\" has changed!");
                                Toast.makeText(getApplicationContext(), "\"User Name\"has changed!", Toast.LENGTH_SHORT).show();
                                getUserName();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, " \"User Name\" change has failed.", e);
                                Toast.makeText(getApplicationContext(), "\"User Name\" change has failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            nameUpCheck = true;
        }
    }

    protected void passwordUpdate() {
        if (!oldPassword.equals(password.getText().toString())) {
            if (password.getText().toString().length() < 6) {
                Toast.makeText(getApplicationContext(), "Please enter at least 6 digits.", Toast.LENGTH_SHORT).show();
            } else {
                // 사용자 재인증 및 비밀번호 변경
                passwordReauthAndModify();
                docRef  //데이터베이스에도 바뀐 비밀번호로 업데이트
                        .update("password", password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, " \"Password\"has changed!");
                                Toast.makeText(getApplicationContext(), "\"Password\"has changed!", Toast.LENGTH_SHORT).show();
                                getPassword();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, " \"Password\"change has failed.", e);
                                Toast.makeText(getApplicationContext(), "\"Password\"change has failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            pwUpCheck = true;
        }
    }

    //password 재인증 및 수정
    protected void passwordReauthAndModify() {
        AuthCredential credential = EmailAuthProvider
                .getCredential(firebaseUser.getEmail(), oldPassword);

        // 사용자 재인증
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        newPassword = password.getText().toString();
                        if (newPassword.length() < 6) { //비밀번호 6자리 이하 서버 인증 불가
                            Log.d(TAG, "Please enter at least 6 digits.");
                        } else { //사용자 비밀번호 변경
                            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Your password has been changed.");
                                    }
                                    if (task.isCanceled()) {
                                        Log.d(TAG, "Changing password has canceled.");
                                        return;
                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });
                        }
                    }
                });
    }

    public void getUserName() {
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            oldName = documentSnapshot.getString(KEY_USERNAME);
                        }
                    }
                });
    }

    public void getPassword() {
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            oldPassword = documentSnapshot.getString(KEY_PASSWORD);
                        }
                    }
                });
    }
}