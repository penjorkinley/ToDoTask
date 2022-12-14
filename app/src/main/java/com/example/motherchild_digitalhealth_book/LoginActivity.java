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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    private ProgressDialog loader;
    private FirebaseAuth mauth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        email_et = findViewById(R.id.login_email);
        password_et = findViewById(R.id.login_pw);
        TextView signupLink = findViewById(R.id.sign_up_link);
        loader = new ProgressDialog(this);

        mauth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();

//        authStateListener = firebaseAuth -> {
//            FirebaseUser user = mauth.getCurrentUser();
//            if (user != null){
//                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//        };

        //to open the sign up activity
        signupLink.setOnClickListener(view -> {
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

        });
    }

    //to login
    public void login(View view) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            final String emailTxt = email_et.getText().toString().trim();
            final String passwordTxt = password_et.getText().toString().trim();

            if(TextUtils.isEmpty(emailTxt)){
                email_et.setError("Email is required");
            }
            if(TextUtils.isEmpty(passwordTxt)){
                password_et.setError("Password is required");
            }
            else{
                loader.setMessage("Logging in");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                mauth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                        .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String RegisteredUserId = currentUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserId);

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userType = snapshot.child("userType").getValue().toString();
                                    Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                    if (userType.equals("admin")){
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));

                                    }
                                    else{
                                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                    }
                                    loader.dismiss();
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            loader.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Email Address or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
            Toast.makeText(this, "Check Your Network Connectivity and try again", Toast.LENGTH_SHORT).show();
        }

    }


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

