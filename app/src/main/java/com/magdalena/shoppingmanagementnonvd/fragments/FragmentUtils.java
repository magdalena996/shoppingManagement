package com.magdalena.shoppingmanagementnonvd.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public abstract class FragmentUtils
{
    public static void refreshFrag(FragmentManager fragmentManager, Fragment fragment)
    {
        fragmentManager.beginTransaction()
                .hide(fragment)
                .commit();
        fragmentManager.beginTransaction()
                .show(fragment)
                .commit();
    }
}
