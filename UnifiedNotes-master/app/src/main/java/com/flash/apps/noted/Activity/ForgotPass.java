package com.flash.apps.noted.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.apps.noted.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    private EditText emailAddress;
    private Button resetPassword;
    private TextView backtoLogin;
    private TextInputLayout txlEmail;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);

        emailAddress = findViewById(R.id.emailaddress);
        resetPassword = findViewById(R.id.resetpassword);
        backtoLogin = findViewById(R.id.back_to_login);
        txlEmail = findViewById(R.id.text_input_layout);
        progressBar  = findViewById(R.id.progressBar);

        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPass.this, MainActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();


        resetPassword.setTextColor(ContextCompat.getColor(ForgotPass.this, R.color.buttonText));
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resetEmail = emailAddress.getText().toString();
                if(TextUtils.isEmpty(resetEmail))
                {
                    txlEmail.setError("Enter your email address");
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(ForgotPass.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPass.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPass.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }



                    }
                });


            }
        });



    }
}