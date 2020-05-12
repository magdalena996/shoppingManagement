package com.magdalena.shoppingmanagementnonvd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magdalena.shoppingmanagementnonvd.R;

public class AdjustStatsFrag extends Fragment
{
    View view;

    public AdjustStatsFrag()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_adjust_stat, container, false);
        return view;
    }

}
