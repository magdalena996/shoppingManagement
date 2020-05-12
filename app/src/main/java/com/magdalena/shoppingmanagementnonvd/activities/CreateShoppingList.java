package com.magdalena.shoppingmanagementnonvd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.adapters.CreateListAdapter;
import com.magdalena.shoppingmanagementnonvd.fragments.FragmentUtils;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ShoppingDBDAO;

import java.util.ArrayList;
import java.util.List;

public class CreateShoppingList extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CreateListAdapter.ItemClicked, Utils
{
    ActionBar actionBar;

    FragmentManager fragmentManager;
    Fragment createListFrag;

    Button btnCreateList;
    EditText etCount;
    Spinner spUnits;

    ShoppingDBDAO db;
    ProductsDAO dbP;

    private List<ShoppingActualList> list;
    private boolean ifClicked = false;
    private String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);

        db = new ShoppingDBDAO(this);
        dbP = new ProductsDAO(this);
        list = new ArrayList<>();

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.title_create_list);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setMapping();

        spUnits.setOnItemSelectedListener(this);
        etCount.setText("1");

        btnCreateList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ifClicked)
                {
                    db.open();
                    db.saveShoppingList(list);
                    db.loadActualListFromDb();
                    db.close();
                    Toast.makeText(getApplicationContext(), "Lista zakupów została pomyślnie utworzona", Toast.LENGTH_SHORT).show();
                    list.clear();
                    startActivity(new Intent(CreateShoppingList.this, ShoppingList.class));
                    CreateShoppingList.this.finish();
                } else
                {
                    Toast.makeText(getApplicationContext(), "Do listy nie został dodany żaden produkt, kliknij w produkt aby dodać go do listy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MainActivity.updateList(dbP);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        unit = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onItemClicked(int index)
    {
        ifClicked = true;
        int count;
        if (etCount.getText().toString().isEmpty())
        {
            etCount.setText("1");
            count = 1;
        } else
        {
            count = Integer.parseInt(etCount.getText().toString());
            if (count <= 0)
            {
                count = 1;
            }
        }

        Product product = MainActivity.fProducts.get(index);
        ShoppingActualList shoppingList = new ShoppingActualList(product.getId(), count, unit);

        list.add(shoppingList);
        MainActivity.fProducts.remove(index);

        FragmentUtils.refreshFrag(fragmentManager, createListFrag);
        Toast.makeText(getApplicationContext(), "Produkt " + product.getName() + " został pomyślnie dodany do listy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setMapping()
    {
        fragmentManager = getSupportFragmentManager();
        createListFrag = fragmentManager.findFragmentById(R.id.createListFrag);

        etCount = findViewById(R.id.etCount);
        btnCreateList = findViewById(R.id.btnCreateList);
        spUnits = findViewById(R.id.spUnits);
    }
}
