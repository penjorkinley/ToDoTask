package com.example.motherchild_digitalhealth_book;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.motherchild_digitalhealth_book.tab_layout_fragment.Home_fragment;
import com.example.motherchild_digitalhealth_book.tab_layout_fragment.Information_fragment;
import com.example.motherchild_digitalhealth_book.tab_layout_fragment.Reminder_fragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Information_fragment();
            case 2:
                return new Reminder_fragment();
            default:
                return new Home_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
