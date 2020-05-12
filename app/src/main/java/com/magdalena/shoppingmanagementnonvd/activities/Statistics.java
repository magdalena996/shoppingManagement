package com.magdalena.shoppingmanagementnonvd.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.Tools;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ShoppingDBDAO;

import java.util.Calendar;

import static com.magdalena.shoppingmanagementnonvd.Tools.endDate;
import static com.magdalena.shoppingmanagementnonvd.Tools.getFormattedPrice;
import static com.magdalena.shoppingmanagementnonvd.Tools.setDefaultDates;
import static com.magdalena.shoppingmanagementnonvd.Tools.startDate;

public class Statistics extends AppCompatActivity implements Utils
{
    ActionBar actionBar;

    FragmentManager fragmentManager;
    Fragment showStatsFrag, showGeneralStatsFrag;

    Button btnShowGeneral, btnCloseStats, btnStartDate, btnEndDate, btnGenStats;
    TextView tvTimePer, tvCategory, tvAll, tvInCategory, tvStartDate, tvEndDate;
    Spinner spTimePer, spCategoryStats;
    TableRow trDatePer, trTimePer;
    RadioGroup radioGroup;
    LinearLayout linLayExpenses, linLayDatePickers;

    PurchasedProductDAO ppDb;
    ShoppingDBDAO dbDAO;

    public int rbID = 3;
    public String category, timePer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ppDb = new PurchasedProductDAO(this);
        dbDAO = new ShoppingDBDAO(this);

