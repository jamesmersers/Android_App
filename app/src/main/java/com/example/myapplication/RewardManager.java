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
    public void addReward(int position,Reward reward) {
        this.rewardList.add(position,reward);
    }
    public void removeReward(int position) {
        this.rewardList.remove(position);
    }
    public Reward getReward(int position) {
        return rewardList.get(position);
    }

    public void setRewardList(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
}
