package example.com.step.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import example.com.step.R;
import example.com.step.activity.SendDynamicActivity;
import example.com.step.adapter.LoadListViewAdapter;
import example.com.step.adapter.MyViewPagerAdapter;
import example.com.step.bean.SynamicItemBean;


/**
 * Created by qinghua on 2016/9/4.
 */
public class PlaygroundFragment extends Fragment implements View.OnClickListener{



    private ViewPager playViewPager;
    private List<Fragment> fra_list;
    private MyViewPagerAdapter V_adapter;
    private PagerAdapter mAdapter;




    private ImageView iv_senddynamic;//发布动态的按钮
    private TextView tv_dynamic,tv_mynamic,tv_dynamic_line,tv_mydynamic_line;
    private View playview,listview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        playview=inflater.inflate(R.layout.playgroundfragment,container,false);
        listview=LayoutInflater.from(getContext()).inflate(R.layout.playgrounddynamic,null);
        initView();

        return playview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void initView() {


        playViewPager= (ViewPager) playview.findViewById(R.id.play_ViewPager);
        iv_senddynamic= (ImageView) playview.findViewById(R.id.iv_senddynamic);
        tv_dynamic= (TextView) playview.findViewById(R.id.tv_dynamyc);
        iv_senddynamic.setOnClickListener(this);//设置监听
        tv_dynamic_line= (TextView) playview.findViewById(R.id.tv_dynamyc_line);
        tv_mynamic= (TextView) playview.findViewById(R.id.tv_mydynamyc);
        tv_mydynamic_line= (TextView) playview.findViewById(R.id.tv_mydynamyc_line);
        fra_list=new ArrayList<Fragment>();

       // View myview=LayoutInflater.from(getContext()).inflate(R.layout.playgrounddynamic,null);

        dynamicFragment fragment1=new dynamicFragment();
        fra_list.add(fragment1);
        MydynamicFragment fragment2=new MydynamicFragment();
        fra_list.add(fragment2);
        V_adapter=new MyViewPagerAdapter(getChildFragmentManager(),fra_list);
        playViewPager.setAdapter(V_adapter);


       playViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
                switch (position)
                {
                    case  0:
                        tv_dynamic.setTextColor(getResources().getColor(R.color.white));
                        tv_dynamic_line.setBackgroundColor(getResources().getColor(R.color.white));
                        tv_mynamic.setTextColor(getResources().getColor(R.color.gray));
                        tv_mydynamic_line.setBackgroundColor(getResources().getColor(R.color.blue));
                        break;
                    case  1:
                        tv_dynamic.setTextColor(getResources().getColor(R.color.gray));
                        tv_dynamic_line.setBackgroundColor(getResources().getColor(R.color.blue));
                        tv_mynamic.setTextColor(getResources().getColor(R.color.white));
                        tv_mydynamic_line.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });


    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.iv_senddynamic:
                Intent intent=new Intent(getContext(), SendDynamicActivity.class);
                startActivity(intent);
                break;
        }
    }
}
