package to.msn.wings.lenzeed.Data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import to.msn.wings.lenzeed.Activities.main.tab.RecommendFragment;
import to.msn.wings.lenzeed.Activities.main.tab.Main2Fragment;

public class OriginalFragmentPagerAdapter extends FragmentPagerAdapter {

    private CharSequence[] tabTitles = {"たぶ1", "たぶ2"};

    public OriginalFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new RecommendFragment();
            case 1:
                return new Main2Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return tabTitles.length;
    }
}
