package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import example.com.step.R;
import example.com.step.bean.User;
import example.com.step.util.SharedPreferenceUtil;

/**
 * Created by qinghua on 2016/11/26.
 */

public class MyInformationActivity extends Activity implements View.OnClickListener {
    private TextView tv_bianji,tv_return;
    private TextView tv_name,tv_heigh,tv_weight,tv_age,tv_sex;
    private ImageView iv_head_bg;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydetailinformation);
        initView();
    }

    /**
     * 设置用户数据
     */
    private void setUserData() {
        if(user.getUserName()!=null){
            tv_name.setText(user.getUserName());
        }
        if(user.getUserAge()!=null){
            tv_age.setText(user.getUserAge().toString());
        }
        if(user.getUserHeight()!=null){
           tv_heigh.setText(user.getUserHeight());
        }
        if(user.getUserWeight()!=null){
           tv_weight.setText(user.getUserWeight());
        }
        if(user.getUserSex()!=null){
        tv_sex.setText(user.getUserSex());
        }
    }

    private void initView() {

        iv_head_bg= (ImageView)findViewById(R.id.head_bg);

        tv_bianji= (TextView) findViewById(R.id.bianji);
        tv_return= (TextView) findViewById(R.id.fanhui);
        tv_bianji.setOnClickListener(this);
        tv_return.setOnClickListener(this);


        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_heigh= (TextView) findViewById(R.id.tv_heigh);
        tv_weight= (TextView) findViewById(R.id.tv_weight);
        tv_age= (TextView) findViewById(R.id.tv_age);
        tv_sex= (TextView) findViewById(R.id.tv_sex);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.bianji:
                Intent intent=new Intent(MyInformationActivity.this,UpdateInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.fanhui:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user= JSON.parseObject(SharedPreferenceUtil.getDate("userInfo"),User.class);
        if(user.getUserHeadPicture()!=null){
            obtainPicturre(user.getUserHeadPicture());
        }
        setUserData();
    }


    /**
     * 获取头像
     */
    private void obtainPicturre(String url) {
        Picasso.with(this)
                .load(url)
                .into(iv_head_bg);
    }
}
