package com.kkv.library.librarysystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kkv.library.librarysystem.Other.UserBooks;
import com.kkv.library.librarysystem.R;


import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button login;
    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference userfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()==true ) {
                startActivity(new Intent(MainActivity.this, Home.class));
                finish();
            }
            else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
       }
           setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        login.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                FirebaseUser user;
                user= FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                   userfb= FirebaseDatabase.getInstance().getReference();
                   //userfb.child("Users").child(user.getDisplayName()).child("BorrowedBooks").setValue(new UserBooks());
                    HashMap<String, Object> hm=new HashMap<>();
                    hm.put("fine","0");
                    userfb.child("Users").child(user.getDisplayName()).child("0").setValue(new UserBooks("0", "0", "0", 0));

                }
                startActivity(new Intent(MainActivity.this,Home.class));
                finish();
                return;
            } else {
                if (response == null) {
                    Toast.makeText(MainActivity.this,"Login canceled by UserBooks",Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(MainActivity.this,"Unknown Error",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(MainActivity.this,"Unknown sign in response",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "pasued", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "resumed", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "stoped", Toast.LENGTH_SHORT).show();
        super.onStop();
       }

    @Override
    protected void onStart() {
        Toast.makeText(this, "started", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Toast.makeText(this, "restarted", Toast.LENGTH_SHORT).show();
        super.onRestart();
    }
}