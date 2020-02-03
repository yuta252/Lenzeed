package to.msn.wings.lenzeed.Activities.main.bottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import to.msn.wings.lenzeed.Activities.main.tab.Main2Fragment;
import to.msn.wings.lenzeed.Activities.main.tab.RecommendFragment;
import to.msn.wings.lenzeed.R;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        return view;
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter{

        // タブが増えたら更新する
        private static final int NUM_ITEMS = 2;

        public ViewPagerAdapter(FragmentManager fm){
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return RecommendFragment.newInstance();
                case 1:
                    return Main2Fragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position){

            switch (position){
                case 0:
                    return "Recommend";
                case 1:
                    return "Art";
                default:
                    return "";
            }
        }
    }

}
