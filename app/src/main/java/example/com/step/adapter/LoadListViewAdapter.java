package example.com.step.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.activity.DetailSynamicActivity;
import example.com.step.bean.Synamic;
import example.com.step.bean.Thumb;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.util.AsyncImageLoader;
import example.com.step.widget.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qinghua on 2016/10/6.
 */

public class LoadListViewAdapter extends BaseAdapter {
    private Thumb thumb=new Thumb();//点赞信息的实体
    private User user=new User();//存放用户信息
    private SharedPreferences sp;
    private boolean islike=false;
    private List<Synamic> mylist;
    private LayoutInflater inflater;
    private Context mcontext;
    private  AsyncImageLoader imageLoader; //用来下载图片的类
    private ListView listview=null;
    public LoadListViewAdapter(Context context, List<Synamic> list, ListView mlistView)
    {
        sp =context.getSharedPreferences("User",MODE_PRIVATE);
        String str_user=sp.getString("userInfo","");
        user= JSON.parseObject(str_user,User.class);//获取用户信息
        mylist=list;
        mcontext=context;
        inflater=LayoutInflater.from(context);
        imageLoader=new AsyncImageLoader();
        listview=mlistView;
    }
    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int i) {
        return mylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public LoadListViewAdapter() {
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int index=i;
        Holder holder=null;

        if(view==null)
        {
            holder=new Holder();
            view=inflater.inflate(R.layout.dynamicitem,null);
            holder.duration= (TextView) view.findViewById(R.id.tv_duration);
            holder.img_head= (CircleImageView) view.findViewById(R.id.img_head);
            holder.img_bg= (ImageView) view.findViewById(R.id.img_bg);
            holder.content= (TextView) view.findViewById(R.id.tv_content);
            holder.usename= (TextView) view.findViewById(R.id.tv_usename);
            holder.commentNub= (TextView) view.findViewById(R.id.tv_commentnub);
            holder.likeNub= (TextView) view.findViewById(R.id.tv_likenub);
            holder.img_like= (ImageView) view.findViewById(R.id.img_like);
            holder.ll_syanmicComment= (LinearLayout) view.findViewById(R.id.ll_syanmicComment);
            holder.ll_syanmicComment_more= (LinearLayout) view.findViewById(R.id.ll_syanmicComment_more);
            view.setTag(holder);
        }
        else
        {
            holder= (Holder) view.getTag();
        }
        holder.usename.setText(mylist.get(i).getCreatorName());
        holder.content.setText(mylist.get(i).getDescription());
        holder.likeNub.setText(mylist.get(i).getThumb()+"");
        holder.commentNub.setText(mylist.get(i).getReviewId()+"");
        String str_duration=jisuanDuration(mylist.get(i).getCreateTime());//计算时长
        holder.duration.setText(str_duration);
        //holder.img_bg.setTag(mylist.get(i));
        Picasso.with(mcontext)
                .load(mylist.get(i).getUserHeadPicture())
                .into( holder.img_head);


        holder.img_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(mcontext)
                .load(mylist.get(i).getPicture())
                .into(holder.img_bg);


        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!islike)
                {
                    setThumbData(index);
                    SendLikes();
                    view.setBackground(mcontext.getResources().getDrawable(R.mipmap.like1));
                    int i=mylist.get(index).getThumb();
                    mylist.get(index).setThumb(i+1);
                    islike=true;
                    notifyDataSetChanged();
                }
            }
        });

        holder.ll_syanmicComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext, DetailSynamicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Synamic",mylist.get(index));
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
            }
        });

        holder.ll_syanmicComment_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View altview = inflater.inflate(R.layout.dynamicpopwindow, null);
                TextView tv_synamic_detele= (TextView) altview.findViewById(R.id.tv_synamic_detele);
                TextView tv_synamic_share= (TextView) altview.findViewById(R.id.tv_synamic_share);
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setView(altview);
                final AlertDialog dialog = builder.create();//获取dialog
                dialog.show();//显示对话框
                tv_synamic_detele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mylist.remove(index);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                tv_synamic_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mcontext,"分享成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        holder.img_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private String jisuanDuration(Date createTime) {

        String str="";
        int longTime;
        long now_time=System.currentTimeMillis()/1000;
        long last_time=createTime.getTime()/1000;
        Log.d("zzz","now_time="+now_time);
        Log.d("zzz","last_time="+last_time);
        long duration=now_time-last_time;
        if(duration<3600)
        {
            longTime= (int) (duration/60);
            str=longTime+"分钟前";
        }
        else if(duration<84600&&duration>3600)
        {
            longTime= (int) (duration/3600);
            str=longTime+"小时前";
        }
        else if(duration>84600)
        {
            longTime= (int) (duration/84600);
            str=longTime+"天前";
        }
        return str;
    }



    /**
     * 设置点赞信息
     */
    private void setThumbData(int i) {
        thumb.setSynamicId(mylist.get(i).getRingId());
        thumb.setThumberId(user.getUserId()+"");
        thumb.setThumbHeadPicture(user.getUserHeadPicture());
        thumb.setThumbCount(mylist.get(i).getThumb()+1);
    }

    class Holder
    {
        private CircleImageView img_head;
        public ImageView img_bg,img_like;
        public TextView usename,duration,content,commentNub,likeNub;
        public LinearLayout ll_syanmicComment,ll_syanmicComment_more;
    }


    private void SendLikes()
    {
        String url="http://zqhstep.applinzi.com/synamic/user/thumb/thumb";
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("thumb", JSON.toJSONString(thumb))
                .add("accesstoken", sp.getString("accessToken","")).build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("zzz","网络请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                Log.d("zzz",str);
                ResultData data= ResultDataUtils.paraResult(str);
                if(!ResultDataUtils.isError(data)) {
                    Log.d("zzz", "成功点赞");
                }
                else {
                    Log.d("zzz", "失败点赞");
                }
            }
        });
    }

}
