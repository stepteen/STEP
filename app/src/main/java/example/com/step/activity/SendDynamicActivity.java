package example.com.step.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.bean.MessageBean;
import example.com.step.bean.RunScoresBeans;
import example.com.step.bean.Synamic;
import example.com.step.bean.SynamicItemBean;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.fragment.MydynamicFragment;
import example.com.step.util.DlgLoading;
import example.com.step.util.FileandBitmap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.baidu.mapapi.UIMsg.l_ErrorNo.REQUEST_OK;
import static vi.com.gdi.bgl.android.java.EnvDrawText.bmp;

/**
 * Created by qinghua on 2016/11/14.
 */

public class SendDynamicActivity extends Activity implements View.OnClickListener{

    private SharedPreferences sp;
    private User myuser;//获取用户的信息
    private FileandBitmap filebitmap=new FileandBitmap();
    private Synamic synamic=new Synamic();//存放动态消息的实体
    private TextView tv_senddynamic,tv_dynamicmessage;
    private String picturePath;
    private ImageView iv_addImage;
    private static int RESULT_LOAD_IMAGE = 1;
    private String str_dynamicpicture;
    private DlgLoading dlgLoading;
    private String filePath;//选择的图片路径
    private RunScoresBeans Rrun_date=new RunScoresBeans();//存放可能从历史轨迹活动传递过来的跑步数据
    private Context context;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case 0:
                    Toast.makeText(SendDynamicActivity.this,"发布失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(SendDynamicActivity.this,"发布成功!",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(SendDynamicActivity.this,"网络异常!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senddynamic);
        initView();
        getDate();
    }

    /**
     * 获取可能从历史轨迹分享过来的数据
     */
    private void getDate() {

        Intent intent=getIntent();
        if(intent!=null)
        {
            Rrun_date= (RunScoresBeans) intent.getSerializableExtra("Rrun_date");
            filePath= (String) intent.getSerializableExtra("filePath");
            iv_addImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if(filePath!=null)
            {
                Bitmap bitmap=readImg();
                if(bitmap!=null){
                    str_dynamicpicture= filebitmap.convertIconToString(bitmap);
                }

                iv_addImage.setImageBitmap(bitmap);
                tv_dynamicmessage.setText("我跑了"+Rrun_date.getRun_Distance()+"米，用时"+Rrun_date.getTimeLong()+",平均速度"+Rrun_date.getAverage_speed()+"km/h");
            }


        }

    }

    private void initView() {
        context=SendDynamicActivity.this;
        dlgLoading=new DlgLoading(this);
        iv_addImage= (ImageView) findViewById(R.id.add_image);
        iv_addImage.setOnClickListener(this);
        tv_senddynamic= (TextView) findViewById(R.id.tv_senddynamic);
        tv_senddynamic.setOnClickListener(this);
        tv_dynamicmessage= (TextView) findViewById(R.id.tv_dynamicmessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.add_image:
                showTwoWays();
                break;
            case R.id.tv_senddynamic:
                initUsrData();//将数据添加到消息实体中
                postAsynHttp();
                break;
        }
    }

    private void addMessabeData(){
        synamic.setDescription(tv_dynamicmessage.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1) {
            if (data != null) {
                Uri uri = data.getData();
                getImg(uri);
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            if (data != null) {
                Uri photoUri=data.getData();
                Bitmap bitmap =getBitmapFromUri(photoUri, context);
                str_dynamicpicture=filebitmap.convertIconToString(bitmap);
                if(!str_dynamicpicture.isEmpty())
                {
                    iv_addImage.setImageBitmap(bitmap);
                }
            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                str_dynamicpicture=filebitmap.convertIconToString(bitmap);
                if(!str_dynamicpicture.isEmpty())
                {
                    iv_addImage.setImageBitmap(bitmap);
                }
            } else {
                return;
            }
        }

    }
    // 读取uri所在的图片
    public  Bitmap getBitmapFromUri(Uri uri,Context mContext)
    {
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    //读取位图（图片）
    private Bitmap readImg() {
        File mfile = new File(filePath);
        Bitmap bm = null;
        if (mfile.exists()) {        //若该文件存在
            bm = BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }

    private void getImg(Uri uri) {
        try {
            InputStream inputStream =context.getContentResolver().openInputStream(uri);
            cutImg(uri);
            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //裁剪图片
    private void cutImg(Uri uri) {
        if (uri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //true:出现裁剪的框
            intent.putExtra("crop", "true");
            //裁剪宽高时的比例
            intent.putExtra("aspectX", 1.5);
            intent.putExtra("aspectY", 1);
            //裁剪后的图片的大小
            WindowManager wm = (WindowManager) SendDynamicActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getWidth();
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);  // 返回数据
            intent.putExtra("output", uri);
            intent.putExtra("scale", true);
            startActivityForResult(intent, 0x2);
        } else {
            return;
        }
    }


    /**
     * 显示两个提示的对话框
     */
    private void showTwoWays()
    {
        LayoutInflater inflater = LayoutInflater.from(SendDynamicActivity.this);
        View altview = inflater.inflate(R.layout.showtwoways, null);
        TextView tv_xiangjipaishe= (TextView) altview.findViewById(R.id.xiangjipaishe);
        TextView tv_phonePhoto= (TextView) altview.findViewById(R.id.phonePhoto);
        AlertDialog.Builder builder = new AlertDialog.Builder(SendDynamicActivity.this);
        builder.setView(altview);
        final AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框
        tv_xiangjipaishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0x3);
                dialog.dismiss();
            }
        });

        tv_phonePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开手机的图库;
                Intent intent;
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0x1);
                dialog.dismiss();
            }
        });
    }


    /**
     * post请求服务器
     */
    private void postAsynHttp() {
        dlgLoading.show("发表中");
        String url= ContractData.URL+"/synamic/user/RunningRing/save";
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.SYNAMIC, JSON.toJSONString(synamic))
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
                new Thread()
                {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                        super.run();
                    }
                }.start();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    new Thread()
                    {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                            super.run();
                        }
                    }.start();
                }
                else
                {
                    new Thread()
                    {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                            super.run();
                        }
                    }.start();
                }
            }
        });
    }

    private void initUsrData()
    {
        sp = getApplicationContext().getSharedPreferences("User",getApplicationContext().MODE_PRIVATE);
        String str_user=sp.getString("userInfo","");
        myuser= JSON.parseObject(str_user,User.class);//获取用户信息
        synamic.setCreatorName(myuser.getUserName());
        synamic.setCreatorNo(myuser.getUserId()+"");
        synamic.setUserHeadPicture(myuser.getUserHeadPicture());
        synamic.setDescription(tv_dynamicmessage.getText().toString());
        synamic.setPicture(str_dynamicpicture);
    }

}
