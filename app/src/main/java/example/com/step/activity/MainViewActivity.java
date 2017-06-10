package example.com.step.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.step.R;
import example.com.step.fragment.FindFragment;
import example.com.step.fragment.MessageFragment;
import example.com.step.fragment.MyFragment;
import example.com.step.fragment.PlaygroundFragment;
import example.com.step.fragment.SportsFragment;

public class MainViewActivity extends FragmentActivity implements View.OnClickListener {

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
}
