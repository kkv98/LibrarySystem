package com.kkv.library.librarysystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.kkv.library.librarysystem.R;
import com.paypal.android.sdk.payments.PayPalService;

public class Home extends AppCompatActivity {
    private ActionBar toolbar;
    Fragment fragment;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = getSupportActionBar();
        auth = FirebaseAuth.getInstance();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Library Home");
        fragment = new com.kkv.library.librarysystem.Fragments.Home();
        loadFragment(fragment);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Library Home");
                    fragment = new com.kkv.library.librarysystem.Fragments.Home();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_books:
                    toolbar.setTitle("Books");
                    fragment = new com.kkv.library.librarysystem.Fragments.Books();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_borret:
                    toolbar.setTitle("History");
                    fragment = new com.kkv.library.librarysystem.Fragments.History();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_fine:
                    toolbar.setTitle("Fine");
                    fragment = new com.kkv.library.librarysystem.Fragments.Fine();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_about:
                    toolbar.setTitle("About");
                    fragment = new com.kkv.library.librarysystem.Fragments.About();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Intent i=new Intent(Home.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
