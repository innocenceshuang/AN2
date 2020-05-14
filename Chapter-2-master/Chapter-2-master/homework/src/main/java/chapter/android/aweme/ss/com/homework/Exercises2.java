package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * getclassname()
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        ViewGroup rootnode=(ViewGroup) getLayoutInflater().inflate(R.layout.activity_exercise2,null);
//        View rootlayout=rootnode.getChildAt(0);
        int result = getAllChildViewCount(rootnode);
        TextView sumNum=findViewById(R.id.viewnum);
        sumNum.setText(String.valueOf(result));
    }

    public int getAllChildViewCount(ViewGroup view) {
        //todo 补全你的代码
        int count = 0;
        for(int x = 0; x < view.getChildCount(); x++)
        {
            if(view.getChildAt(x) instanceof ViewGroup) {
                count+=getAllChildViewCount((ViewGroup)view.getChildAt(x));
            }
            count++;
        }
        return count;
    }
}
