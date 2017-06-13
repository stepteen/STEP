package example.com.step.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.step.R;
import example.com.step.bean.MessageBean;
import example.com.step.bean.RunScoresBeans;
import example.com.step.widget.CircleImageView;



public class HistoryAdapter extends BaseAdapter {

    private List<RunScoresBeans> mylist;
    private LayoutInflater inflater;
    public HistoryAdapter(Context context, List<RunScoresBeans> list)
    {
        mylist=list;
        inflater=LayoutInflater.from(context);
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
            viewHold= new ViewHold();
            view=inflater.inflate(R.layout.historscoresitem,null);
            viewHold.tv_date= (TextView) view.findViewById(R.id.tv_history_date);
            viewHold.tv_luceng= (TextView) view.findViewById(R.id.tv_history_luceng);
            viewHold.tv_time= (TextView) view.findViewById(R.id.tv_history_time);
            viewHold.tv_sudu= (TextView) view.findViewById(R.id.tv_history_sudu);
            view.setTag(viewHold);
        }
        else
        {
            viewHold= (ViewHold) view.getTag();

        }
        viewHold.tv_date.setText(mylist.get(i).getRun_startTime());
        viewHold.tv_luceng.setText(mylist.get(i).getRun_Distance()+"");
        viewHold.tv_time.setText(mylist.get(i).getTimeLong());
        viewHold.tv_sudu.setText(mylist.get(i).getAverage_speed().toString());
        return view;
    }
    class ViewHold
    {
        public TextView tv_date,tv_luceng,tv_time,tv_sudu;
    }
}
