package com.example.hp.trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.PriorityQueue;

public class FirstActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private Button Signup;
    private int counter = 2;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView Forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);
        Signup = findViewById(R.id.btnSignup);
        Forgotpassword = findViewById(R.id.tv_forgotpassword);

        Info.setText("No. of attempts remaining: 2");

        firebaseAuth = FirebaseAuth.getInstance(); //get the instance of the class

        //while waiting for login
        progressDialog = new ProgressDialog(this);

        //Check if user was already logged in or not, if yes, direct him to next activity directly
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            finish();
            startActivity(new Intent(FirstActivity.this, SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent1 = new Intent(FirstActivity.this, SignupActivity.class);
                    startActivity(intent1);
            }
        });

        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstActivity.this, ForgotPassword.class));
            }
        });
    }

    private void validate(String userName, String userPassword){

        if(userName.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(this, "Enter complete Login details", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Waiting for Login to complete");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        //Toast.makeText(FirstActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //startActivity (new Intent(FirstActivity.this, SecondActivity.class));
                        checkEmailVerification();
                    } else {
                        Toast.makeText(FirstActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        counter--;
                        progressDialog.dismiss();
                        Info.setText("No. of attempts remaining: " + String.valueOf(counter));
                        if (counter == 0) {
                            Login.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        
        if(emailFlag){
            finish();
            startActivity(new Intent(FirstActivity.this, SecondActivity.class));
        }else{
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
