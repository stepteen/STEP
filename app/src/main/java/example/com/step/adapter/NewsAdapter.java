package example.com.step.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.step.R;
import example.com.step.bean.NewsEntity;
import example.com.step.util.AsyncImageLoader;

/**
 * Created by qinghua on 2016/12/10.
 */

public class NewsAdapter  extends BaseAdapter{
    private Context mContext;
    private List<NewsEntity> mylist;
    private LayoutInflater inflater;
    private ListView listview=null;
    private  AsyncImageLoader imageLoader; //用来下载图片的类
    public NewsAdapter(Context context, List<NewsEntity> list, ListView mlistView)
    {
        mylist=list;
        inflater=LayoutInflater.from(context);
        listview=mlistView;
        imageLoader=new AsyncImageLoader();
        mContext=context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHold viewHold=null;
        if(view==null)
        {
            viewHold=new ViewHold();
            view=inflater.inflate(R.layout.newsitem,null);
            viewHold.imageView= (ImageView) view.findViewById(R.id.img_cic_head);
            viewHold.tv_news_title= (TextView) view.findViewById(R.id.tv_news_title);
            viewHold.tv_msg_time= (TextView) view.findViewById(R.id.msg_date);
            view.setTag(viewHold);
        }
        else
        {
            viewHold= (ViewHold) view.getTag();

        }

        if(mylist.get(i).getLinkPic()!="") {
            viewHold.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(mContext)
                    .load(mylist.get(i).getLinkPic())
                    .into( viewHold.imageView);
        }
        viewHold.tv_news_title.setText(mylist.get(i).getLinkText());
        viewHold.tv_msg_time.setText(mylist.get(i).getTime());
        return view;
    }

    class ViewHold
    {
        public ImageView imageView;
        public TextView tv_news_title,tv_msg_time;
    }
}
