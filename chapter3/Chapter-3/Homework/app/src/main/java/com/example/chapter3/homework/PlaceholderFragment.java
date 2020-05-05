package com.example.chapter3.homework;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {
    private ArrayAdapter<String> adapterItems;
    private ListView lvItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View view = inflater.inflate(R.layout.fragment_placeholder, container,
                false);
//        View lvItems=getView().findViewById(R.id.lvItems);
//        ObjectAnimator alphaAni2 = ObjectAnimator.ofFloat(lvItems, "alpha", 1.0f, 0f);
//        alphaAni2.setDuration(10);
//        alphaAni2.setInterpolator(new LinearInterpolator());
//        alphaAni2.setRepeatCount(0);
//        alphaAni2.start();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入

                View animation_view=getView().findViewById(R.id.animation_view);
                ObjectAnimator alphaAni = ObjectAnimator.ofFloat(animation_view, "alpha", 1.0f, 0f);
                alphaAni.setRepeatCount(0);
                alphaAni.setInterpolator(new LinearInterpolator());
                alphaAni.setDuration(500);
                alphaAni.start();

                View lvItems=getView().findViewById(R.id.lvItems);
                ObjectAnimator alphaAni2 = ObjectAnimator.ofFloat(lvItems, "alpha", 0, 1.0f);
                alphaAni2.setDuration(500);
                alphaAni2.setInterpolator(new LinearInterpolator());
                alphaAni2.setRepeatCount(0);
                alphaAni2.start();
            }
        }, 5000);
    }

}
