package com.example.motherchild_digitalhealth_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    private TextView signupLink;
    private ProgressDialog loader;
//    private FirebaseAuth mauth;
//    private FirebaseFirestore fstore;
//
//    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        email_et = findViewById(R.id.login_email);
        password_et = findViewById(R.id.login_pw);
        signupLink =findViewById(R.id.sign_up_link);
        loader = new ProgressDialog(this);

//        mauth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = mauth.getCurrentUser();
//                if (user != null){
//                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        };

        //to open the sign up activity
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()){
                    Intent signUpIntent = new Intent(LoginActivity.this,
                            SignUpActivity.class);
                    startActivity(signUpIntent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Check your network connectivity and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //to login
    public void login(View view) {

        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()){
//
//            final String emailTxt = email_et.getText().toString().trim();
//            final String passwordTxt = password_et.getText().toString().trim();
//
//            if(TextUtils.isEmpty(emailTxt)){
//                email_et.setError("Email is required");
//            }
//            if(TextUtils.isEmpty(passwordTxt)){
//                password_et.setError("Password is required");
//            }
//            else{
//                loader.setMessage("Logging in");
//                loader.setCanceledOnTouchOutside(false);
//                loader.show();
//
//                mauth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
//                        }
//                        loader.dismiss();
//                    }
//                });
//
//            }
//        }
//        else{
//            Toast.makeText(this, "Check Your Network Connectivity and try again", Toast.LENGTH_SHORT).show();
//        }
//
    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mauth.addAuthStateListener(authStateListener);
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mauth.removeAuthStateListener(authStateListener);
//    }

}