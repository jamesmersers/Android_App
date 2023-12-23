package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reward);

        getWindow().setBackgroundDrawableResource(R.drawable.background);
        getWindow().setDimAmount(0.8f);

        EditText titleEditText = findViewById(R.id.rewardTitleEditText);
        EditText rewardEditText = findViewById(R.id.costEditText);
        EditText limitCountEditText = findViewById(R.id.rewardLimitCountEditText);
        Button buttonEdit = findViewById(R.id.reward_edit_button);

        Intent intent =  getIntent();
        int requestCode = getIntent().getIntExtra("requestCode", 0);
        Bundle bundle = intent.getExtras();
        Reward reward = (Reward) bundle.getSerializable("reward");
        titleEditText.setText(reward.getTitle());
        rewardEditText.setText(reward.getCost() + "");
        limitCountEditText.setText(String.valueOf(reward.getLimitCount()));

        buttonEdit.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String cost = rewardEditText.getText().toString();
            String limitCount = limitCountEditText.getText().toString();
            if(title.isEmpty() || cost.isEmpty() || limitCount.isEmpty()) {
                Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(EditRewardActivity.this);
            builder.setMessage("确认修改吗？");
            builder.setPositiveButton("确认", (dialog, which) -> {
                reward.setTitle(title);
                reward.setCost(Integer.parseInt(cost));
                reward.setLimitCount(Integer.parseInt(limitCount));
                Bundle resultBundle = new Bundle();
                resultBundle.putSerializable("reward", reward);
                intent.putExtra("requestCode", requestCode);
                setResult(RESULT_OK, intent);
                finish();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }
}