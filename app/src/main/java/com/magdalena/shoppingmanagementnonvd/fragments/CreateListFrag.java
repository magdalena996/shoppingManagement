package com.magdalena.shoppingmanagementnonvd.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.activities.MainActivity;
import com.magdalena.shoppingmanagementnonvd.adapters.CreateListAdapter;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;

public class CreateListFrag extends Fragment implements AdapterView.OnItemSelectedListener
{
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    View view;
    Spinner spFilter;
    ProductsDAO db;

    public CreateListFrag()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_create_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        db = new ProductsDAO(this.getActivity());

        spFilter = view.findViewById(R.id.spFilter);
        spFilter.setOnItemSelectedListener(this);

        recyclerView = view.findViewById(R.id.rvProdToBuyList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new CreateListAdapter(this.getActivity(), MainActivity.fProducts);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            myAdapter = new CreateListAdapter(this.getActivity(), MainActivity.fProducts);
            recyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        MainActivity.pFilter = (String) parent.getItemAtPosition(position);

        db.open();
        MainActivity.updateList(db);
        db.close();

        myAdapter = new CreateListAdapter(this.getActivity(), MainActivity.fProducts);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

}
