package com.example.myapplication;

import static com.example.myapplication.MainActivity.data;
import static com.example.myapplication.StatisticsViewFragment.index;
import static com.example.myapplication.StatisticsViewFragment.yearlyIncome;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.tabs.TabLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
public class TaskViewFragment extends Fragment {
    ActivityResultLauncher<Intent> launcher;
    RecycleViewBookAdapter adapter;
    private ImageButton button;
    private int currentTaskType = 0;
    ArrayList<TaskManager> taskManagers = new ArrayList<>();
    public class RecycleViewBookAdapter extends RecyclerView.Adapter{
        private List<Task> taskList;
        private int currentPosition;
        public List<Task> getTaskList() { return taskList; }
        public void setTaskList(List<Task> taskList) { this.taskList = taskList; }
        public int getContextMenuPosition() { return currentPosition; }
        public void setContextMenuPosition(int position) { this.currentPosition = position; }
        public RecycleViewBookAdapter(List<Task> taskList) {
            this.taskList = taskList;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Task task = taskList.get(position);
            int _position = position;

            CheckBox taskCheckbox = holder.itemView.findViewById(R.id.taskCheckbox);
            TextView taskProgress = holder.itemView.findViewById(R.id.taskProgress);
            TextView taskDescription = holder.itemView.findViewById(R.id.taskDescription);
            TextView taskReward =  holder.itemView.findViewById(R.id.taskReward);

            String completion = String.valueOf(task.getCompletionCount());
            String limit = String.valueOf(task.getLimitCount());
            String progressText = completion + "/" + limit;
            String rewardText = "+" +task.getReward();
            taskProgress.setText(progressText);
            taskDescription.setText(task.getTitle());
            taskReward.setText(rewardText);
            taskCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        TaskManager taskManager = new TaskManager(adapter.getTaskList());
                        Task task = taskList.get(_position);
                        task.finish();
                        taskManager.removeTask(_position);
                        taskManager.positionAdd(_position,task);
                        adapter.setTaskList(taskManager.getTaskList());
                        adapter.notifyItemChanged(_position);

                        if (task.getCompletionCount() == task.getLimitCount()) {
                            switch(task.getTaskType()){
                            case 0:
                                taskManager.removeTask(_position);
                                adapter.setTaskList(taskManager.getTaskList());
                                adapter.notifyItemRemoved(_position);
                                adapter.notifyItemRangeChanged(_position, adapter.getItemCount());
                                break;
                            case 1:
                            case 2:
                                task.resetCompletionCount();
                                taskManager.removeTask(_position);
                                taskManager.positionAdd(_position,task);
                                adapter.setTaskList(taskManager.getTaskList());
                                adapter.notifyItemChanged(_position);
                                break;
                            }
                            taskCheckbox.setChecked(false);
                        }
                        else{
                            taskCheckbox.setChecked(false);
                        }
                        data.set(0,data.get(0) + task.getReward());
                        yearlyIncome.set(index,yearlyIncome.get(index) + task.getReward());
                        write(new TaskManager(adapter.getTaskList()),currentTaskType);
                    }
                }
            });
            holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                adapter.setContextMenuPosition(position);
                menu.add(0, 1, 0, "修改");
                menu.add(0, 2, 0, "删除");
            });
        }
        public void buttonAddTask(View view) {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            intent.putExtra("requestCode", 1);
            launcher.launch(intent);
        }
        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        TaskManager taskManager = new TaskManager(adapter.getTaskList());
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                intent.putExtra("requestCode", 2);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", taskManager.getTask(adapter.getContextMenuPosition()));
                intent.putExtras(bundle);
                launcher.launch(intent);
                return true;
            case 2:
                int position = adapter.getContextMenuPosition();
                remove(position,taskManager.getTask(position).getTaskType());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void remove(int position,int taskType){
        TaskManager taskManager = new TaskManager(new ArrayList<>());
        switch(taskType){
            case 0:
                taskManager = taskManagers.get(0);
                break;
            case 1:
                taskManager = taskManagers.get(1);
                break;
            case 2:
                taskManager = taskManagers.get(2);
                break;
        }
        taskManager.removeTask(position);
        adapter.setTaskList(taskManager.getTaskList());
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
        write(taskManager,taskType);
    }
    public void add(int position,int taskType,Task task){
        TaskManager taskManager = new TaskManager(new ArrayList<>());
        switch(taskType){
            case 0:
                taskManager = taskManagers.get(0);
                break;
            case 1:
                taskManager = taskManagers.get(1);
                break;
            case 2:
                taskManager = taskManagers.get(2);
                break;
        }
        taskManager.positionAdd(position,task);
        if(currentTaskType == taskType){
            adapter.setTaskList(taskManager.getTaskList());
            adapter.notifyItemChanged(position);
            adapter.notifyItemRangeChanged(position,adapter.getItemCount());
        }
        write(taskManager,taskType);
    }
    public void edit(int position,int taskType,Task task){
        TaskManager taskManager = new TaskManager(new ArrayList<>());
        switch(taskType){
            case 0:
                taskManager = taskManagers.get(0);
                break;
            case 1:
                taskManager = taskManagers.get(1);
                break;
            case 2:
                taskManager = taskManagers.get(2);
                break;
        }
        taskManager.removeTask(position);
        taskManager.positionAdd(position,task);
        adapter.setTaskList(taskManager.getTaskList());
        adapter.notifyItemChanged(position);
        write(taskManager,taskType);
    }
    public void write(TaskManager taskManager,int i){
        try {
            FileOutputStream fileOut = null;
            switch(i){
                case 0:
                    fileOut = getContext().openFileOutput("normalTask.txt",Context.MODE_PRIVATE);
                    break;
                case 1:
                    fileOut = getContext().openFileOutput("dailyTask.txt",Context.MODE_PRIVATE);
                    break;
                case 2:
                    fileOut = getContext().openFileOutput("weeklyTask.txt",Context.MODE_PRIVATE);
            }
            int size = taskManagers.get(0).getTaskList().size() + taskManagers.get(1).getTaskList().size() + taskManagers.get(2).getTaskList().size();
            data.set(1,size);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(taskManager.getTaskList());
            fileOut = getContext().openFileOutput("data.txt",Context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(data);
            fileOut = getContext().openFileOutput("yearlyIncome.txt",Context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(yearlyIncome);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateRecyclerViewAdapter(){
        switch(currentTaskType){
            case 0:
                adapter.setTaskList(taskManagers.get(0).getTaskList());
                break;
            case 1:
                adapter.setTaskList(taskManagers.get(1).getTaskList());
                break;
            case 2:
                adapter.setTaskList(taskManagers.get(2).getTaskList());
                break;
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taskview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        for(int i = 0;i < 3; i++)
            taskManagers.add(new TaskManager(new ArrayList<>()));
        adapter = new RecycleViewBookAdapter(taskManagers.get(0).getTaskList());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("普通任务"));
        tabLayout.addTab(tabLayout.newTab().setText("每日任务"));
        tabLayout.addTab(tabLayout.newTab().setText("每周任务"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentTaskType = 0;
                        break;
                    case 1:
                        currentTaskType = 1;
                        break;
                    case 2:
                        currentTaskType = 2;
                        break;
                }
                updateRecyclerViewAdapter();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        try {
            FileInputStream fileIn = null;
            for(int i = 0;i < 3; i++){
                switch(i){
                    case 0:
                        fileIn = getContext().openFileInput("normalTask.txt");
                        break;
                    case 1:
                        fileIn = getContext().openFileInput("dailyTask.txt");
                        break;
                    case 2:
                        fileIn = getContext().openFileInput("weeklyTask.txt");
                }
                if(fileIn != null) {
                    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                    List<Task> savedTaskList = (List<Task>) objectIn.readObject();
                    taskManagers.get(i).setTaskList(savedTaskList);
                    objectIn.close();
                    fileIn.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        adapter.setTaskList(taskManagers.get(0).getTaskList());
        recyclerView.setAdapter(adapter);

        ImageButton addButton = view.findViewById(R.id.add_task_button);
        this.button = addButton;
        addButton.setImageResource(R.drawable.add_icon);
        addButton.setBackground(null);
        addButton.setOnClickListener(v -> {
            adapter.buttonAddTask(v);
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int position = adapter.getContextMenuPosition();
                            Bundle bundle = data.getExtras();
                            Task task = (Task) bundle.getSerializable("task");
                            switch(data.getIntExtra("requestCode", 0)){
                                case 1:
                                    add(position,task.getTaskType(),task);
                                    break;
                                case 2:
                                    edit(position,task.getTaskType(),task);
                                    break;
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
