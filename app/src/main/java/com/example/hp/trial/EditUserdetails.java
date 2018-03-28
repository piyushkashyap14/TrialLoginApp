package com.example.hp.trial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserdetails extends AppCompatActivity {

    private EditText name, email, phoneno, age;
    private Button update;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        name = findViewById(R.id.etname);
        email = findViewById(R.id.etmail);
        phoneno = findViewById(R.id.etphoneno);
        age = findViewById(R.id.etage);
        update = findViewById(R.id.btnupdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //initially previous saved details to be displayed but editable
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                name.setText(userProfile.getUserName());
                email.setText(userProfile.getUserEmail());
                phoneno.setText(userProfile.getUserNumber());
                age.setText(userProfile.getUserAge());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditUserdetails.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

       update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = name.getText().toString();
                String newemail = email.getText().toString();
                String newphoneno = phoneno.getText().toString();
                String newage = age.getText().toString();

                UserProfile userProfile = new UserProfile(newname, newemail, newphoneno, newage);
                databaseReference.setValue(userProfile);

                finish();
                startActivity(new Intent(EditUserdetails.this, SecondActivity.class));
                Toast.makeText(EditUserdetails.this, "Details Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
