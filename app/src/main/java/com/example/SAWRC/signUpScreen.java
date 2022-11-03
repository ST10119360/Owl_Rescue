package com.example.SAWRC;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class signUpScreen extends AppCompatActivity {
    Button signupBtn;
    EditText FName, Email, Password;
    Button Google, Facebook;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    TextView Login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //saves the user entered values to the variables

        FName = findViewById(R.id.usernameUserInput);
        Email = findViewById(R.id.EmailUserInput);
        Password = findViewById((R.id.passwordUserInput));
        signupBtn = (Button) findViewById(R.id.signupBtnSignupPage);
        Login = findViewById(R.id.alreadyHaveAccountText);

        // Creates an Instance in firebase to capture the values

        fAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressBar);



        //sign up conditions to check if the values entered are valid

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName =  FName.getText().toString().trim();
                String UName = Email.getText().toString().trim();
                String passW = Password.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)){
                    FName.setError("Full Name is required");
                    return;
                }

                if (TextUtils.isEmpty(UName)){
                    Email.setError("Please Enter an Email");
                    return;
                }

                if (TextUtils.isEmpty(passW)){
                    Password.setError("Password is required");
                    return;
                }

                if (passW.length() < 6){
                    Password.setError("Password must be than 6 characters");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                //register user in firebase

                fAuth.createUserWithEmailAndPassword(UName,passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signUpScreen.this, "You have successfully signed up", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(signUpScreen.this, "An error has occurred  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }


}

