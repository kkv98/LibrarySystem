package com.kkv.library.librarysystem.Other;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kkv.library.librarysystem.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MyAdapterBook extends BaseAdapter{
    ArrayList<Book> books;
    DatabaseReference book;
    DataSnapshot ddd;
    Context ca;
    String f;

    public MyAdapterBook(Context c,String find) {
        ca=c;
        f=find.toUpperCase() +".*";
        book = FirebaseDatabase.getInstance().getReference("Books");
        books=new ArrayList<Book>();
        retrieve();
    }
    public MyAdapterBook(Context c) {
        ca=c;
        f=".*";
        book = FirebaseDatabase.getInstance().getReference("Books");
        books=new ArrayList<Book>();
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
        LayoutInflater inflater= (LayoutInflater) ca.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.booklist,viewGroup,false);
        TextView bname=row.findViewById(R.id.booklistname);
        TextView aname=row.findViewById(R.id.autherlistname);
        Book temp=books.get(i);
        bname.setText(temp.bookname);
        aname.setText("BY "+temp.authername);
        return row;
    }
    public void retrieve() {
        book.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ddd=dataSnapshot;
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void fetchData(DataSnapshot dataSnapshot)
    {
        books.clear();
        ddd=dataSnapshot;
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Book b=ds.getValue(Book.class);
            if(Pattern.matches(f,b.bookname.toUpperCase()))
            books.add(b);
            this.notifyDataSetChanged();
        }
    }
}
