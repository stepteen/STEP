package example.com.step.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import example.com.step.R;
import example.com.step.fragment.FindFragment;
import example.com.step.fragment.MessageFragment;
import example.com.step.fragment.MyFragment;
import example.com.step.fragment.PlaygroundFragment;
import example.com.step.fragment.SportsFragment;

public class MainViewActivity extends FragmentActivity implements View.OnClickListener {

    private final int GPS_REQUEST_CODE=1;

    private TextView tv_playground,tv_find,tv_sports,tv_message,tv_my;
    private ImageView iv_playground,iv_find,iv_sports,iv_message,iv_my;

    private LinearLayout ll_playground,ll_find,ll_sports,ll_message,ll_my;
    private Fragment playgroundFragment,findFragment,sportsFragment,messageFragment,myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_view);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        openGPSSettings();
    }

    private void setStatusBar() {
        //全透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        setLinearLayoutId();
        setButtonId();
        setclick();
        setSelect(0);

    }

    /**
     * 绑定LinearLayout的ID
     */
    private void setLinearLayoutId() {
        ll_playground= (LinearLayout) findViewById(R.id.ll_playground);
        ll_find= (LinearLayout) findViewById(R.id.ll_find);
        ll_sports= (LinearLayout) findViewById(R.id.ll_sports);
        ll_message= (LinearLayout) findViewById(R.id.ll_message);
        ll_my= (LinearLayout) findViewById(R.id.ll_my);
    }

    /**
     * 绑定Button的ID
     */
    private void setButtonId() {

        tv_playground= (TextView) findViewById(R.id.tv_playground);
        tv_find= (TextView) findViewById(R.id.tv_find);
        tv_sports= (TextView) findViewById(R.id.tv_sports);
        tv_message= (TextView) findViewById(R.id.tv_message);
        tv_my= (TextView) findViewById(R.id.tv_my);

        iv_playground= (ImageView) findViewById(R.id.iv_playground);
        iv_find= (ImageView) findViewById(R.id.iv_find);
        iv_sports= (ImageView) findViewById(R.id.iv_sports);
        iv_message= (ImageView) findViewById(R.id.iv_message);
        iv_my= (ImageView) findViewById(R.id.iv_my);
    }

    /**
     * 设置监听器
     */
    private void setclick() {

        ll_playground.setOnClickListener(this);
        ll_find.setOnClickListener(this);
        ll_sports.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_my.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        setbg();
        switch (view.getId())
        {
            case R.id.ll_playground:
                setSelect(0);
                break;
            case R.id.ll_find:
                setSelect(1);
                break;
            case R.id.ll_sports:
                setSelect(2);
                break;
            case R.id.ll_message:
                setSelect(3);
                break;
            case R.id.ll_my:
                setSelect(4);
                break;
        }
    }

    private void setChangeBg(int i) {
        switch (i)
        {
            case 0:
                tv_playground.setTextColor(getResources().getColor(R.color.blue));
                iv_playground.setBackgroundResource(R.mipmap.play1);
                break;
            case 1:
                tv_find.setTextColor(getResources().getColor(R.color.blue));
                iv_find.setBackgroundResource(R.mipmap.find1);
                break;
            case 2:
                tv_sports.setTextColor(getResources().getColor(R.color.blue));
                iv_sports.setBackgroundResource(R.mipmap.run1);
                break;
            case 3:
                tv_message.setTextColor(getResources().getColor(R.color.blue));
                iv_message.setBackgroundResource(R.mipmap.chat1);
                break;
            case 4:
                tv_my.setTextColor(getResources().getColor(R.color.blue));
                iv_my.setBackgroundResource(R.mipmap.my1);
                break;
        }
    }

    private void setbg() {
        tv_playground.setTextColor(getResources().getColor(R.color.gray));
        tv_find.setTextColor(getResources().getColor(R.color.gray));
        tv_sports.setTextColor(getResources().getColor(R.color.gray));
        tv_message.setTextColor(getResources().getColor(R.color.gray));
        tv_my.setTextColor(getResources().getColor(R.color.gray));

        iv_playground.setBackgroundResource(R.mipmap.play);
        iv_find.setBackgroundResource(R.mipmap.find);
        iv_sports.setBackgroundResource(R.mipmap.run);
        iv_message.setBackgroundResource(R.mipmap.chat);
        iv_my.setBackgroundResource(R.mipmap.my);
    }

    private void hideFragment(FragmentTransaction transaction)
    {
        if (playgroundFragment!= null)
        {
            transaction.hide(playgroundFragment);
        }
        if (findFragment != null)
        {
            transaction.hide(findFragment);
        }
        if (sportsFragment != null)
        {
            transaction.hide(sportsFragment);
        }
        if (messageFragment != null)
        {
            transaction.hide(messageFragment);
        }
        if (myFragment != null)
        {
            transaction.hide(myFragment);
        }
    }
    private void setSelect(int i)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i)
        {
            case 0:
                if (playgroundFragment == null)
                {
                    playgroundFragment = new PlaygroundFragment();
                    transaction.add(R.id.id_content, playgroundFragment);
                }
                else
                {
                    transaction.show(playgroundFragment);
                }
                setChangeBg(0);
                break;
            case 1:
                if (findFragment == null)
                {
                    findFragment = new FindFragment();
                    transaction.add(R.id.id_content, findFragment);
                } else
                {
                    transaction.show(findFragment);
                }
                setChangeBg(1);
                break;
            case 2:
                if (sportsFragment == null)
                {
                    sportsFragment = new SportsFragment();
                    transaction.add(R.id.id_content, sportsFragment);
                } else
                {
                    transaction.show(sportsFragment);
                }
                setChangeBg(2);
                break;

            case 3:
                if (messageFragment == null)
                {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.id_content, messageFragment);
                } else
                {
                    transaction.show(messageFragment);
                }
                setChangeBg(3);
                break;

            case 4:
                if (myFragment == null)
                {
                    myFragment = new MyFragment();
                    transaction.add(R.id.id_content, myFragment);
                }
                else
                {
                    transaction.show(myFragment);
                }
                setChangeBg(4);
                break;
        }
        transaction.commit();
    }

    /**
     * GPS设置
     */
    private void openGPSSettings() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(MainViewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainViewActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(MainViewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调，判断用户到底点击是还是否。
        //如果同时申请多个权限，可以for循环遍历
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //写入你需要权限才能使用的方法
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this,"需要获得gps权限",Toast.LENGTH_SHORT).show();
        }
    }
}
