package example.com.step.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import example.com.step.R;
import example.com.step.bean.CommentEntity;
import example.com.step.bean.Synamic;
import example.com.step.util.AsyncImageLoader;
import example.com.step.widget.CircleImageView;

/**
 * Created by qinghua on 2016/12/3.
 */

public class CommentAdapter extends BaseAdapter {
    private  AsyncImageLoader imageLoader; //用来下载图片的类
    private ListView listview=null;
    private EditText meditText;
    private List<CommentEntity> mylist;
    private  Synamic msynamic;
    private LayoutInflater inflater;
    private Context mcontext;
    private int type=1;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    public CommentAdapter(Context context , List<CommentEntity> list, Synamic synamic, ListView mlistView,EditText editText)
    {
        listview=mlistView;
        meditText=editText;
        imageLoader=new AsyncImageLoader();
        mylist=list;
        msynamic=synamic;
        mcontext=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mylist.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return mylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p <= 0)
            return TYPE_1;
        else
            return TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold=null;
        ViewHoldItem viewHoldItem=null;
        int type = getItemViewType(i);
        switch(type)
        {
            case TYPE_1:
                if(view==null)
                {
                    viewHoldItem=new ViewHoldItem();
                    view=inflater.inflate(R.layout.detailsynamictypeone,null);
                    viewHoldItem.img_head= (ImageView) view.findViewById(R.id.img_head);
                    viewHoldItem.creatorName= (TextView) view.findViewById(R.id.tv_usename);
                    viewHoldItem.creator_content= (TextView) view.findViewById(R.id.tv_content);
                    viewHoldItem.img_like= (ImageView) view.findViewById(R.id.img_like);
                    viewHoldItem.img_bg= (ImageView) view.findViewById(R.id.img_bg);
                    viewHoldItem.ll_comment= (LinearLayout) view.findViewById(R.id.ll_comment);
                    viewHoldItem.ll_commnet_more= (LinearLayout) view.findViewById(R.id.ll_commnet_more);
                    viewHoldItem.tv_content= (TextView) view.findViewById(R.id.tv_content);
                    viewHoldItem.tv_commentnub= (TextView) view.findViewById(R.id.tv_commentnub);
                    viewHoldItem.tv_likenub= (TextView) view.findViewById(R.id.tv_likenub);
                    viewHoldItem.tv_duration= (TextView) view.findViewById(R.id.tv_duration);
                    view.setTag(viewHoldItem);
                }
                else
                {
                    viewHoldItem= (ViewHoldItem) view.getTag();
                }
                viewHoldItem.creatorName.setText(msynamic.getCreatorName());
                viewHoldItem.tv_content.setText(msynamic.getDescription());
                viewHoldItem.tv_commentnub.setText(msynamic.getReviewId()+"");
                viewHoldItem.tv_likenub.setText(msynamic.getThumb()+"");
                String str_duration=jisuanDuration(msynamic.getCreateTime());//计算时长
                viewHoldItem.tv_duration.setText(str_duration);
                viewHoldItem.img_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyDataSetChanged();
                    }
                });

                Picasso.with(mcontext)
                        .load(msynamic.getUserHeadPicture())
                        .into(viewHoldItem.img_head);

                viewHoldItem.img_bg.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(mcontext)
                        .load(msynamic.getPicture())
                        .into( viewHoldItem.img_bg);


                viewHoldItem.img_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewHoldItem.ll_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        meditText.requestFocus();
                        /**
                         * 显示输入法弹出
                         */
                        InputMethodManager m= (InputMethodManager) meditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        m.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });
                viewHoldItem.ll_commnet_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case TYPE_2:
                if(view==null)
                {
                    viewHold=new ViewHold();
                    view=inflater.inflate(R.layout.comment_content_item,null);
                    viewHold.commentHead= (CircleImageView) view.findViewById(R.id.commenter_head);
                    viewHold.comment_content= (TextView) view.findViewById(R.id.commenter_comtent);
                    viewHold.comment_name= (TextView) view.findViewById(R.id.commenter_name);
                    view.setTag(viewHold);
                }
                else
                {
                    viewHold= (ViewHold) view.getTag();
                }
                Picasso.with(mcontext)
                        .load(mylist.get(i-1).getObserverHeadPicture())
                        .into(  viewHold.commentHead);
                viewHold.comment_name.setText(mylist.get(i-1).getObserverName());
                viewHold.comment_content.setText(mylist.get(i-1).getComment_content());
                break;
        }
        return view;
    }

    class ViewHoldItem
    {
        CircleImageView creatorHead;
        TextView creatorName,creator_content,tv_content,tv_likenub,tv_commentnub,tv_duration;
        ImageView img_comment,img_like,img_bg,img_head;
        LinearLayout ll_comment,ll_commnet_more;
    }
    class ViewHold
    {
        CircleImageView commentHead;
        TextView comment_name,comment_content;
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
}
