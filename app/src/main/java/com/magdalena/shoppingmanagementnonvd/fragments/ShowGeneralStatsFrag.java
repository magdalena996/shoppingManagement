package com.magdalena.shoppingmanagementnonvd.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.Tools;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;

public class ShowGeneralStatsFrag extends Fragment
{
    View view;
    RadioGroup rgTimePeriod;
    TextView tvGExpense, tvCExpense, tvHChExpense, tvKExpense, tvPExpense, tvOExpense, tvAllExpense,
            tvTimePeriod;

    PurchasedProductDAO ppDb;

    public ShowGeneralStatsFrag()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_show_general_stat, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        rgTimePeriod = view.findViewById(R.id.rgTimePeriod);
        tvGExpense = view.findViewById(R.id.tvGExpense);
        tvCExpense = view.findViewById(R.id.tvCExpense);
        tvHChExpense = view.findViewById(R.id.tvHChExpense);
        tvKExpense = view.findViewById(R.id.tvKExpense);
        tvPExpense = view.findViewById(R.id.tvPExpense);
        tvOExpense = view.findViewById(R.id.tvOExpense);
        tvAllExpense = view.findViewById(R.id.tvAllExpense);
        tvTimePeriod = view.findViewById(R.id.tvTimePeriod);

        ppDb = new PurchasedProductDAO(this.getContext());

        setTVForAll();

        rgTimePeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                ppDb.open();
                switch (checkedId)
                {
                    case R.id.rbAllData:
                        setTVForAll();
                        break;
                    case R.id.rbWeek:
                        setTVByTimePer("tydzień");
                        break;
                    case R.id.rbMonth:
                        setTVByTimePer("miesiąc");
                        break;
                    case R.id.rbQuarter:
                        setTVByTimePer("kwartał");
                        break;
                    case R.id.rbYear:
                        setTVByTimePer("rok");
                        break;
                }
                ppDb.close();
            }
        });

    }

    private void setTVByCategory(String category, TextView tv)
    {
        float price;
        String sPrice;
        price = ppDb.getExpensesByCategory(category);
        sPrice = Tools.getFormattedPrice(Float.toString(price)) + " PLN";
        tv.setText(sPrice);
    }

    private void setTVByCategoryAndTimePer(String category, TextView tv, String timePer)
    {
        float price = Tools.getExpensesByDatesAndCategory(
                category,
                Tools.subtractDate(Tools.getCurrentDate(), timePer),
                Tools.getCurrentDate()
        );

        String sPrice = Tools.getFormattedPrice(Float.toString(price));
        tv.setText(sPrice);
    }

    private void setTVByTimePer(String timePer)
    {
        setTVByCategoryAndTimePer("Wszystkie", tvAllExpense, timePer);
        setTVByCategoryAndTimePer("Artykuły spożywcze", tvGExpense, timePer);
        setTVByCategoryAndTimePer("Kosmetyki", tvCExpense, timePer);
        setTVByCategoryAndTimePer("Chemia gospodarcza", tvHChExpense, timePer);
        setTVByCategoryAndTimePer("Dla dzieci", tvKExpense, timePer);
        setTVByCategoryAndTimePer("Dla zwierząt", tvPExpense, timePer);
        setTVByCategoryAndTimePer("Inne", tvOExpense, timePer);

        tvTimePeriod.setText(R.string.timePer);
        if (timePer.equals("tydzień"))
        {
            tvTimePeriod.append(" tygodnia");
        } else if (timePer.equals("miesiąc"))
        {
            tvTimePeriod.append(" miesiąca");
        } else if (timePer.equals("kwartał"))
        {
            tvTimePeriod.append(" kwartału");
        } else
        {
            tvTimePeriod.append(" roku");
        }
    }

    private void setTVForAll()
    {
        setTVByCategory("Wszystkie", tvAllExpense);
        setTVByCategory("Artykuły spożywcze", tvGExpense);
        setTVByCategory("Kosmetyki", tvCExpense);
        setTVByCategory("Chemia gospodarcza", tvHChExpense);
        setTVByCategory("Dla dzieci", tvKExpense);
        setTVByCategory("Dla zwierząt", tvPExpense);
        setTVByCategory("Inne", tvOExpense);
        tvTimePeriod.setText(R.string.all_expenses);
    }
}
