package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    public class MyPagerAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 4;
        public MyPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TaskViewFragment();
                case 1:
                    return new RewardViewFragment();
                case 2:
                case 3:
                    return new StatisticsViewFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        getWindow().setBackgroundDrawableResource(R.drawable.background);
        getWindow().setDimAmount(0.8f);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("任务");
                            break;
                        case 1:
                            tab.setText("奖励");
                            break;
                        case 2:
                            tab.setText("统计");
                            break;
                        case 3:
                            tab.setText("更多");
                            break;
                    }
                }
        ).attach();
    }
}