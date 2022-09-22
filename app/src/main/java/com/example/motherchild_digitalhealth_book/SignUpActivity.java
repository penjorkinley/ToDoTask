package com.example.motherchild_digitalhealth_book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    Button signup_btn;
    private EditText name_et, cid_et, date_et, year_et, mobileNo_et, password_et,
            confirm_pw_et, email_address_et;
    private Spinner spinnerMonth;
    private ProgressDialog loader;

    private Uri resultUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        profile_image = findViewById(R.id.signUp_profile_pic);
        name_et = findViewById(R.id.name_input);
        email_address_et = findViewById(R.id.emailAddress_input);
        cid_et = findViewById(R.id.cid_input);
        date_et = findViewById(R.id.date_input);
        spinnerMonth = findViewById(R.id.month_input);
        year_et = findViewById(R.id.year_input);
        mobileNo_et = findViewById(R.id.mobileNo_input);
        password_et = findViewById(R.id.password_input);
        confirm_pw_et = findViewById(R.id.confirmPassword_input);
        signup_btn = findViewById(R.id.sigup_btn);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();



        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to open gallery in the phone
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trim() method is to remove any white spaces
                final String name = name_et.getText().toString().trim();
                final String email = email_address_et.getText().toString().trim();
                final String cidNo = cid_et.getText().toString().trim();
                final String date = date_et.getText().toString().trim();
                final String month = spinnerMonth.getSelectedItem().toString();
                final String year = year_et.getText().toString().trim();
                final String mobileNo = mobileNo_et.getText().toString().trim();
                final String password = password_et.getText().toString().trim();
                final String confirmPw = confirm_pw_et.getText().toString().trim();

                //to throw error if the field is empty
                if (TextUtils.isEmpty(name)){
                    name_et.setError("Name is required");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    email_address_et.setError("Email address is required");
                    return;
                }

                if (TextUtils.isEmpty(cidNo)){
                    cid_et.setError("CID No is required");
                    return;
                }
                if (cidNo.length() != 11){
                    cid_et.setError("CID No should contain 11 digits");
                }
                if (TextUtils.isEmpty(date)){
                    date_et.setError("Date is required");
                    return;
                }
                if (month.equals("Select Month")){
                    Toast.makeText(SignUpActivity.this, "Month is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(year)){
                    year_et.setError("Year is required");
                    return;
                }
                if (TextUtils.isEmpty(mobileNo)){
                    mobileNo_et.setError("Mobile No is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    password_et.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(confirmPw)){
                    confirm_pw_et.setError("Confirmation password is required");
                }
                if (password.equals(confirmPw) && cidNo.length() == 11 && mobileNo.length() == 8){
                    loader.setMessage("Registering...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                //to insert data into that particular node or folder
                                //HashMap is data structure that uses key value pairs

                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", name);
                                userInfo.put("email address", email);
                                userInfo.put("cid No", cidNo);
                                userInfo.put("Date of Birth", date +"/" +month + "/" + year);
                                userInfo.put("mobile No", mobileNo);
                                userInfo.put("password", password);

                                databaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    }
                                });

                                if (resultUri != null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    }
                                    catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Image upload fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profile_picture_url", imageUrl);

                                                        databaseReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(SignUpActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                                                        finish();

                                                    }
                                                });
                                            }
                                        }
                                    });


                                }
                                startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                                finish();
                                loader.dismiss();
                            }
                            loader.dismiss();
                        }
                    });


                }
                else if (cidNo.length() != 11){
                    Toast.makeText(SignUpActivity.this, "CID No should contain 11 digits", Toast.LENGTH_SHORT).show();
                }
                else if (mobileNo.length() != 8){
                    Toast.makeText(SignUpActivity.this, "Mobile No should contain 8 digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        //to set the image chosen from the gallery in the imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }

}