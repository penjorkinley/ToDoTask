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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    private ProgressDialog loader;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;

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
        fstore = FirebaseFirestore.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = mauth.getCurrentUser();
            if (user != null){
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }

        };

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

                mauth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        isAdmin(Objects.requireNonNull(authResult.getUser()).getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        loader.dismiss();
                    }
                });


            }
        }
        else{
            Toast.makeText(this, "Check Your Network Connectivity and try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void isAdmin(String uid) {
        DocumentReference df = fstore.collection("user").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                if (documentSnapshot.getString("Admin") != null){
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                }
                else{
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                }
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(authStateListener);
    }

}
//                mauth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                            String uid = task.getResult().getUser().getUid();
//
//                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                            firebaseDatabase.getReference().child("user").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    int userType = snapshot.getValue(Integer.class);
//
//                                    if (userType == 1){
//                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
//                                    }
//                                    else{
//                                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//                                    }
//                                    finish();
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//
//
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
//                        }
//                        loader.dismiss();
//
//                    }

//                });