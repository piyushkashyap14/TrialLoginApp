package com.example.hp.trial;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity {

    private TextView name, email, phoneno, age, url1;
    private ImageView profilepic;
    private Button editprofile,btnLogout;
    private FirebaseAuth firebaseAuth; //Object created for getting instance of firebaseauth class
    private FirebaseDatabase firebaseDatabase; //Object created for getting instance of firebasedatabase class
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        profilepic = findViewById(R.id.ivprofilepic);
        name = findViewById(R.id.tvname);
        email = findViewById(R.id.tvemail);
        phoneno = findViewById(R.id.tvphoneno);
        age = findViewById(R.id.tvage);
        url1 = findViewById(R.id.tvurl);
        editprofile = findViewById(R.id.btneditprofile);
        btnLogout = findViewById(R.id.btn_logout);

        //arrow for going back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting instances
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //creating storage reference object
        //storageReference = firebaseStorage.getReference();

        //getting reference of the database from where we are going to pull the data
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //getting reference to the image in storage
        String ab = firebaseAuth.getUid().toString().trim();
        String abc = ab + "/Images/Profile Picture";
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/trail-48e74.appspot.com/o/");
        StorageReference imageReference = storageRef.child(abc);

        //getting image from the storage reference and displaying it
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .into(profilepic);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                name.setText("Name: " + userProfile.getUserName());
                email.setText("Email: " + userProfile.getUserEmail());
                phoneno.setText("Contact No.: " + userProfile.getUserNumber());
                age.setText("Age: " + userProfile.getUserAge());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this,FirstActivity.class));
            }
        });
    }

    // implementing back arrow to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
