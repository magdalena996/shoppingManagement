package com.magdalena.shoppingmanagementnonvd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magdalena.shoppingmanagementnonvd.R;

public class HomeFrag extends Fragment
{
    View view;

    public HomeFrag()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView tvHomeHead;
        tvHomeHead = view.findViewById(R.id.tvHomeHead);
        tvHomeHead.setVisibility(View.GONE);
    }
}
