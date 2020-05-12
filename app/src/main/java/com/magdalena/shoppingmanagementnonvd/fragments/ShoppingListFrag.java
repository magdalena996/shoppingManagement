package com.magdalena.shoppingmanagementnonvd.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;
import com.magdalena.shoppingmanagementnonvd.adapters.BuyListAdapter;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ShoppingDBDAO;
import com.magdalena.shoppingmanagementnonvd.swipetodelete.SwipeToDelete;

public class ShoppingListFrag extends Fragment
{
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    BuyListAdapter myAdapter;

    View view;
    ProductsDAO db;
    ShoppingDBDAO dbDAO;

    public ShoppingListFrag()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        db = new ProductsDAO(this.getActivity());
        dbDAO = new ShoppingDBDAO(this.getActivity());

        recyclerView = view.findViewById(R.id.rvShoppingList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new BuyListAdapter(this.getActivity(), ShoppingDBDAO.actualShoppingList);
        recyclerView.setAdapter(myAdapter);

        enableSwipeToDelete();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        myAdapter = new BuyListAdapter(this.getActivity(), ShoppingDBDAO.actualShoppingList);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);

        if (!hidden)
        {
            myAdapter = new BuyListAdapter(this.getActivity(), ShoppingDBDAO.actualShoppingList);
            recyclerView.setAdapter(myAdapter);
        }
    }

    private void enableSwipeToDelete()
    {
        SwipeToDelete swipeToDelete = new SwipeToDelete(this.getActivity())
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                final int position = viewHolder.getAdapterPosition();
                final ShoppingActualList item = myAdapter.getData().get(position);

                myAdapter.removeItem(position);

                dbDAO.open();
                dbDAO.deleteProductFromList(item);
                dbDAO.loadActualListFromDb();
                dbDAO.close();

                onResume();
                Toast.makeText(getContext(), "Usunięto produkt z listy",
                        Toast.LENGTH_SHORT).show();

                if (ShoppingDBDAO.actualShoppingList.isEmpty())
                {
                    ShoppingListFrag.this.getActivity().finish();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDelete);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
