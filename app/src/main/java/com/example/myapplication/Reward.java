package com.example.myapplication;

import java.io.Serializable;
public class Reward implements Serializable {
    private String title;
    private String cost;
    private int completionCount;
    private int limitCount;
    public Reward(String title, String cost, int limitCount) {
        this.title = title;
        this.cost = cost;
        this.completionCount = 0;
        this.limitCount = limitCount;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public int getCompletionCount() {
        return completionCount;
    }
    public void setCompletionCount(int completionCount) {
        this.completionCount = completionCount;
    }
    public void finish() {
        completionCount++;
    }
    public int getLimitCount() {
        return limitCount;
    }
}
