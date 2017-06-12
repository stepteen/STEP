package example.com.step.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.service.PushTestReceiver;
import example.com.step.util.DlgLoading;
import example.com.step.util.SharedPreferenceUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qinghua on 2016/12/6.
 */

public class UpdateInformationActivity  extends Activity  implements View.OnClickListener{
    private User myuser;
    private ImageView iv_head_bg;
    private TextView tv_fanhui,tv_xiugai;
    private User user=new User();
    private EditText et_nickname,et_high,et_weight,et_age,et_sex;
    private DlgLoading dlgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateinformationactivity);
        initView();
        setUserData();
    }

    private void initView() {
        dlgLoading=new DlgLoading(this);
        myuser= JSON.parseObject(SharedPreferenceUtil.getDate("userInfo"),User.class);;//获取用户信息
        tv_fanhui= (TextView) findViewById(R.id.fanhui);
        tv_xiugai= (TextView) findViewById(R.id.xiugai);
        et_nickname= (EditText) findViewById(R.id.et_nickname);
        et_high= (EditText) findViewById(R.id.et_high);
        et_weight= (EditText) findViewById(R.id.et_weight);
        et_age= (EditText) findViewById(R.id.et_age);
        et_sex= (EditText) findViewById(R.id.et_sex);
        iv_head_bg= (ImageView) findViewById(R.id.iv_headpicture);
        tv_fanhui.setOnClickListener(this);
        tv_xiugai.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.fanhui:
                finish();
                break;
            case R.id.xiugai:
                SaveInformation();
                break;
        }
    }


    /**
     * 设置用户数据
     */
    private void setUserData() {
        if(myuser.getUserHeadPicture()!=null){
            obtainPicturre(myuser.getUserHeadPicture());
        }
        if(myuser.getUserName()!=null){
            et_nickname.setText(myuser.getUserName());
        }
        if(myuser.getUserAge()!=null){
            et_age.setText(myuser.getUserAge().toString());
        }
        if(myuser.getUserHeight()!=null){
            et_high.setText(myuser.getUserHeight());
        }
        if(myuser.getUserWeight()!=null){
            et_weight.setText(myuser.getUserWeight());
        }
        if(myuser.getUserSex()!=null){
            et_sex.setText(myuser.getUserSex());
        }
    }

    /**
     * 保存修改的用户信息
     */
    private void SaveInformation()
    {
        dlgLoading.show("保存用户信息中...");
        String url=ContractData.URL+"/user/perfectormodify";
        updateUserData();
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.USER, JSON.toJSONString(user))
                .add(ContractData.ACCESSTOKEN, SharedPreferenceUtil.getDate("accessToken"))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dlgLoading.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateInformationActivity.this,"网络异常!",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                String str=response.body().string();
                Log.d("zzz",str);

                if(parseResultData(str)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateInformationActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
                            SharedPreferenceUtil.CommitDate("userInfo",JSON.toJSONString(user));
                            finish();
                        }
                    });

                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateInformationActivity.this,"修改失败!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    boolean parseResultData(String resultData) {
        //先将返回值解析为ResultData
        ResultData data = ResultDataUtils.paraResult(resultData);
        Log.d("zqh","1");
        if (data.getStatus().equals(ResultData.ERROR)) {
            return false;
        }
        //保存accesstoken 和usermsg
        SharedPreferences sp = getSharedPreferences("User",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putString("userInfo",data.getData().toString());
        editor.commit();
        Log.d("xyz","用户信息：" + data.getData().toString());
        return true;
    }
    /**
     * 获取用户修改信息
     */
    private void updateUserData() {
        if(user.getPhoneNum()!=null){
            user.setPhoneNum(user.getPhoneNum());
        }
        if(PushTestReceiver.getChannelId()!=null){
            user.setChannelId(PushTestReceiver.getChannelId());
        }
        if(myuser.getUserPwd()!=null){
            user.setUserPwd(myuser.getUserPwd());
        }
        if(user.getUserId()!=null){
            user.setUserId(myuser.getUserId());
        }

        user.setUserName(et_nickname.getText().toString());
        user.setUserHeight(et_high.getText().toString());
        user.setUserWeight(et_weight.getText().toString());
        user.setUserSex(et_sex.getText().toString());
        user.setUserAge(Integer.parseInt(et_age.getText().toString()));
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
