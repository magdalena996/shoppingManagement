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

import java.util.List;

public class CreateListAdapter extends RecyclerView.Adapter<CreateListAdapter.ViewHolder>
{
    private List<Product> products;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public CreateListAdapter(Context context, List<Product> list)
    {
        products = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvPPrice;

        public ViewHolder(@NonNull final View itemView)
        {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPPrice = itemView.findViewById(R.id.tvPPrice);

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
    public CreateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateListAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.itemView.setTag(products.get(i));
        viewHolder.tvName.setText(products.get(i).getName());

        if (products.get(i).getPrice().equals("nie podano"))
        {
            viewHolder.tvPPrice.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.tvPPrice.setVisibility(View.VISIBLE);
            String sPrice = "Cena: " + products.get(i).getPrice() + " PLN";
            viewHolder.tvPPrice.setText(sPrice);
        }
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }
}
