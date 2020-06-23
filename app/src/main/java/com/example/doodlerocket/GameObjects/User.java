package com.example.doodlerocket.GameObjects;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {

    private String name;
    private int place;
    private int score;

    public User(String name, int score) {
        this.name = name;
        //this.place = place;
        this.score = score;
    }

    @Override
    public int compareTo(User user) {
        if(this.score > user.getScore()) return 1;
        else if(this.score == user.getScore()) return 0;
        else return -1; //not in leadboard
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", place=" + place +
                ", score=" + score +
                '}';
    }
}
