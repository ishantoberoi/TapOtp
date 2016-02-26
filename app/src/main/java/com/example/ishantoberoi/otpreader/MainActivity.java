package com.example.ishantoberoi.otpreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.FragmentPagerAdapter;

import com.example.ishantoberoi.otpreader.IntroScreens.CopyToClipboard;
import com.example.ishantoberoi.otpreader.IntroScreens.MovableAlwaysOn;
import com.example.ishantoberoi.otpreader.IntroScreens.SalientFreatures;
import com.example.ishantoberoi.otpreader.IntroScreens.ShareTheCode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
    }



    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SalientFreatures();
                case 1:
                    return new CopyToClipboard();
                case 2:
                    return new ShareTheCode();
                case 3:
                    return new MovableAlwaysOn();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}



