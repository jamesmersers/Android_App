package com.example.myapplication;

import static com.example.myapplication.MainActivity.data;
import static com.example.myapplication.StatisticsViewFragment.index;
import static com.example.myapplication.MainActivity.yearlyExpense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RewardViewFragment extends Fragment {
    ActivityResultLauncher<Intent> launcher;
    RecycleViewBookAdapter adapter;
    private ImageButton button;
    public class RecycleViewBookAdapter extends RecyclerView.Adapter{
        private List<Reward> rewardList;
        private int position;
        public List<Reward> getRewardList() { return rewardList; }
        public void setRewardList(List<Reward> rewardList) { this.rewardList = rewardList; }
        public int getContextMenuPosition() { return position; }
        public void setContextMenuPosition(int position) { this.position = position; }
        public RecycleViewBookAdapter(List<Reward> rewardList) {
            this.rewardList = rewardList;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Reward reward = rewardList.get(position);
            int _position = position;

            CheckBox rewardCheckbox = holder.itemView.findViewById(R.id.rewardCheckbox);
            TextView rewardProgress = holder.itemView.findViewById(R.id.rewardProgress);
            TextView rewardDescription = holder.itemView.findViewById(R.id.rewardDescription);
            TextView rewardCost =  holder.itemView.findViewById(R.id.rewardCost);

            String completion = String.valueOf(reward.getCompletionCount());
            String limit = String.valueOf(reward.getLimitCount());
            String progressText = completion + "/" + limit;
            String rewardText = "-" +reward.getCost();
            rewardProgress.setText(progressText);
            rewardDescription.setText(reward.getTitle());
            rewardCost.setText(rewardText);

            rewardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Reward reward = rewardList.get(_position);
                    int rewardBalance = data.get(0);
                    if(rewardBalance > reward.getCost()){
                        if (isChecked) {
                            RewardManager rewardManager = new RewardManager(adapter.getRewardList());
                            reward.finish();
                            rewardManager.removeReward(_position);
                            rewardManager.addReward(_position,reward);
                            adapter.setRewardList(rewardManager.getRewardList());
                            adapter.notifyItemChanged(_position);

                            if (reward.getCompletionCount() == reward.getLimitCount()) {
                                rewardManager.removeReward(_position);
                                adapter.setRewardList(rewardManager.getRewardList());
                                adapter.notifyItemRemoved(_position);
                                adapter.notifyItemRangeChanged(_position, adapter.getItemCount());
                            }
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "您的余额不足", Toast.LENGTH_SHORT).show();
                    }
                    rewardCheckbox.setChecked(false);
                    data.set(0,data.get(0) - reward.getCost());
                    yearlyExpense.set(index,yearlyExpense.get(index) + reward.getCost());
                    try {
                        FileOutputStream fileOut = getContext().openFileOutput("rewards.txt", Context.MODE_PRIVATE);
                        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                        objectOut.writeObject(adapter.getRewardList());
                        fileOut = getContext().openFileOutput("data.txt",Context.MODE_PRIVATE);
                        objectOut = new ObjectOutputStream(fileOut);
                        objectOut.writeObject(data);
                        fileOut = getContext().openFileOutput("yearlyExpense.txt",Context.MODE_PRIVATE);
                        objectOut = new ObjectOutputStream(fileOut);
                        objectOut.writeObject(yearlyExpense);
                        objectOut.close();
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                adapter.setContextMenuPosition(position);
                menu.add(0, 3, 0, "修改");
                menu.add(0, 4, 0, "删除");
            });
        }
        public void buttonAddReward(View view) {
            Intent intent = new Intent(getActivity(), AddRewardActivity.class);
            intent.putExtra("requestCode", 1);
            launcher.launch(intent);
        }
        @Override
        public int getItemCount() {
            return rewardList.size();
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        RewardManager rewardManager = new RewardManager(adapter.getRewardList());
        switch (item.getItemId()) {
            case 3:
                Intent intent = new Intent(getActivity(), EditRewardActivity.class);
                intent.putExtra("requestCode", 2);
                Bundle bundle = new Bundle();
                bundle.putSerializable("reward", rewardManager.getReward(adapter.getContextMenuPosition()));
                intent.putExtras(bundle);
                launcher.launch(intent);
                return true;
            case 4:
                int position = adapter.getContextMenuPosition();
                rewardManager.removeReward(position);
                adapter.setRewardList(rewardManager.getRewardList());
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewardview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_rewards);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RewardManager rewardManager = new RewardManager(new ArrayList<>());
        adapter = new RecycleViewBookAdapter(rewardManager.getRewardList());

        try {
            FileInputStream fileIn = getContext().openFileInput("rewards.txt");
            if(fileIn != null) {
                fileIn = getContext().openFileInput("rewards.txt");
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                List<Reward> savedRewardList = (List<Reward>) objectIn.readObject();
                rewardManager.setRewardList(savedRewardList);
                adapter.setRewardList(savedRewardList);
                objectIn.close();
                fileIn.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(adapter);

        ImageButton addButton = view.findViewById(R.id.add_reward_button);
        button = addButton;
        addButton.setImageResource(R.drawable.add_icon);
        addButton.setBackground(null);
        addButton.setOnClickListener(v -> {
            adapter.buttonAddReward(v);
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int position = adapter.getContextMenuPosition();
                            Bundle bundle = data.getExtras();
                            Reward reward = (Reward) bundle.getSerializable("reward");
                            switch(data.getIntExtra("requestCode", 0)){
                                case 1:
                                    rewardManager.addReward(position,reward);
                                    adapter.setRewardList(rewardManager.getRewardList());
                                    adapter.notifyItemChanged(position);
                                    adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                                    break;
                                case 2:
                                    rewardManager.removeReward(position);
                                    rewardManager.addReward(position,reward);
                                    adapter.setRewardList(rewardManager.getRewardList());
                                    adapter.notifyItemChanged(position);
                                    break;
                            }
                            try {
                                FileOutputStream fileOut = getContext().openFileOutput("rewards.txt", Context.MODE_PRIVATE);
                                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                                objectOut.writeObject(adapter.getRewardList());
                                objectOut.close();
                                fileOut.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        return view;
    }
    public void onPause() {
        super.onPause();
        this.button.setVisibility(View.GONE);
    }
    public void onResume() {
        super.onResume();
        this.button.setVisibility(View.VISIBLE);
    }
}
