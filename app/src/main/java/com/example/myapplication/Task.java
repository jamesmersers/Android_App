package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private String title;
    private int reward;
    private int taskType;
    private int completionCount;
    private int limitCount;
    private List<Integer> daysOfWeek;
    public Task(){
        this.title = "";
        this.reward = 0;
        this.taskType = 0;
        this.completionCount = 0;
        this.daysOfWeek = new ArrayList<>();
        this.limitCount = 0;
    }
    public Task(String title, int reward, int taskType, int limitCount, List<Integer> daysOfWeek) {
        this.title = title;
        this.reward = reward;
        this.taskType = taskType;
        this.completionCount = 0;
        this.daysOfWeek = daysOfWeek;
        this.limitCount = limitCount;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getReward() {
        return reward;
    }
    public void setReward(int reward) {
        this.reward = reward;
    }
    public int getTaskType() {
        return taskType;
    }
    public int getCompletionCount() {
        return completionCount;
    }
    public int getLimitCount(){
        return limitCount;
    }
    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }
    public void resetCompletionCount() {
        this.completionCount = 0;
    }
    public void finish() {
        completionCount++;
    }
    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    public void addDayOfWeek(int dayOfWeek) {
        daysOfWeek.add(dayOfWeek);
    }
    public void removeDayOfWeek(int dayOfWeek) {
        daysOfWeek.remove(dayOfWeek);
    }
}