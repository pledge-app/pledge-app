package com.pledgeapp.pledge.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.astuetz.PagerSlidingTabStrip;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.fragments.nonprofitlists.CategoriesSelectionListFragment;
import com.pledgeapp.pledge.fragments.nonprofitlists.FeaturedNonProfitsFragment;
import com.pledgeapp.pledge.fragments.nonprofitlists.LocalNonProfitsFragment;

/**
 * Created by nikhil on 10/14/15.
 */
public class BrowsePagerAdapter extends SmartFragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private static final String[] TAB_TITLES = {"Featured", "Categories", "Local"};
    private static final int[] TAB_ICONS = {R.drawable.ic_featured, R.drawable.ic_categories, R.drawable.ic_nearby};

    public BrowsePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // TODO(nikhilb) actually load correct fragments
        if (position == 0) {
            return FeaturedNonProfitsFragment.newInstance();
        } else if (position == 1) {
            return CategoriesSelectionListFragment.newInstance();
        } else {
            return LocalNonProfitsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getPageIconResId(int i) {
        return TAB_ICONS[i];
    }
}
