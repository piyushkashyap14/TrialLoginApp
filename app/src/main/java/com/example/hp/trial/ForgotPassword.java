package com.example.hp.trial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private Button ResetPassword;
    private EditText PasswordEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ResetPassword = findViewById(R.id.btnreset);
        PasswordEmail = findViewById(R.id.etpasswordemail);
        firebaseAuth = FirebaseAuth.getInstance();

        //arrow for going back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = PasswordEmail.getText().toString().trim();
                if(useremail.equals("")){
                    Toast.makeText(ForgotPassword.this, "Enter registered email ID", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Password reset mail sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, FirstActivity.class));
                            }else{
                                Toast.makeText(ForgotPassword.this, "Email is not registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

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
