package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // setup variables
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        // setup login button behavior
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> login());
    }

    /*
        helper func to launch main activity
     */
    private void launchMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    /*
        helper func to check email validity
     */
    private Boolean checkEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
        helper func to check password length sufficient
     */
    private Boolean checkPassword(String password) {
        return password.length() > 5;
    }

    /*
        handle login:
            - sign in if account exists
            - ask to create account if account does not exist
     */
    private void login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!checkEmail(email) || password.isEmpty()) {
            Log.d("LoginActivity", "Incorrect info");
            Toast.makeText(this, "Invalid email or password, try again",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!checkPassword(password)) {
            Log.d("LoginActivity", "Invalid password");
            Toast.makeText(this, "Error: Password must be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LoginActivity", "Attempting login");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d("LoginActivity", "Sign in successful, launching MainActivity");
                Toast.makeText(this, "Signed in with email: " + email, Toast.LENGTH_SHORT).show();
                launchMainActivity();
            } else {
                Log.d("LoginActivity", "Account does not exist, asking to create");
                createAccount(email, password);
            }
        });

    }

    /*
        show a dialog if user wants to create account:
            yes: create account and send to main activity
            no: do nothing
     */
    private void createAccount(String email, String password) {
        new AlertDialog.Builder(this)
                .setTitle("Create Account")
                .setMessage("There is no existing user with this email and password, would you " +
                        "like to create a new account?")
                .setPositiveButton("Create", ((dialog, which) -> {

                    // attempting to create an account
                    Log.d("LoginActivity", "Attempting account creation");
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                        // if successful, launch MainActivity
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "Account created");
                            Toast.makeText(this, "Created new account with email: "
                                    + email, Toast.LENGTH_SHORT).show();
                            launchMainActivity();
                        } else {
                            Log.d("LoginActivity", "Error creating account");
                            Toast.makeText(this, "Error creating account, " +
                                    "please try again (check password)?", Toast.LENGTH_LONG).show();
                        }
                    });

                }))
                .setNegativeButton("Cancel", null)
                .show();
    }
}