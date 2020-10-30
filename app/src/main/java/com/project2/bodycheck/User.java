package com.project2.bodycheck;

public class User {
    public String username;
    public String email;
    public String password;
    public String uid;
    public String registerDate;
    public String habitScore;
    public String dayScore;

    public User(String registerDate, String username, String email, String password, String uid, String habitScore, String dayScore) {
        this.registerDate = registerDate;
        this.username = username;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.habitScore = habitScore;
        this.dayScore = dayScore;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String rigisterDate) {
        this.registerDate = rigisterDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHabitScore() {
        return habitScore;
    }

    public void setHabitScore(String habitScore) {
        this.habitScore = habitScore;
    }

    public String getDayScore() {
        return dayScore;
    }

    public void setDayScore(String dayScore) {
        this.dayScore = dayScore;
    }

}
