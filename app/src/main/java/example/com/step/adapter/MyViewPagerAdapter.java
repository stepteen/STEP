package example.com.step.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinghua on 2016/10/7.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list = new ArrayList<Fragment>();

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

}
