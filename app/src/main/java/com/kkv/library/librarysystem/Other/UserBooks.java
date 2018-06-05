package com.kkv.library.librarysystem.Other;


public class UserBooks {
    public String id,issuedate,renewdate;
    public int fine;

    public UserBooks()
    {

    }
    public UserBooks(String id, String issuedate, String renewdate, int fine) {
        this.id = id;
        this.issuedate = issuedate;
        this.renewdate = renewdate;
        this.fine = fine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getRenewdate() {
        return renewdate;
    }

    public void setRenewdate(String renewdate) {
        this.renewdate = renewdate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}
