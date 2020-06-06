package com.example.shakingparrot.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shakingparrot.R;
import com.example.shakingparrot.adapter.ViewPagerAdapter;
import com.example.shakingparrot.data.VideoSource;

import java.util.List;

public class PlayFragment extends Fragment {

    private View view;
    private List<VideoSource> videoSourceList;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter adapter;

    public PlayFragment(int pos, List<VideoSource> videoSourceList) {
        // Required empty public constructor

        this.videoSourceList = videoSourceList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play, container, false);
        initView();
        return view;
    }

    private void initView()
    {
        viewPager2 = view.findViewById(R.id.viewpager2);
        adapter = new ViewPagerAdapter(videoSourceList, view.getContext());
        viewPager2.setAdapter(adapter);
    }
}

