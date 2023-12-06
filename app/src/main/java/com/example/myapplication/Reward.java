package com.example.myapplication;

import java.io.Serializable;
public class Reward implements Serializable {
    private String title;
    private int cost;
    private int completionCount;
    private int limitCount;
    public Reward(String title, int cost, int limitCount) {
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
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public int getCompletionCount() {
        return completionCount;
    }
    public void finish() {
        completionCount++;
    }
    public int getLimitCount() {
        return limitCount;
    }
    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }
}
