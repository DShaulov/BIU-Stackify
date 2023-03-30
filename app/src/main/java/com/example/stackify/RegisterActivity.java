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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private String userEmail;
    private String userPassword;
    private String userConfirmPassword;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerBtn;
    private FirebaseAuth auth;
    private Toast emptyInputsToast;
    private Toast passwordMismatchToast;
    private Toast passwordTooShortToast;
    private Toast emailTakenToast;
    private Toast emailInvalidToast;
    private ProgressBar registerProgressBar;
    private DatabaseReference databaseReference;
    private SolutionDao solutionDao;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();

        userEmail = "";
        userPassword = "";
        userConfirmPassword = "";
        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.registerConfirmPasswordEditText);
        registerProgressBar = findViewById(R.id.registerProgressBar);
        registerProgressBar.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        emptyInputsToast = Toast.makeText(RegisterActivity.this, "Fields Cannot Empty", Toast.LENGTH_SHORT);
        passwordMismatchToast = Toast.makeText(RegisterActivity.this, "Password Don't Match", Toast.LENGTH_SHORT);
        passwordTooShortToast = Toast.makeText(RegisterActivity.this, "Password Must Be 6 Or More Digits", Toast.LENGTH_SHORT);
        emailTakenToast = Toast.makeText(RegisterActivity.this, "Email Already Taken", Toast.LENGTH_SHORT);
        emailInvalidToast = Toast.makeText(RegisterActivity.this, "Email Is Invalid", Toast.LENGTH_SHORT);

        registerBtn = findViewById(R.id.registerSubmitBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        auth = FirebaseAuth.getInstance();
    }

    /**
     * Checks user input and if valid creates a new user.
     */
    public void createNewUser() {
        registerProgressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);

        userEmail = emailEditText.getText().toString();
        userPassword = passwordEditText.getText().toString();
        userConfirmPassword = confirmPasswordEditText.getText().toString();

        if (userEmail.equals("") || userPassword.equals("") || userConfirmPassword.equals("")) {
            emptyInputsToast.show();
            resetUI();
            return;
        }
        if (!userPassword.equals(userConfirmPassword)) {
            passwordMismatchToast.show();
            resetUI();
            return;
        }
        if (userPassword.length() < 6) {
            passwordTooShortToast.show();
            resetUI();
            return;
        }
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    saveUser(user.getUid());
                    solutionDao.deleteAll();
                    resetUI();
                    startMainActivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        emailInvalidToast.show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        emailTakenToast.show();
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    resetUI();
                }
            }
        });
    }

    /**
     * Saves the user in the firebase database
     */
    public void saveUser(String userId) {
        databaseReference.child("users").child(userId).child("email").setValue(userEmail);
        databaseReference.child("users").child(userId).child("solutions").setValue("");
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Resets the UI after a failed register attempt
     */
    public void resetUI() {
        registerProgressBar.setVisibility(View.INVISIBLE);
        registerBtn.setEnabled(true);
    }

}