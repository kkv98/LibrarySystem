package com.kkv.library.librarysystem.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kkv.library.librarysystem.Other.Book;
import com.kkv.library.librarysystem.Other.MyAdapterBook;
import com.kkv.library.librarysystem.R;

import java.util.ArrayList;

public class SearchBook extends AppCompatActivity implements View.OnClickListener{
    Button s;
    EditText et;
    ListView lv;
    ArrayList<Book> books;
    MyAdapterBook a;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        s = (Button) findViewById(R.id.bbsearch);
        lv= (ListView) findViewById(R.id.booklist);
        et = (EditText) findViewById(R.id.bookname);
        s.setOnClickListener(this);
        books=new ArrayList<>();
        et = (EditText) findViewById(R.id.bookname);
        a=new MyAdapterBook(this);
        lv.setAdapter(a);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                sb = new StringBuffer();

                Book b= (Book) a.getItem(pos);
                sb.append("Book Name:"+b.bookname+"\n");
                sb.append("Author Name:"+b.authername+"\n");
                sb.append("No. Of Copies Available:"+b.count+"\n");
                sb.append("Id:"+b.id+"\n");
                AlertDialog.Builder bbb=new AlertDialog.Builder(SearchBook.this);
                bbb.setCancelable(true);
                bbb.setTitle("Book Details:");
                bbb.setMessage(sb.toString());
                bbb.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        a=new MyAdapterBook(this,et.getText().toString());
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
       lv.setAdapter(a);
        a.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                sb = new StringBuffer();

                       Book b= (Book) a.getItem(pos);
                        sb.append("Book Name:"+b.bookname+"\n");
                        sb.append("Author Name:"+b.authername+"\n");
                        sb.append("No. Of Copies Available:"+b.count+"\n");
                        sb.append("Id:"+b.id+"\n");
                AlertDialog.Builder bbb=new AlertDialog.Builder(SearchBook.this);
                bbb.setCancelable(true);
                bbb.setTitle("Book Details:");
                bbb.setMessage(sb.toString());
                bbb.show();
            }
        });
        a.notifyDataSetChanged();

    }


}
