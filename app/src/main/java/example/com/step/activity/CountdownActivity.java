package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import example.com.step.R;

/**
 * Created by qinghua on 2016/11/6.
 */

public class CountdownActivity extends Activity{

    private TextView tv_nember;//计数
    private int count=3;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_run);
        Log.d("xyz","3");
        tv_nember= (TextView) findViewById(R.id.tv_count);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_text);
        tv_nember.startAnimation(animation);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                int str=getCount();
                if(count>=0) {
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
                if(str>=0)
                {
                    tv_nember.setText(str+"");
                }
                animation.reset();
                tv_nember.startAnimation(animation);
            }

        };

    };
    /**
     * 实现计数减一
     */
    private int getCount() {
        count--;
        if (count <0) {
            Intent intent=new Intent(getApplicationContext(),RunActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }

}
