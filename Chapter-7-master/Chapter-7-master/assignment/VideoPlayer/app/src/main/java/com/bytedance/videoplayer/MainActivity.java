package com.bytedance.videoplayer;


import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private TextView sumTime;
    private TextView nowTime;
    private final Handler handler = new Handler();
    private int progress;
    private int position;
    private Runnable runnable;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MediaPlayer");
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surface);
        sumTime = findViewById(R.id.sum_time);
        nowTime = findViewById(R.id.now_time);
        seekBar = findViewById(R.id.playController);

        player = new MediaPlayer();
        try {
            player.setDataSource(getResources().openRawResourceFd(R.raw.bytedance));
            holder = surfaceView.getHolder();
            holder.addCallback(new PlayerCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 自动播放
                    player.start();
                    player.setLooping(true);
                    sumTime.setText(String.valueOf(mp.getDuration()/1000));

                }
            });
            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    changeVideoSize(mp);
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });

        findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int b, boolean fromUser) {
                progress = b * player.getDuration()/seekBar.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(progress);
            }
        });
        runnable = new Runnable() {
            @Override
            public void run() {
                position = player.getCurrentPosition();
                int duration = player.getDuration();
                if(duration > 0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nowTime.setText(String.valueOf(position/1000));
                        }
                    });
                    long pos = seekBar.getMax() * position / duration;
                    seekBar.setProgress((int)pos);
                }
                handler.postDelayed(this,500);
            }
        };
        handler.postDelayed(runnable, 200);
    }

    public void changeVideoSize(MediaPlayer mediaPlayer){
        SurfaceView surface = findViewById(R.id.surface);
        int surfaceWidth = surface.getWidth();
        int surfaceHeight = surface.getHeight();

        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        LinearLayout linearLayout = findViewById(R.id.parent_layout);
        int parentWidth = linearLayout.getWidth();
        int parentHeight = linearLayout.getHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
            videoWidth = (int) Math.ceil((float) videoWidth / max);
            videoHeight = (int) Math.ceil((float) videoHeight / max);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) parentHeight), (float) videoHeight / (float) parentWidth);
            videoHeight=parentHeight;
            videoWidth=parentWidth;
        }
//        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
//        videoWidth = (int) Math.ceil((float) videoWidth / max);
//        videoHeight = (int) Math.ceil((float) videoHeight / max);
        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。

        surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}

