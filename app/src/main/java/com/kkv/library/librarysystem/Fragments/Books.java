package com.kkv.library.librarysystem.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kkv.library.librarysystem.Other.Book;
import com.kkv.library.librarysystem.R;
import com.kkv.library.librarysystem.activity.*;

import java.util.ArrayList;


public class Books extends Fragment {
    DatabaseReference book;
    ArrayList<String> books;
    ListView lv;
    ListAdapter la;
    StringBuffer sb;
    DataSnapshot ddd;
    Button ser;
    public Books() {
        // Required empty public constructor
    }
    public static Books newInstance(String param1, String param2) {
        Books fragment = new Books();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        book = FirebaseDatabase.getInstance().getReference("Books");
        ser=view.findViewById(R.id.searchbook);
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),SearchBook.class);
                startActivity(i);
            }
        });
        lv= (ListView) view.findViewById(R.id.lvb);
        books=new ArrayList<>();
        la=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,books);
        retrieve();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                sb = new StringBuffer();
                int i=0;
                for (DataSnapshot ds : ddd.getChildren()) {
                    if(i==pos){
                        Book b=ds.getValue(Book.class);
                        sb.append("Book Name:"+b.bookname+"\n");
                        sb.append("Author Name:"+b.authername+"\n");
                        sb.append("No. Of Copies Available:"+b.count+"\n");
                        sb.append("Id:"+b.id+"\n");
                        break;
                    }
                    i++;
                }
                AlertDialog.Builder bbb=new AlertDialog.Builder(getActivity());
                bbb.setCancelable(true);
                bbb.setTitle("Book Details:");
                bbb.setMessage(sb.toString());
                bbb.show();
            }
        });
        return view;
    }
    public void fetchData(DataSnapshot dataSnapshot)
    {
        books.clear();
        ddd=dataSnapshot;
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Book b=ds.getValue(Book.class);
            books.add(b.bookname);
        }
    }
    public void retrieve() {
        book.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ddd=dataSnapshot;
                fetchData(dataSnapshot);
                lv.setAdapter(la);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
