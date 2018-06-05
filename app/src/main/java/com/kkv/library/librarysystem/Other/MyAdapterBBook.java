package com.kkv.library.librarysystem.Other;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.kkv.library.librarysystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapterBBook extends BaseAdapter {
    ArrayList<UserBooks> books;
    DatabaseReference book,bbref,curuser,root;
    DataSnapshot ddd;
    FirebaseUser user;
    Context ca;
    String f;

    public MyAdapterBBook(Context c, String find) {
        ca = c;
        book = FirebaseDatabase.getInstance().getReference("Users").child(find);

        books = new ArrayList<UserBooks>();
        f=find;
        retrieve();
    }
    public MyAdapterBBook(Context c) {
        ca=c;

        book = FirebaseDatabase.getInstance().getReference("Users").child(f);
        books=new ArrayList<UserBooks>();
        retrieve();
    }
    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) ca.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.booklist, viewGroup, false);
        final TextView bname = row.findViewById(R.id.booklistname);
        final TextView aname = row.findViewById(R.id.autherlistname);
        final UserBooks temp = books.get(i);
        bbref = FirebaseDatabase.getInstance().getReference("Books").child(temp.id);
        bbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bname.setText(dataSnapshot.getValue(Book.class).bookname);
                aname.setText(dataSnapshot.getValue(Book.class).authername);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // String kkk = ub.bookname;
        //  Toast.makeText(ca,kkk , Toast.LENGTH_SHORT).show();


        return row;
    }

    public void retrieve() {
        book.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ddd = dataSnapshot;
                if(dataSnapshot.getChildrenCount()!=1) {
                    fetchData(dataSnapshot);
                }else{
                    Toast.makeText(ca, "User Has No Books", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchData(DataSnapshot dataSnapshot) {
        books.clear();
        ddd = dataSnapshot;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            final UserBooks b = ds.getValue(UserBooks.class);
            if(Integer.parseInt(b.id)!=0) {
                root=FirebaseDatabase.getInstance().getReference("Date");
                root.setValue(ServerValue.TIMESTAMP);
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String curdateval = dataSnapshot.getValue().toString();
                        long f = Long.parseLong(curdateval);
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
                        Date ss;
                        long s;
                        try {
                            ss=sfd.parse(b.renewdate);
                            s=f-ss.getTime();
                            float days = (s / (1000*60*60*24));
                            if(days>0){
                                b.fine=  ((int)days*2);
                                user= FirebaseAuth.getInstance().getCurrentUser();
                                curuser = FirebaseDatabase.getInstance().getReference("Users").child(user.getDisplayName());
                                curuser.child(b.id).setValue(b);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                books.add(b);
                this.notifyDataSetChanged();
            }
        }
    }
}
