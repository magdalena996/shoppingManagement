package com.magdalena.shoppingmanagementnonvd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magdalena.shoppingmanagementnonvd.R;

public class AddProductFrag extends Fragment
{
    public AddProductFrag()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_add_prod, container, false);
    }
}
