package com.kkv.library.librarysystem.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.kkv.library.librarysystem.Other.Book;
import com.kkv.library.librarysystem.Other.PayPalConfig;
import com.kkv.library.librarysystem.Other.UserBooks;
import com.kkv.library.librarysystem.R;
import com.kkv.library.librarysystem.activity.Payment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fine extends Fragment {
    FirebaseUser user;
    DatabaseReference book,root,curuser;
    int totalfine;
    String curdateval,curdate,rewdate;
    TextView fine;
    Button but;
    String paymentAmount;

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    public Fine() {

    }

    public static Fine newInstance(String param1, String param2) {
        Fine fragment = new Fine();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fine, container, false);
        fine=view.findViewById(R.id.fine);
        but=view.findViewById(R.id.butfine);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalfine>0)
                getPayment();
                else {
                    renew();

                }
            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();

        curuser = FirebaseDatabase.getInstance().getReference("Users").child(user.getDisplayName());
        curuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalfine=0;
                if(dataSnapshot.getChildrenCount()!=1) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        totalfine+=ds.getValue(UserBooks.class).fine;
                    }
                    fine.setText("Rs.:"+totalfine);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(getContext(), PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        getContext().startService(intent);
        return view;
    }
    private void getPayment() {
        paymentAmount=totalfine+"";
        PayPalPayment payment = new PayPalPayment(new BigDecimal(totalfine), "USD", "Fine Amount",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        renew();
                        Log.i("paymentExample", paymentDetails);
                        startActivity(new Intent(getContext(), Payment.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    public void renew(){
        book=FirebaseDatabase.getInstance().getReference("Users").child(user.getDisplayName());
        book.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=1) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(UserBooks.class).id != null) {
                            root=FirebaseDatabase.getInstance().getReference("Date");
                            root.setValue(ServerValue.TIMESTAMP);
                            root.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    curdateval = dataSnapshot.getValue().toString();
                                    long i = Long.parseLong(curdateval);
                                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
                                    Date cu = new Date(i);
                                    curdate = sfd.format(cu);
                                    Calendar c = Calendar.getInstance();
                                    try {
                                        c.setTime(sfd.parse(curdate));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    c.add(Calendar.DATE, 5);
                                    rewdate = sfd.format(c.getTime());
                                    curuser.child(ds.getValue(UserBooks.class).id).setValue(new UserBooks(ds.getValue(UserBooks.class).id, ds.getValue(UserBooks.class).issuedate, rewdate, 0));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getContext(), "All Books Were Successfully Renewed.", Toast.LENGTH_SHORT).show();
    }

}
