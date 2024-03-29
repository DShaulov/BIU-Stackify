package com.example.stackify;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private String userEmail;
    private String userPassword;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth auth;
    private ProgressBar loginProgressBar;
    private Button loginBtn;
    private Button registerBtn;
    private DatabaseReference databaseReference;
    private SolutionDao solutionDao;
    private AppDB db;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = "";
        userPassword = "";
        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        auth = FirebaseAuth.getInstance();
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginProgressBar.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        gson = new Gson();

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
    }

    public void authenticateUser() {
        loginProgressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        loginBtn.setEnabled(false);

        userEmail = emailEditText.getText().toString();
        userPassword = passwordEditText.getText().toString();

        if (userEmail.equals("") || userPassword.equals("")) {
            Toast.makeText(LoginActivity.this, "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show();
            resetUI();
            return;
        }
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            fetchPreviousSolutions(user.getUid());
                            resetUI();
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            resetUI();
                        }
                    }
                });
    }

    /**
     * Fetches user data from firebase realtime database
     */
    public void fetchPreviousSolutions(String userId) {
        databaseReference.child("users").child(userId).child("solutions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    solutionDao.deleteAll();
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String key = child.getKey();
                        String value = child.getValue().toString();
                        Solution solFromJson = gson.fromJson(value, Solution.class);
                        solutionDao.insert(solFromJson);
                        System.out.println(solFromJson);
                    }
                }
            }
        });
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    };

    /**
     * Resets the UI after a failed login attempt
     */
    public void resetUI() {
        loginProgressBar.setVisibility(View.INVISIBLE);
        registerBtn.setEnabled(true);
        loginBtn.setEnabled(true);
    }
}