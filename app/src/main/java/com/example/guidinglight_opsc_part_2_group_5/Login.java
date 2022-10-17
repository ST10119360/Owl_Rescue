package com.example.guidinglight_opsc_part_2_group_5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Button loginBtn;
    Button sig;
    EditText Email, passW;
    ProgressBar proB;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //creates a onclick listener to open registration
        sig = (Button)findViewById(R.id.CreateAcc);
        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reg();
            }
        });


//declares the fields from xml to variables

        Email = findViewById(R.id.Emailtext);
        passW = findViewById(R.id.passwordText);
        proB = findViewById(R.id.progressBarLog);
        fAuth = FirebaseAuth.getInstance();





        loginBtn.setOnClickListener(new View.OnClickListener() {

            //checks if the users information has met the conditions
            @Override
            public void onClick(View view) {
                String mail = Email.getText().toString().trim();
                String pass = passW.getText().toString().trim();

                if (TextUtils.isEmpty(mail)){
                    Email.setError("Please Enter your Email");
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    passW.setError("Password is required");
                    return;
                }

                if (pass.length() < 6){
                    passW.setError("Password must be than 6 characters");
                    return;
                }

                proB.setVisibility(View.VISIBLE);

                //check if user has registered

                fAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Login.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomePage.class));
                        }else{
                            Toast.makeText(Login.this, "An error has occurred  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            proB.setVisibility(View.GONE);
                        }
                    }
                });
            }


        });





    }





    public void login(){

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void Reg(){

        Intent intent = new Intent(this, signUpScreen.class);
        startActivity(intent);
    }

    public void sig(View view) {
    }
}
