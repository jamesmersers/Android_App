package com.example.myapplication;

import static com.example.myapplication.MainActivity.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MyViewFragment extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myview, container, false);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    public void onPause() {
        super.onPause();
    }
    public void onResume() {
        super.onResume();
        TextView textView1 = view.findViewById(R.id.rewardBalanceTextView);
        textView1.setText(data.get(0) + "");
        TextView textView2 = view.findViewById(R.id.totalCountTextView);
        textView2.setText(data.get(1)+ "");
    }
}
