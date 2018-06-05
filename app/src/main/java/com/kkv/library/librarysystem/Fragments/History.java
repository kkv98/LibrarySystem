package com.kkv.library.librarysystem.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kkv.library.librarysystem.Other.MyAdapterBBook;
import com.kkv.library.librarysystem.Other.UserBooks;
import com.kkv.library.librarysystem.R;

import java.util.ArrayList;


public class History extends Fragment {
    DatabaseReference userd;
    ListView lv;
    MyAdapterBBook a;
    ArrayList<UserBooks> books;
    StringBuffer sb;
    TextView name,fine;
    FirebaseUser user;
    String uname;
    public History() {
        // Required empty public constructor
    }

    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        lv= (ListView) view.findViewById(R.id.uubooklist);
        books=new ArrayList<>();
        uname=user.getDisplayName();
        userd= FirebaseDatabase.getInstance().getReference("Users").child(uname);
        a=new MyAdapterBBook(getContext(),uname);
        lv.setAdapter(a);
        a.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                sb = new StringBuffer();
                UserBooks b= (UserBooks) a.getItem(pos);
                sb.append("Id:"+b.id+"\n");
                sb.append("Issue Date:"+b.issuedate+"\n");
                sb.append("Renewal Date:"+b.renewdate+"\n");
                sb.append("Fine:"+b.fine+"\n");

                AlertDialog.Builder bbb=new AlertDialog.Builder(getContext());
                bbb.setCancelable(true);
                bbb.setTitle("Book Details:");
                bbb.setMessage(sb.toString());
                bbb.show();
            }
        });
        a.notifyDataSetChanged();
        return view;
    }
}
