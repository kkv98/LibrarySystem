package com.kkv.library.librarysystem.Other;

/**
 * Created by keerthivaasan on 14/2/18.
 */

public class Book {
   public String bookname,authername,count,id;

    public Book(String bookname, String authername, String count,String id) {
        this.bookname = bookname;
        this.authername = authername;
        this.count = count;
        this.id = id;
    }
    public Book()
    {

    }

    public String getBookname() {
        return bookname;
    }

    public String getAuthername() {
        return authername;
    }

    public String getCount() {
        return count;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setAuthername(String authername) {
        this.authername = authername;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
