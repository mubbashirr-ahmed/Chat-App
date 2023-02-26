package com.example.socialmediamobileqev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView loginTextView;
    Button register;
    EditText yourName, emailEditText, passwordEditText, rePasswordEditText;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Add underline for Sign UP textView Programtically
        loginTextView = findViewById(R.id.loginRegister_TextView);
        loginTextView.setPaintFlags(loginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //I Initializing ProgressBar
        progressBar = findViewById(R.id.progressBarRegister);

        // Initialize EditText
        yourName = findViewById(R.id.nameRegisterEditText);
        emailEditText = findViewById(R.id.emailRegisterEditText);
        passwordEditText = findViewById(R.id.passwordRegisterEditText);
        rePasswordEditText = findViewById(R.id.rePasswordRegisterEditText);

        // Login Button Intialiazing
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);

            }
        });

        register = findViewById(R.id.registerBtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 registration();

            }


        });

    }

    private void registration() {
        String name = yourName.getText().toString().trim();
        String email = emailEditText.getText().toString().trim() ;
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();

        if(name.isEmpty()){
            yourName.setError("Required");
            yourName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailEditText.setError("Required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide email valid");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Required");
            passwordEditText.requestFocus();
            return;
        }
        if(rePassword.isEmpty()){
            rePasswordEditText.setError("Required");
            rePasswordEditText.requestFocus();
            return;
        }
        if(!password.equals(rePassword)){ // For Check password match re-password
            rePasswordEditText.setError("password not match");
            emailEditText.requestFocus();
            passwordEditText.setError("password not match");
            passwordEditText.requestFocus();
            return;
        }

        // show progressBar and create User
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Users user = new Users(name,email,"" ,FirebaseAuth.getInstance().getUid());

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                            Toast.makeText(Register.this," Registered Successfully ",Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                                Toast.makeText(Register.this," Logging Successfully...  ",Toast.LENGTH_LONG).show();

                                            }
                                            else{
                                                Toast.makeText(Register.this , "Registration Failed ",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        }


                                    });
                        }else{
                            Toast.makeText(Register.this , " Already Registered ",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


    }








}