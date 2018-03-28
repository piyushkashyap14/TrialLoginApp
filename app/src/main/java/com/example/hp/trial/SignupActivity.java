package com.example.hp.trial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.squareup.picasso.Picasso.*;

public class SignupActivity extends AppCompatActivity {

    private ImageView Profilepic;
    private EditText Username;
    private EditText Name1;
    private EditText Phoneno;
    private EditText Password1;
    private EditText Age;
    private Button Signup1;
    private Button Alreadyuser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    String username, name, password, phoneno, age;

    //adding firebase storage object
    private FirebaseStorage firebaseStorage;

    //imageview from gallery
    private static int PICK_IMAGE = 123;
    Uri imagePath;

    //made it global
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Profilepic = (ImageView) findViewById(R.id.ivprofilepic);
        Username = (EditText) findViewById(R.id.etusername);
        Name1 =(EditText) findViewById(R.id.etName1);
        Phoneno = (EditText) findViewById(R.id.etPhoneno);
        Password1 =(EditText) findViewById(R.id.etPassword1);
        Age = (EditText) findViewById(R.id.etage);
        Signup1 = (Button) findViewById(R.id.btnSignup1);
        Alreadyuser = (Button) findViewById(R.id.alreadysigned);
        progressBar = findViewById(R.id.progress_bar);

        //getting instance
        firebaseAuth = FirebaseAuth.getInstance();

        //getting instance
        firebaseStorage = FirebaseStorage.getInstance();

        //creating storage reference object
        storageReference = firebaseStorage.getReference();

        Signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //upload data in database
                    String user_name = Name1.getText().toString().trim();
                    String user_password = Password1.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_name, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendEmailVerification();
                            }else {
                                Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SignupActivity.this, FirstActivity.class);
                startActivity(intent2);
            }
        });

        //putting image view picture from gallery
        Profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*"); //we can also use application/*  audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //pick image acts as request
                //if request is successful then only intent is executed
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
    }

    //putting image view picture from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                // we can also use picasso library for loading image from gallery to this imageview
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                Profilepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //validating sign up details for new user
    private boolean validate(){
        boolean result = false;

        //make these variable global
        username = Username.getText().toString();
        name = Name1.getText().toString();
        phoneno = Phoneno.getText().toString();
        password = Password1.getText().toString();
        age = Age.getText().toString();

        if(username.isEmpty() || name.isEmpty() || password.isEmpty() || phoneno.isEmpty() || age.isEmpty() || imagePath == null){
            Toast.makeText(this,"Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(SignupActivity.this, "Successfully Registered and a verification mail has been sent", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }else{
                        Toast.makeText(SignupActivity.this, "Verification mail has not been sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //sending data to firebase database
    private void sendUserData(){
        //get the instance of firebase database, create an object for it
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());

        //in storage -> (main folder -> UID/images/profile_pic.jpg
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Picture");
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignupActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
                if((int)progress == 0) {
                    Toast.makeText(SignupActivity.this, "Saving Image and User Data", Toast.LENGTH_SHORT).show();
                }
                if((int)progress == 100){
                    finish();
                    startActivity(new Intent(SignupActivity.this,FirstActivity.class));
                }
            }
        });

        UserProfile userProfile = new UserProfile(username,name,phoneno,age);
        myref.setValue(userProfile);
    }
}
