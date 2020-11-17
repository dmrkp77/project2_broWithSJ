package com.project2.bodycheck.calendar;

public class CalendarGridViewData {

    private String day;
    private String dayColor;
    private String image;

    public CalendarGridViewData(String day, String dayColor, String image) {
        this.day = day;
        this.dayColor = dayColor;
        this.image = image;
    }

    public String getDay() { return day; }
    public String getDayColor() { return  dayColor; }
    public String getImage() { return image; }
}
