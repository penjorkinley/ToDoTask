package com.example.motherchild_digitalhealth_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile_activity extends AppCompatActivity {

    CircleImageView edit_imageView;
    TextView username, emailAddress,phoneNo;
    Button changeEmail, changePhoneNo, changePassword, ep_logout_btn;
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        edit_imageView = findViewById(R.id.edit_profile_pic);
        username = findViewById(R.id.edit_profile_username);
        emailAddress = findViewById(R.id.edit_profile_email);
        phoneNo = findViewById(R.id.edit_profile_phoneNo);
        changeEmail = findViewById(R.id.change_email_btn);
        changePhoneNo = findViewById(R.id.change_no_btn);
        changePassword = findViewById(R.id.change_pw_btn);
        ep_logout_btn = findViewById(R.id.logout_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child((FirebaseAuth.getInstance().getCurrentUser()).getUid()
        );

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    username.setText(name);

                    String email = snapshot.child("email_address").getValue().toString();
                    emailAddress.setText(email);

                    String phoneNum = snapshot.child("mobile_No").getValue().toString();
                    phoneNo.setText(phoneNum);

                    if (snapshot.hasChild("profile_picture_url")){
                        String imageUrl = snapshot.child("profile_picture_url").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(edit_imageView);
                    }else{
                        edit_imageView.setImageResource(R.drawable.profile_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder = new AlertDialog.Builder(this);
    }

    //to logout
    public void editP_logout(View view) {
        builder.setTitle("")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(EditProfile_activity.this, LoginActivity.class));
                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    //to change email address
    public void change_email_btn(View view) {
    }

    //to change phone No
    public void change_pNo_btn(View view) {
    }

    //to change password
    public void change_pw_btn(View view) {
    }

    //to change profile pic
    public void change_profile_pic(View view) {
    }
}