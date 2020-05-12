package com.magdalena.shoppingmanagementnonvd.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
{
    private List<Product> products;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public ProductAdapter(Context context, List<Product> list)
    {
        products = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvPrice, tvDays;

        public ViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDays = itemView.findViewById(R.id.tvDays);

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
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.itemView.setTag(products.get(i));

        viewHolder.tvName.setText(products.get(i).getName());

        if (products.get(i).getPrice().equals("nie podano"))
        {
            viewHolder.tvPrice.setVisibility(View.VISIBLE);
            viewHolder.tvPrice.setText("Brak ceny");
        } else
        {
            viewHolder.tvPrice.setVisibility(View.VISIBLE);
            viewHolder.tvPrice.setText("Cena: " + products.get(i).getPrice() + " PLN");
        }

        if(products.get(i).getDays() == 1)
        {
            viewHolder.tvDays.setVisibility(View.VISIBLE);
            viewHolder.tvDays.setText("Wystarcza na: " + products.get(i).getDays() + " dzień");
        }
        else if (products.get(i).getDays() > 1)
        {
            viewHolder.tvDays.setVisibility(View.VISIBLE);
            viewHolder.tvDays.setText("Wystarcza na: " + products.get(i).getDays() + " dni");
        } else if(products.get(i).getDays() == -1)
        {
            viewHolder.tvDays.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.tvDays.setVisibility(View.VISIBLE);
            viewHolder.tvDays.setText("Produkt niezużywalny");
        }
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }
}

