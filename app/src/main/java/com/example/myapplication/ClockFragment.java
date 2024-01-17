package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.myapplication.view.CustomClockView;

public class ClockFragment extends Fragment {
    private CustomClockView mClockView;
    private Handler mHandler;
    private Runnable mUpdateRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clockview, container, false);
        mClockView = view.findViewById(R.id.custom_clock_view);
        mHandler = new Handler();
        mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                mClockView.invalidate();
                mHandler.postDelayed(this, 1000);
            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(mUpdateRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateRunnable);
    }
}