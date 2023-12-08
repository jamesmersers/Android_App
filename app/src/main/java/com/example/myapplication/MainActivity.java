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
        private static final int NUM_TABS = 5;
        public MyPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new BookViewFragment();
                case 1:
                    return new WebViewFragment();
                case 2:
                    return new MapViewFragment();
                case 3:
                    return new ClockFragment();
                case 4:
                    return new GameViewFragment();
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

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("图书");
                            break;
                        case 1:
                            tab.setText("新闻");
                            break;
                        case 2:
                            tab.setText("地图");
                            break;
                        case 3:
                            tab.setText("时钟");
                            break;
                        case 4:
                            tab.setText("游戏");
                            break;
                    }
                }
        ).attach();
    }
}