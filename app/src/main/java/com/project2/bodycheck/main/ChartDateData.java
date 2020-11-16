package com.project2.bodycheck.main;

public class ChartDateData {
    String dates;
    int scores;

    public ChartDateData(String dates, int scores) {
        this.dates = dates;
        this.scores = scores;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
