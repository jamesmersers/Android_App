package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        getWindow().setBackgroundDrawableResource(R.drawable.background);
        getWindow().setDimAmount(0.8f);

        EditText titleEditText = findViewById(R.id.taskTitleEditText);
        EditText rewardEditText = findViewById(R.id.rewardEditText);
        EditText limitCountEditText = findViewById(R.id.taskLimitCountEditText);
        LinearLayout limitCountLayout = findViewById(R.id.taskLimitCountLayout);
        Button buttonEdit = findViewById(R.id.task_edit_button);

        Intent intent =  getIntent();
        int requestCode = getIntent().getIntExtra("requestCode", 0);
        Bundle bundle = intent.getExtras();
        Task task = (Task) bundle.getSerializable("task");
        titleEditText.setText(task.getTitle());
        rewardEditText.setText(task.getReward());
        limitCountEditText.setText(String.valueOf(task.getLimitCount()));
        if(task.getTaskType() == 2){
            limitCountLayout.setVisibility(View.GONE);
        }
        buttonEdit.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String reward = rewardEditText.getText().toString();
            int limitCount = 1;
            switch (task.getTaskType()) {
                case 0:
                case 1:
                    if(limitCountEditText.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "请输入任务数量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    limitCount = Integer.parseInt(limitCountEditText.getText().toString());
                    break;
            }
            int finalLimitCount = limitCount;
            if(title.isEmpty() || reward.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
            builder.setMessage("确认修改吗？");
            builder.setPositiveButton("确认", (dialog, which) -> {
                task.setTitle(title);
                task.setReward(Integer.parseInt(reward));
                task.setLimitCount(finalLimitCount);
                Bundle resultBundle = new Bundle();
                resultBundle.putSerializable("task", task);
                intent.putExtra("requestCode", requestCode);
                setResult(RESULT_OK, intent);
                finish();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }
}