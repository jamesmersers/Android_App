package com.example.myapplication;

import java.util.List;

public class RewardManager {
    private List<Reward> rewardList;
    public RewardManager(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
    public List<Reward> getRewardList() {
        return rewardList;
    }
    public void addTask(int position,Reward reward) {
        this.rewardList.add(position,reward);
    }
    public void removeTask(int position) {
        this.rewardList.remove(position);
    }
    public Reward getTask(int position) {
        return rewardList.get(position);
    }
}
