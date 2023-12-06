package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = new Intent();
        int requestCode = getIntent().getIntExtra("requestCode", 0);

        TextView limitCountTextView = findViewById(R.id.taskLimitCountTextView);
        TextView tipsTextView = findViewById(R.id.tipsTaskTextView);
        EditText titleEditText = findViewById(R.id.taskTitleEditText);
        EditText rewardEditText = findViewById(R.id.rewardEditText);
        EditText limitCountEditText = findViewById(R.id.taskLimitCountEditText);
        LinearLayout limitCountLayout = findViewById(R.id.taskLimitCountLayout);
        LinearLayout daysOfWeekLayout = findViewById(R.id.daysOfWeekLayout);
        Spinner taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
        Button buttonAdd = findViewById(R.id.add_task_button);

        List<Integer> selectedDays = new ArrayList<>();

        taskTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTaskType = parent.getItemAtPosition(position).toString();
                switch (selectedTaskType) {
                    case "普通任务":
                        limitCountLayout.setVisibility(View.VISIBLE);
                        daysOfWeekLayout.setVisibility(View.GONE);
                        tipsTextView.setVisibility(View.VISIBLE);
                        break;
                    case "每日任务":
                        limitCountTextView.setText("每日任务数量：");
                        limitCountLayout.setVisibility(View.VISIBLE);
                        daysOfWeekLayout.setVisibility(View.GONE);
                        tipsTextView.setVisibility(View.VISIBLE);
                        break;
                    case "每周任务":
                        limitCountLayout.setVisibility(View.GONE);
                        daysOfWeekLayout.setVisibility(View.VISIBLE);
                        tipsTextView.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        CompoundButton.OnCheckedChangeListener onDaySelectedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int day = -1;
                switch (buttonView.getId()) {
                    case R.id.mondayCheckBox:
                        day = Calendar.MONDAY;
                        break;
                    case R.id.tuesdayCheckBox:
                        day = Calendar.TUESDAY;
                        break;
                    case R.id.wednesdayCheckBox:
                        day = Calendar.WEDNESDAY;
                        break;
                    case R.id.thursdayCheckBox:
                        day = Calendar.THURSDAY;
                        break;
                    case R.id.fridayCheckBox:
                        day = Calendar.FRIDAY;
                        break;
                    case R.id.saturdayCheckBox:
                        day = Calendar.SATURDAY;
                        break;
                    case R.id.sundayCheckBox:
                        day = Calendar.SUNDAY;
                        break;
                }
                if (isChecked) {
                    if (!selectedDays.contains(day)) {
                        selectedDays.add(day);
                    }
                }
                else {
                    selectedDays.remove(Integer.valueOf(day));
                }
            }
        };

        for (int i = 0; i < daysOfWeekLayout.getChildCount(); i++) {
            View childView = daysOfWeekLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                checkBox.setOnCheckedChangeListener(onDaySelectedListener);
            }
        }

        buttonAdd.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String reward = rewardEditText.getText().toString();
            String stringTaskType = taskTypeSpinner.getSelectedItem().toString();
            int limitCount = 1;
            int taskType = 0;
            switch (stringTaskType) {
                case "普通任务":
                    limitCount = Integer.parseInt(limitCountEditText.getText().toString());
                    break;
                case "每日任务":
                    limitCount = Integer.parseInt(limitCountEditText.getText().toString());
                    taskType = 1;
                    break;
                case "每周任务":
                    taskType = 2;
                    break;
            }
            if(title.isEmpty() || reward.isEmpty()){
                Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
            builder.setMessage("确认添加吗？");
            int finalTaskType = taskType;
            int finalLimitCount = limitCount;
            builder.setPositiveButton("确认", (dialog, which) -> {
                intent.putExtra("requestCode", requestCode);
                Task task = new Task();
                switch(finalTaskType){
                    case 0:
                    case 1:
                        task = new Task(title, Integer.parseInt(reward), finalTaskType, finalLimitCount, selectedDays);
                        break;
                    case 2:
                        task = new Task(title, Integer.parseInt(reward), finalTaskType, selectedDays.size(), selectedDays);
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("task",task);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }
}