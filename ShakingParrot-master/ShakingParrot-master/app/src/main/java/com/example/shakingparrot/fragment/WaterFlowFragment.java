package com.example.shakingparrot.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.bumptech.glide.request.transition.Transition;
import com.example.shakingparrot.MainActivity;
import com.example.shakingparrot.R;
import com.example.shakingparrot.adapter.VideoAdapter;
import com.example.shakingparrot.data.VideoSource;
import com.example.shakingparrot.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterFlowFragment extends Fragment {

    View view;
    private List<VideoSource> videoSourceList;
    private VideoAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private RecyclerView recyclerView;

    public WaterFlowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_water_flow, container, false);

        initData();
        initView();

        return view;
    }

    private void initData()
    {
        videoSourceList = new ArrayList<>();
        Request();
        adapter = new VideoAdapter(videoSourceList, view.getContext());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    private void initView()
    {
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void Request()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideoSource().enqueue(new Callback<List<VideoSource>>() {
            @Override
            public void onResponse(Call<List<VideoSource>> call, Response<List<VideoSource>> response) {
                Log.d("API","got response");
                if (response.body() != null) {
                    videoSourceList = response.body();
                    adapter.setData(videoSourceList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<VideoSource>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });
    }

    private List<VideoSource> reorderSourceList(int pos)
    {
        List<VideoSource> result = new ArrayList<>();
        result.addAll(videoSourceList);

        for (int i = 0; i < pos; i++)
        {
            VideoSource videoSource = result.get(0);
            result.remove(0);
            result.add(videoSource);
        }
        return  result;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                WaterFlowFragment waterFlowFragment = (WaterFlowFragment) fragmentManager.findFragmentById(R.id.fragment);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(waterFlowFragment).add(R.id.fragment, new PlayFragment(position, reorderSourceList(position)));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }
}
