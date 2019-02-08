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

public class Register extends AppCompatActivity {


    //Declare private members

    private EditText inpEmailAddress, inpPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private TextView cancel;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        inpEmailAddress = findViewById(R.id.emailaddress);
        inpPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.register);
        btnRegister.setTextColor(ContextCompat.getColor(Register.this, R.color.buttonText));
        cancel = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.progressBar);
        final TextInputLayout txlemail = findViewById(R.id.text_input_layout);
        final TextInputLayout txlpassword = findViewById(R.id.text_input_layout2);

        firebaseAuth = FirebaseAuth.getInstance();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = inpEmailAddress.getText().toString();
                String password = inpPassword.getText().toString();

                //Validations. Write here
                if (TextUtils.isEmpty(email)) {
                    txlemail.setError("Enter email address!");
                    return;
                }

                else {
                    txlemail.setError(null);
                }

                if (TextUtils.isEmpty(password)) {
                    txlpassword.setError("Enter password");
                    return;
                }

                else {
                    txlpassword.setError(null);
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            //btnRegister.setError("Authentication failed");
                            //btnRegister.getBackground().setColorFilter(getResources().getColor(R.color.errorRed), PorterDuff.Mode.SRC_ATOP);
                            Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //btnRegister.setError(null);
                            startActivity(new Intent(Register.this, Login.class));
                            finish();
                        }

                    }
                });



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
