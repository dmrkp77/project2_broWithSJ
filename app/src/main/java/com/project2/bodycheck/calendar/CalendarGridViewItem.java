package com.project2.bodycheck.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project2.bodycheck.R;

public class CalendarGridViewItem extends LinearLayout {

    private TextView myDay;
    private ImageView myImage;

    public CalendarGridViewItem(Context context, CalendarGridViewData data) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_item, this, true);

        myDay = findViewById(R.id.calendar_item_text);
        myImage = findViewById(R.id.calendar_item_image);
    }

    public void setDay(String day) { myDay.setText(day); }

    public void setMyImage(String image) {
        if(image.equals("0")) { myImage.setBackgroundResource(R.drawable.ic_launcher_foreground); }
    }
}
