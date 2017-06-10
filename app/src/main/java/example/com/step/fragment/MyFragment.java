package example.com.step.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.activity.HistoryScores;
import example.com.step.activity.MyInformationActivity;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.util.DlgLoading;
import example.com.step.util.FileandBitmap;
import example.com.step.util.SharedPreferenceUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by qinghua on 2016/9/4.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private FileandBitmap filebitmap=new FileandBitmap();
    private String headPictureString;
    private User myuser=new User();
    private ImageView iv_head_bg;
    private TextView tv_nickname;
    private LinearLayout ll_history_scores,ll_presonMsg;
    private View myView=null;
    private String filePath;
    private DlgLoading dlgLoading;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.myfragment,container,false);

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        myuser= JSON.parseObject(SharedPreferenceUtil.getDate("userInfo"),User.class);//获取用户信息
        SetData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        myuser= JSON.parseObject(SharedPreferenceUtil.getDate("userInfo"),User.class);//获取用户信息
        if(myuser.getUserHeadPicture()!=null){
            obtainPicturre();
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        dlgLoading=new DlgLoading(getContext());

        ll_history_scores= (LinearLayout) myView.findViewById(R.id.ll_history_scores);
        ll_presonMsg= (LinearLayout) myView.findViewById(R.id.ll_personMsg);
        tv_nickname= (TextView) myView.findViewById(R.id.tv_nikename);
        iv_head_bg= (ImageView) myView.findViewById(R.id.head_bg);
        ll_history_scores.setOnClickListener(this);
        ll_presonMsg.setOnClickListener(this);
        iv_head_bg.setOnClickListener(this);
        //保存图片的路径
        filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.png";
        //读取保存的位图（图片）
        Bitmap bitmap1= readImg();
        if(bitmap1!=null){
            iv_head_bg.setImageBitmap(bitmap1);
        }else{
            return;
        }
    }

    /**
     * 设置用户数据
     */
    private void SetData()
    {
        tv_nickname.setText(myuser.getUserName());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId())
        {
            case R.id.ll_history_scores:
               intent=new Intent(getContext(), HistoryScores.class);
                startActivity(intent);
                break;
            case R.id.ll_personMsg:
                intent=new Intent(getContext(), MyInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.head_bg:
                showTwoWays();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Bitmap bitmap =getBitmapFromUri(photoUri, getContext());
                headPictureString=filebitmap.convertIconToString(bitmap);
                if(!headPictureString.isEmpty())
                {
                    changeHeadPost();
                    iv_head_bg.setImageBitmap(bitmap);
                }
            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {
               Bundle bundle = data.getExtras();
               Bitmap bitmap = bundle.getParcelable("data");
                headPictureString=filebitmap.convertIconToString(bitmap);
                if(!headPictureString.isEmpty())
                {
                    changeHeadPost();
                    iv_head_bg.setImageBitmap(bitmap);
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
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
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
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后的图片的大小
            intent.putExtra("outputX", 300);
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View altview = inflater.inflate(R.layout.showtwoways, null);
        TextView tv_xiangjipaishe= (TextView) altview.findViewById(R.id.xiangjipaishe);
        TextView tv_phonePhoto= (TextView) altview.findViewById(R.id.phonePhoto);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
     * 获取头像
     */
    private void obtainPicturre() {
        String url = myuser.getUserHeadPicture();
        Picasso.with(getContext())
                .load(url)
                .into(iv_head_bg);
    }
    /**
     * post请求服务器
     */
    private void changeHeadPost() {
        dlgLoading.show("修改中");
        String url= ContractData.URL+"/user/modifyHeadPicture";
        OkHttpClient mOkHttpClient=new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                 .add("base64String",headPictureString )
                .add("accesstoken", SharedPreferenceUtil.getDate("accessToken"))
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
               Log.d("xyz","网络请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                String str=response.body().string();
                Log.d("zzz",str);
                ResultData data = ResultDataUtils.paraResult(str);
                if(!ResultDataUtils.isError(data)) {
                    Log.d("xyz","修改成功！");
                }
                else
                {
                    Log.d("xyz","修改失败！");
                }
            }
        });
    }
}
