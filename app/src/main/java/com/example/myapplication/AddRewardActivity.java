package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);

        Intent intent = new Intent();
        int requestCode = getIntent().getIntExtra("requestCode", 0);

        EditText titleEditText = findViewById(R.id.rewardTitleEditText);
        EditText costEditText = findViewById(R.id.costEditText);
        EditText limitCountEditText = findViewById(R.id.rewardLimitCountEditText);
        Button buttonAdd = findViewById(R.id.add_reward_button);

        buttonAdd.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String cost = costEditText.getText().toString();
            String limitCount = limitCountEditText.getText().toString();
            if(title.isEmpty() || cost.isEmpty() || limitCount.isEmpty()){
                Toast.makeText(getApplicationContext(), "请输入", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(AddRewardActivity.this);
            builder.setMessage("确认添加吗？");
            builder.setPositiveButton("确认", (dialog, which) -> {
                intent.putExtra("requestCode", requestCode);
                Reward reward = new Reward(title, Integer.parseInt(cost), Integer.parseInt(limitCount));
                Bundle bundle = new Bundle();
                bundle.putSerializable("reward",reward);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }
}