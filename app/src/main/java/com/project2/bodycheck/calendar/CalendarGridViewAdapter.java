package com.project2.bodycheck.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CalendarGridViewAdapter extends BaseAdapter {
    private Context myContext = null;
    private ArrayList<CalendarGridViewData> myData = new ArrayList<CalendarGridViewData>();

    public CalendarGridViewAdapter(Context context) { this.myContext = context; }

    public void addItem(CalendarGridViewData data) { myData.add(data); }

    @Override
    public int getCount() { return myData.size(); }

    @Override
    public Object getItem(int position) { return myData.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarGridViewItem itemView;

        if(convertView == null) { itemView = new CalendarGridViewItem(myContext, myData.get(position)); }
        else { itemView = (CalendarGridViewItem)convertView; }

        itemView.setDay(myData.get(position).getDay());
        itemView.setMyImage(myData.get(position).getImage());

        return itemView;
    }

    public void clear() { myData.clear(); }
}
