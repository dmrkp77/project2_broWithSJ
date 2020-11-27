package com.project2.bodycheck.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.project2.bodycheck.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    int image[] = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ImageFragment().newInstance(image[position]);
    }

    @Override
    public int getCount() {
        return image.length;
    }
}
