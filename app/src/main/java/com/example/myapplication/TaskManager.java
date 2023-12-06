package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class TaskManager implements Serializable{
    private List<Task> taskList;
    public TaskManager(List<Task> taskList) {
        this.taskList = taskList;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public void addTask(int position,Task task) {
        this.taskList.add(position,task);
    }
    public void removeTask(int position) {
        this.taskList.remove(position);
    }

    public Task getTask(int position) {
        return taskList.get(position);
    }
}
