package example.com.step.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.adapter.CommentAdapter;
import example.com.step.bean.CommentEntity;
import example.com.step.bean.Synamic;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.util.DlgLoading;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qinghua on 2016/12/3.
 */

public class DetailSynamicActivity extends Activity implements View.OnClickListener{

    private SharedPreferences sp;
    private User myuser=new User();//获取用户的信息
    private ListView commentlistView;
    private EditText et_comment_content;
    private Button btn_SendComment;
    private Synamic synamic=new Synamic();
    private CommentEntity commentEntity=new CommentEntity();
    private List<CommentEntity> commentData;
    private CommentAdapter commentAdapter;
    private DlgLoading dlgLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_synamic);
        ObtainTransSynamic();
        ObtainUser();
        initView();

    }

    private void ObtainTransSynamic() {
        Intent intent=getIntent();
        synamic= (Synamic) intent.getSerializableExtra("Synamic");
    }

    private void initView() {
        sp = getApplicationContext().getSharedPreferences("User",MODE_PRIVATE);
        dlgLoading=new DlgLoading(DetailSynamicActivity.this);
        et_comment_content= (EditText) findViewById(R.id.et_comment_content);
        commentlistView= (ListView) findViewById(R.id.detail_comment_content);
        btn_SendComment= (Button) findViewById(R.id.btn_SendComment);
        btn_SendComment.setOnClickListener(this);
        commentData=new ArrayList<CommentEntity>();
        ObtainComment();
        commentAdapter=new CommentAdapter(this,commentData,synamic,commentlistView,et_comment_content);
        commentlistView.setAdapter(commentAdapter);
    }

    private void addData() {
        for(int i=0;i<10;i++)
        {
            commentData.add(new CommentEntity("威斯布鲁克","连续四场3双"));
        }
    }

    private void ObtainUser()
    {
        sp = getApplicationContext().getSharedPreferences("User",getApplicationContext().MODE_PRIVATE);
        String str_user=sp.getString("userInfo","");
        myuser= JSON.parseObject(str_user,User.class);//获取用户信息

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_SendComment:
                if(!et_comment_content.getText().toString().isEmpty())
                {
                    commentEntity.setSynamicId(synamic.getRingId());
                    commentEntity.setObserverId(myuser.getUserId()+"");
                    commentEntity.setReceiverId(synamic.getCreatorNo()+"");
                    commentEntity.setComment_content(et_comment_content.getText().toString());
                    commentEntity.setObserverName(myuser.getUserName());
                    commentEntity.setReceiverName(synamic.getCreatorName());
                    commentData.add(commentEntity);
                    commentAdapter.notifyDataSetChanged();
                }
                SendComment(commentEntity);
                /**
                 * 显示输入法弹出
                 */
                InputMethodManager m= (InputMethodManager) et_comment_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.hideSoftInputFromWindow(et_comment_content.getWindowToken(),0);
            break;
        }
    }

    private void ObtainComment()
    {
        dlgLoading.show("正在初始化数据");
        String url= ContractData.URL+"/comment/user/comment/getcomment";
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.SYNAMICID,synamic.getRingId()+"")
                .add(ContractData.ACCESSTOKEN, sp.getString("accessToken",""))
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
                        Toast.makeText(DetailSynamicActivity.this,"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    Log.d("xyz",(String) data.getData());
                    commentData.clear();
                    commentData.addAll(JSONObject.parseArray((String) data.getData(), CommentEntity.class));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailSynamicActivity.this,"获取评论失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void SendComment(CommentEntity commentEntity)
    {
        dlgLoading.show("正在初始化数据");
        Log.d("xyz",JSON.toJSONString(commentEntity));
        String url=ContractData.URL+"/comment/user/comment/save";
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.COMMENT,JSON.toJSONString(commentEntity))
                .add(ContractData.ACCESSTOKEN, sp.getString("accessToken",""))
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
                        Toast.makeText(DetailSynamicActivity.this,"网络异常！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailSynamicActivity.this,"发表评论成功！",Toast.LENGTH_SHORT).show();
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailSynamicActivity.this,"发表评论失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
