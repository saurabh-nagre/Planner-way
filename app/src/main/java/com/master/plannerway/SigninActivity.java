package com.master.plannerway;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SigninActivity  extends AppCompatActivity {
    EditText memailET,mpasswordET;
    TextView mforgotPasswordTV;

    Button msignupButton,msigninButton;
    private FirebaseAuth mAuth;
    ProgressBar mprogressBar;
    private String memail,mpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        mforgotPasswordTV = findViewById(R.id.forgotpasswordTextView);
        msignupButton = findViewById(R.id.createAccountButton);
        msigninButton = findViewById(R.id.signinButton);
        memailET = findViewById(R.id.emailEditText);
        mpasswordET = findViewById(R.id.passwordEditText);
        mprogressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();

        if(currentuser!=null){
            switchActivity();
            finishAffinity();
        }


        mpasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<8 || s.toString().contains(" ")){
                    mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                }
                else
                {
                    mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().contains(" ")){
                    Toast.makeText(SigninActivity.this,"Password Shouldn't Contain any spaces",Toast.LENGTH_LONG).show();
                }
                else if(s.length()<8){
                    mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                }
            }
        });

        memailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        msignupButton.setOnClickListener(this::signup);

        msigninButton.setOnClickListener(this::signin);

        mforgotPasswordTV.setOnClickListener(this::forgotPassword);


    }

    void signup(View v){
        memail = memailET.getText().toString();
        mpassword = mpasswordET.getText().toString();


        memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        if(!checkNetwork()){
            Snackbar.make(v,"Network Connection Failed",Snackbar.LENGTH_SHORT).show();
        }
        else if(memail.isEmpty() ) {
            Toast.makeText(SigninActivity.this, "Enter Email to Create Account", Toast.LENGTH_SHORT).show();
            memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
        else if(mpassword.length()<8)
            mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        else{
            mprogressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(SigninActivity.this, task -> {

                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null) {
                        switchActivity();
                    }
                    else {
                        Toast.makeText(SigninActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                        recreate();
                    }

                }
                else{
                    mprogressBar.setVisibility(View.GONE);
                    Toast.makeText(SigninActivity.this,"Please check your Email",Toast.LENGTH_SHORT).show();

                    memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                    try {
                        throw Objects.requireNonNull(task.getException());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("SignupException","Exception in creating Account");
                    }

                }

            });
        }
    }

    void signin(View v){
        memail = memailET.getText().toString();
        mpassword = mpasswordET.getText().toString();

        memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        if(!checkNetwork()){
            Snackbar.make(v,"Network Connection Failed",Snackbar.LENGTH_SHORT).show();
        }
        else if(memail.isEmpty()){
            Toast.makeText(SigninActivity.this,"Enter Email to Login or Use Google Sign in",Toast.LENGTH_SHORT).show();
            memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        }
        else if(mpassword.length()<8){
            mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        }
        else{
            mprogressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(SigninActivity.this, task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null) {
                        switchActivity();
                    }
                    else{
                        mprogressBar.setVisibility(View.GONE);
                        Toast.makeText(SigninActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    mprogressBar.setVisibility(View.GONE);
                    mpasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                    memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                    Toast.makeText(SigninActivity.this,"Check Entered Credentials",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void forgotPassword(View v){
        memail =  memailET.getText().toString();
        boolean network = checkNetwork();
        if(!network){
            Snackbar.make(v,"Network Connection Failed",Snackbar.LENGTH_SHORT).show();
        }
        else if(memail.isEmpty()){
            Toast.makeText(SigninActivity.this,"Enter Email to Reset Password",Toast.LENGTH_LONG).show();
            memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        }
        else{
            mAuth.sendPasswordResetEmail(memail).addOnCompleteListener(this,task -> {
                if(task.isSuccessful()){
                    Toast.makeText(SigninActivity.this,"Password Reset link sent to "+memail,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SigninActivity.this,"Check Your Email",Toast.LENGTH_SHORT).show();
                    memailET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
                }
            });
        }


    }
    void switchActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mobileConn.isConnected() || wifiConn.isConnected();
    }


}
