package com.instanza.testmemo.Fragment.FragmentBase;

/**
 * Created by apple on 2017/10/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.instanza.testmemo.Activity.MainActivity;

public class FragmentHelper {

    private FragmentManager fragmentManager;
    public MainActivity context;
    private int containerId;

    public FragmentHelper(MainActivity fragmentActivity, int containerId) {
        this.context = fragmentActivity;
        this.containerId = containerId;
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
    }

    public FragmentHelper(MainActivity fragmentActivity, Fragment fragment, int containerId) {
        this.context = fragmentActivity;
        this.containerId = containerId;
        this.fragmentManager = fragment.getChildFragmentManager();
    }

    public void startFragment(Class<? extends Fragment> cls, Bundle args) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment startFragment = null;
        String tag = cls.getSimpleName();
        try {
            startFragment = (Fragment) fragmentManager
                    .findFragmentByTag(tag);
            if (startFragment == null) {
                startFragment = cls.newInstance();
            }

            startFragment.setArguments(args);
            if (lastFragment != null) {
                lastFragment.onPause();
                ft.hide(lastFragment);
//				ft.detach(lastFragment);
            }
            if (startFragment.isAdded()) {
                ft.show(startFragment);
                startFragment.onResume();
            } else {
//				ft.add(containerId, startFragment).addToBackStack(null);
                ft.add(containerId, startFragment, tag).addToBackStack(null);
            }

//			ft.replace(containerId, startFragment, tag).addToBackStack(null);
            ft.commit();
            lastFragment = startFragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {

    }

    /**
     * 上个fragment
     */
    private Fragment lastFragment;

    public Fragment getLastFragment() {
        return lastFragment;
    }
}
