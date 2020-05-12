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
import android.widget.TextView;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.adapters.ProductAdapter;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;

public class ProductsList extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ProductAdapter.ItemClicked, Utils
{
    ActionBar actionBar;

    FragmentManager fragmentManager;
    Fragment pListFrag, pAddFrag;

    Button btnAddP, btnClose, btnAdd;
    EditText etName;
    TextView tvInfo;
    Spinner spinnerCategories;

    private String productName, productCategory;
    private ProductsDAO db;

    public static int pIndex;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plist);

        db = new ProductsDAO(this);

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.title_products);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setMapping();
        setVisibility();

        tvInfo.setText("Brak zapisanych produktów");

        //Show add product fragment
        btnAddP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager.beginTransaction()
                        .hide(pListFrag)
                        .show(pAddFrag)
                        .commit();
                btnAddP.setVisibility(View.GONE);
            }
        });

        //Close add product fragment
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager.beginTransaction()
                        .show(pListFrag)
                        .hide(pAddFrag)
                        .commit();
                btnAddP.setVisibility(View.VISIBLE);
            }
        });

        //Adding product to list and hide add product fragment
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etName.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "NAZWA PRODUKTU NIE ZOSTAŁA WPROWADZONA",
                            Toast.LENGTH_LONG).show();
                } else
                {
                    productName = etName.getText().toString().trim();

                    Product product = new Product(productName, productCategory);

                    //add product to database and update product list in app
                    db.open();
                    db.saveProduct(product);
                    MainActivity.updateList(db);
                    db.close();

                    Toast.makeText(getApplicationContext(), "Produkt został pomyślnie dodany",
                            Toast.LENGTH_LONG).show();
                    etName.setText("");

                    setVisibility();
                    btnAddP.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MainActivity.updateList(db);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        //assign item selected value in spinnerCategories to productCategory variable
        productCategory = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    //start EditProduct activity after clicked on a list item
    @Override
    public void onItemClicked(int index)
    {
        startActivity(new Intent(ProductsList.this, EditProduct.class));
        //assign the index value of the clicked item
        pIndex = index;
    }

    @Override
    public void setMapping()
    {
        fragmentManager = getSupportFragmentManager();

        pListFrag = fragmentManager.findFragmentById(R.id.pListFrag);
        pAddFrag = fragmentManager.findFragmentById(R.id.pAddFrag);

        btnAddP = findViewById(R.id.btnAddP);
        btnClose = findViewById(R.id.btnClose);
        btnAdd = findViewById(R.id.btnAdd);

        tvInfo = findViewById(R.id.tvInfo);
        etName = findViewById(R.id.etName);

        spinnerCategories = findViewById(R.id.spinner_categories);
        spinnerCategories.setOnItemSelectedListener(this);
    }

    private void setVisibility()
    {
        tvInfo.setVisibility(View.GONE);

        if (MainActivity.fProducts.isEmpty())
        {
            tvInfo.setVisibility(View.VISIBLE);
        }

        fragmentManager.beginTransaction()
                .show(pListFrag)
                .hide(pAddFrag)
                .commit();
    }
}
