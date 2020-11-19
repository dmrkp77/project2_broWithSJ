package com.project2.bodycheck.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project2.bodycheck.R;

public class CheckingListItem extends LinearLayout {
    private TextView myTitle;

    public CheckingListItem(Context context, CheckingListData data) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.checkinglist_style, this, true);

        myTitle = findViewById(R.id.checking_list_item_title);
    }

    public void setTitle(String title) { myTitle.setText(title); }
}