        ppDb.open();
        ppDb.loadPurchasedProductsFromDb();
        ppDb.close();

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.title_statistics);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setDefaultDates();
        setMapping();
        setViewsVisibilityOnCreate();
        setTvTimePer();

        //set radio button id and visibility
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                setDefaultDates();
                spCategoryStats.setSelection(0);

                if (checkedId == R.id.rbTimePer)
                {
                    rbID = 1;

                } else if (checkedId == R.id.rbDatePer)
                {
                    rbID = 2;

                } else if (checkedId == R.id.rbAll)
                {
                    rbID = 3;
                    setTextByCategory();
                    tvTimePer.setText("Wszystkie wydatki od początku");
                }
                setViewsVisibilityByChoice();
            }
        });

        // if rb == 2
        btnStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker(tvStartDate);
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker(tvEndDate);
            }
        });

        // Show General Statistics
        btnShowGeneral.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager.beginTransaction()
                        .hide(showStatsFrag)
                        .show(showGeneralStatsFrag)
                        .commit();

                btnShowGeneral.setVisibility(View.GONE);
                if (actionBar != null)
                {
                    actionBar.hide();
                }
            }
        });

        //Close General Statistics
        btnCloseStats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager.beginTransaction()
                        .show(showStatsFrag)
                        .hide(showGeneralStatsFrag)
                        .commit();

                btnShowGeneral.setVisibility(View.VISIBLE);
                if (actionBar != null)
                {
                    actionBar.show();
                }
            }
        });

        //if rb == 1
        spTimePer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                timePer = (String) parent.getItemAtPosition(position);
                btnGenStats.setVisibility(View.VISIBLE);
                linLayExpenses.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spCategoryStats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                category = (String) parent.getItemAtPosition(position);

                if (rbID == 3)
                {
                    setTextByCategory();
                } else
                {
                    btnGenStats.setVisibility(View.VISIBLE);
                    linLayExpenses.setVisibility(View.GONE);

                    if (rbID == 2)
                    {
                        trDatePer.setVisibility(View.VISIBLE);
                        linLayDatePickers.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        btnGenStats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                linLayExpenses.setVisibility(View.GONE);

                setTvTimePer();
                setTextByCategory();

                if (rbID == 1)
                {
                    btnGenStats.setVisibility(View.GONE);

                } else if (rbID == 2)
                {
                    if (!tvStartDate.getText().toString().isEmpty() && !tvEndDate.getText().toString().isEmpty())
                    {
                        trDatePer.setVisibility(View.GONE);
                        linLayDatePickers.setVisibility(View.GONE);
                        btnGenStats.setVisibility(View.GONE);
                    } else
                    {
                        linLayExpenses.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Wybiesz datę poczatkową i końcową, aby wyświetlić statystyki",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        category = "Wszystkie";

        ppDb.open();
        ppDb.loadPurchasedProductsFromDb();
        ppDb.close();

        setViewsVisibilityByChoice();
        setTextByCategory();

        spCategoryStats.setSelection(0);

        if (rbID == 1 || rbID == 2)
        {
            linLayExpenses.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMapping()
    {
        fragmentManager = getSupportFragmentManager();
        showStatsFrag = fragmentManager.findFragmentById(R.id.showStatFrag);
        showGeneralStatsFrag = fragmentManager.findFragmentById(R.id.showGeneralStatFrag);

        tvTimePer = findViewById(R.id.tvTimePer);
        tvCategory = findViewById(R.id.tvCategory);
        tvAll = findViewById(R.id.tvAll);
        tvInCategory = findViewById(R.id.tvInCategory);

        tvStartDate = findViewById(R.id.etStartDate);
        tvEndDate = findViewById(R.id.etEndDate);

        trTimePer = findViewById(R.id.trTimePer);
        trDatePer = findViewById(R.id.trDatePer);

        spTimePer = findViewById(R.id.spTimePer);
        spCategoryStats = findViewById(R.id.spCategoryStats);

        radioGroup = findViewById(R.id.radioGroup);

        btnGenStats = findViewById(R.id.btnGenStats);
        btnCloseStats = findViewById(R.id.btnCloseStat);
        btnShowGeneral = findViewById(R.id.btnShowGeneral);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        linLayExpenses = findViewById(R.id.linLayExpenses);
        linLayDatePickers = findViewById(R.id.linLayDatePickers);
    }


    public void hideUnused()
    {

        trTimePer.setVisibility(View.GONE);
        trDatePer.setVisibility(View.GONE);
        linLayDatePickers.setVisibility(View.GONE);
        btnGenStats.setVisibility(View.GONE);
    }

    public void setViewsVisibilityOnCreate()
    {
        fragmentManager.beginTransaction()
                .show(showStatsFrag)
                .hide(showGeneralStatsFrag)
                .commit();
        hideUnused();
    }

    public void setViewsVisibilityByChoice()
    {
        if (rbID == 1 || rbID == 2)
        {
            btnGenStats.setVisibility(View.VISIBLE);
            linLayExpenses.setVisibility(View.GONE);
        }
        if (rbID == 1)
        {
            trTimePer.setVisibility(View.VISIBLE);
            trDatePer.setVisibility(View.GONE);
            linLayDatePickers.setVisibility(View.GONE);
        } else if (rbID == 2)
        {
            trDatePer.setVisibility(View.VISIBLE);
            linLayDatePickers.setVisibility(View.VISIBLE);
            trTimePer.setVisibility(View.GONE);
        } else if (rbID == 3)
        {
            hideUnused();
            linLayExpenses.setVisibility(View.VISIBLE);
        }
    }

    public void setTvTimePer()
    {

        if (rbID == 1)
        {
            tvTimePer.setText(R.string.timePer);
            if (timePer.equals("tydzień"))
            {
                tvTimePer.append(" tygodnia");
            } else if (timePer.equals("miesiąc"))
            {
                tvTimePer.append(" miesiąca");
            } else if (timePer.equals("kwartał"))
            {
                tvTimePer.append(" kwartału");
            } else
            {
                tvTimePer.append(" roku");
            }
        } else if (rbID == 2)
        {
            startDate = tvStartDate.getText().toString();
            endDate = tvEndDate.getText().toString();
            String dates = "Wydatki w przedziale\nod " + startDate + " do " + endDate;
            tvTimePer.setText(dates);

        } else if (rbID == 3)
        {
            tvTimePer.setText("Wszystkie wydatki od początku");
        }
    }


    public void setTextByCategory()
    {
        if (category.equals("Wszystkie"))
        {
            tvCategory.setText("We wszystkich kategoriach");
            tvCategory.setGravity(Gravity.LEFT);
            tvInCategory.setVisibility(View.GONE);
        } else
        {
            tvInCategory.setVisibility(View.VISIBLE);
            tvCategory.setGravity(Gravity.RIGHT);
            tvCategory.setText(category.toLowerCase());
        }
        String sPrice = getExpensesWithChangedPriceFormat() + " PLN";

        tvAll.setText(sPrice);
        linLayExpenses.setVisibility(View.VISIBLE);
    }

    public void showDatePicker(final TextView textView)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Statistics.this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        String date;
                        String sMonth = Integer.toString(month + 1), sDay = Integer.toString(dayOfMonth);
                        if (month + 1 < 10)
                        {
                            sMonth = "0" + sMonth;
                        }
                        if (dayOfMonth < 10)
                        {
                            sDay = "0" + sDay;
                        }
                        date = year + "-" + sMonth + "-" + sDay;

                        textView.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public String getExpensesWithChangedPriceFormat()
    {
        float fPrice;

        if (rbID == 1)
        {
            String currentDate = Tools.getCurrentDate();
            fPrice = Tools.getExpensesByDatesAndCategory(category,
                    Tools.subtractDate(currentDate, timePer),
                    currentDate);

        } else if (rbID == 2)
        {
            fPrice = Tools.getExpensesByDatesAndCategory(category, startDate, endDate);
        } else
        {
            ppDb.open();
            fPrice = ppDb.getExpensesByCategory(category);
            ppDb.close();
        }

        String price = Float.toString(fPrice);

        return getFormattedPrice(price);
    }

}
