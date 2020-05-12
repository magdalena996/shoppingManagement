package com.magdalena.shoppingmanagementnonvd.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;

import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder>
{
    private List<ShoppingActualList> products;

    BuyListAdapter.ItemClicked activity;

    ShoppingActualList shoppingActualList;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public BuyListAdapter(Context context, List<ShoppingActualList> list)
    {
        products = list;
        activity = (BuyListAdapter.ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvCount;

        public ViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    activity.onItemClicked(products.indexOf(v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public BuyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_buylist_layout, viewGroup, false);
        return new BuyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyListAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.itemView.setTag(products.get(i));

        Product product = Product.getActiveProductById(products.get(i).getId_product());

        viewHolder.tvName.setText(product.getName());
        viewHolder.tvCount.setVisibility(View.VISIBLE);

        String sCountToBuy = "Ilość: " + products.get(i).getCountToBuy() + " " + products.get(i).getUnit();
        viewHolder.tvCount.setText(sCountToBuy);
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }

    public void removeItem(int position)
    {
        products.remove(position);
        notifyItemRemoved(position);
        shoppingActualList = new ShoppingActualList();
    }

    public List<ShoppingActualList> getData()
    {
        return products;
    }
}
