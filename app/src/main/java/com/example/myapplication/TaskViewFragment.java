package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class TaskViewFragment extends Fragment {
    ActivityResultLauncher<Intent> launcher;
    RecycleViewBookAdapter adapter;
    private int currentRecyclerView = 3;
    public class RecycleViewBookAdapter extends RecyclerView.Adapter{
        private List<Task> taskList;
        private int position;
        public List<Task> getTaskList() { return taskList; }
        public void setTaskList(List<Task> taskList) { this.taskList = taskList; }
        public int getContextMenuPosition() { return position; }
        public void setContextMenuPosition(int position) { this.position = position; }
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
                taskManager.removeTask(position);
                adapter.setTaskList(taskManager.getTaskList());
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taskview, container, false);

        Button normalTaskButton = view.findViewById(R.id.normal_task_button);
        Button dailyTaskButton = view.findViewById(R.id.daily_task_button);
        Button weeklyTaskButton = view.findViewById(R.id.weekly_task_button);
        Button allTasksButton = view.findViewById(R.id.all_tasks_button);

        RecyclerView normalTaskRecyclerView = view.findViewById(R.id.normal_task_recycler_view);
        RecyclerView dailyTaskRecyclerView = view.findViewById(R.id.daily_task_recycler_view);
        RecyclerView weeklyTaskRecyclerView = view.findViewById(R.id.weekly_task_recycler_view);
        RecyclerView allTasksRecyclerView = view.findViewById(R.id.all_task_recycler_view);
        normalTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecyclerView = 0;
                normalTaskRecyclerView.setVisibility(View.VISIBLE);

                allTasksRecyclerView.setVisibility(View.GONE);
                dailyTaskRecyclerView.setVisibility(View.GONE);
                weeklyTaskRecyclerView.setVisibility(View.GONE);
            }
        });

        dailyTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecyclerView = 1;
                dailyTaskRecyclerView.setVisibility(View.VISIBLE);

                allTasksRecyclerView.setVisibility(View.GONE);
                normalTaskRecyclerView.setVisibility(View.GONE);
                weeklyTaskRecyclerView.setVisibility(View.GONE);
            }
        });

        weeklyTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecyclerView = 2;
                weeklyTaskRecyclerView.setVisibility(View.VISIBLE);

                allTasksRecyclerView.setVisibility(View.GONE);
                normalTaskRecyclerView.setVisibility(View.GONE);
                dailyTaskRecyclerView.setVisibility(View.GONE);
            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecyclerView = 3;

                allTasksRecyclerView.setVisibility(View.VISIBLE);

                normalTaskRecyclerView.setVisibility(View.GONE);
                dailyTaskRecyclerView.setVisibility(View.GONE);
                weeklyTaskRecyclerView.setVisibility(View.GONE);
            }
        });
        RecyclerView recyclerView = allTasksRecyclerView;;
        switch(currentRecyclerView){
            case 0:
                recyclerView = normalTaskRecyclerView;
                break;
            case 1:
                recyclerView = dailyTaskRecyclerView;
                break;
            case 2:
                recyclerView = weeklyTaskRecyclerView;
                break;
            case 3:
                recyclerView = allTasksRecyclerView;
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TaskManager taskManager = new TaskManager(new ArrayList<>());
        adapter = new RecycleViewBookAdapter(taskManager.getTaskList());
        recyclerView.setAdapter(adapter);

        ImageButton addButton = view.findViewById(R.id.add_button);
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
                                    taskManager.addTask(position,task);
                                    adapter.setTaskList(taskManager.getTaskList());
                                    adapter.notifyItemChanged(position);
                                    adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                                    break;
                                case 2:
                                    taskManager.removeTask(position);
                                    taskManager.addTask(position,task);
                                    adapter.setTaskList(taskManager.getTaskList());
                                    adapter.notifyItemChanged(position);
                                    break;
                            }
                        }
                    }
                });
        return view;
    }
}
