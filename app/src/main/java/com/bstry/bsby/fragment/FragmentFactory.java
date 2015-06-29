package com.bstry.bsby.fragment;

import android.app.Fragment;

import com.bstry.bsby.R;

/**
 * Created by zhangls on 2/7/15.
 */
public class FragmentFactory {
    public static Fragment getInstanceByIndex(int checkedId) {
        Fragment fragment = null;
        switch (checkedId) {
            case R.id.rb_sb:
                fragment = new SbFragment();
                break;
            case R.id.rb_search:
                fragment = new SearchFragment();
                break;
            case R.id.rb_me:
                fragment = new MeFragment();
                break;

        }
        return fragment;
    }
}
