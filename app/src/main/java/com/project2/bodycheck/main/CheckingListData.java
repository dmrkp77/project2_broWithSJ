package com.project2.bodycheck.main;

public class CheckingListData {
    private String title;
    private String check;

    public CheckingListData(String title, String check) {
        this.title = title;
        this.check = check;
    }

    public String getTitle() { return title; }

    public String getCheck() { return check; }
}
