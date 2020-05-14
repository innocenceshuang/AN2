package chapter.android.aweme.ss.com.chapter2;

import android.app.Application;
import android.os.Bundle;

public class createApplication extends Application {
    Bundle overWrite;
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    @Override
    public void onCreate()
    {
        super.onCreate();
        overWrite=new Bundle();
    }

    public void writeLog(String content)
    {
//        String content = mLifecycleDisplay.getText().toString();//当前已有的log 提取出来
        overWrite.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, content); //把内容存储起来
    }
    public Bundle getLog()
    {
        return overWrite;
    }
}
