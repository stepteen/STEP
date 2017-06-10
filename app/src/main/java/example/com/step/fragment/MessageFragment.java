package example.com.step.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.step.MyApplication;
import example.com.step.R;
import example.com.step.activity.DetailMessageActivity;
import example.com.step.activity.DetailNewsActivity;
import example.com.step.adapter.MyMessageAdapter;
import example.com.step.bean.MessageBean;
import example.com.step.util.MessageServiceUtil;
import example.com.step.widget.SlideListview;


/**
 * Created by qinghua on 2016/9/4.
 */
public class MessageFragment extends Fragment {

    private MessageServiceUtil msgUtil=new MessageServiceUtil(MyApplication.getApp_Context());
    //private ListView m_msg_listview;
    private SlideListview m_msg_listview;
    private List<MessageBean> msg_listdate;
    private MyMessageAdapter myMessageAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View m_view=inflater.inflate(R.layout.messagefragment,container,false);
        m_msg_listview= (SlideListview) m_view.findViewById(R.id.message_list);
        return m_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setData();
        initView();
        initItemClick();
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        myMessageAdapter=new MyMessageAdapter(getContext(),msg_listdate,m_msg_listview);
        m_msg_listview.setAdapter(myMessageAdapter);
    }

    private void setData() {
        msg_listdate= new ArrayList<MessageBean>();
        msg_listdate.addAll(msgUtil.find());

    }

    private void initItemClick()
    {

        m_msg_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
                msg_listdate.get(i).setmIndex(1);
                msgUtil.update(msg_listdate.get(i));
                final int index=i;
                Intent intent=new Intent();
                intent.setClass(getContext(), DetailMessageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("messageBean",msg_listdate.get(index));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
    }

//
//    private void showPopupWindow(View view,int i) {
//
//        final  int index=i;
//        // 一个自定义的布局，作为显示的内容
//        View contentView = LayoutInflater.from(getContext()).inflate(
//                R.layout.pop_window, null);
//        // 设置按钮的点击事件
//        ImageView tv_delete = (ImageView) contentView.findViewById(R.id.pop_delete);
//
//
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//        popupWindow.setTouchable(true);
//
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.i("mengdd", "onTouch : ");
//
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//        tv_delete.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                msgUtil.delete(msg_listdate.get(index));
//                msg_listdate.remove(index);
//                myMessageAdapter.notifyDataSetChanged();
//                popupWindow.dismiss();
//            }
//        });
//        //popupWindow.showAsDropDown(view);
//        popupWindow.showAsDropDown(view,view.getWidth()/2,(-view.getHeight())*3/2);
//    }
}
