package com.example.socialmediamobileqev;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText EmailEditText, passwordEditText;
    TextView signUPTextView;
    Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Variable Firebase and connect it with Database
        mAuth = FirebaseAuth.getInstance();

        // check if user sign in before or first time
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        }


        // Add underline for Sign UP textView Programtically
        signUPTextView = (TextView) findViewById(R.id.signUP_TextView);
        signUPTextView.setPaintFlags(signUPTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // EditText Intialaizing
        EmailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);


        // Login Button Intialiazing
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUsers();

            }


        });


        // SignUP Button Intialiazing
        signUPTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);

            }
        });
        // Initialize Progress Bar
        progressBar = findViewById(R.id.progressBarLogin);

    }

    private void loginUsers() {

        // Get Data From EditText
        String email = EmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            EmailEditText.setError("Required");
            EmailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Required");
            passwordEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailEditText.setError(" Email not valid");
            EmailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //redirect User to profile
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Please Check your email and pasword  ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

    }


}