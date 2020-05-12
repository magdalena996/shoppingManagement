package com.magdalena.shoppingmanagementnonvd.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.Tools;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Utils
{
    ActionBar actionBar;

    EditText etPName, etPDays, etPPrice;
    TextInputLayout layout;
    Button btnSave, btnDelete;
    Spinner spCategories;

    private Product product;
    private ProductsDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_p);

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.title_products);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = new ProductsDAO(EditProduct.this);

        setMapping();
        setSpinnerAdapter();
        setFields();

        if (product.getDays() == -1)
        {
            etPDays.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etPName.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Wprowadź nazwę produktu",
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    product.setName(etPName.getText().toString().trim());

                    if (!etPDays.getText().toString().trim().isEmpty())
                    {
                        product.setDays(Integer.parseInt(etPDays.getText().toString().trim()));
                    }

                    if (!etPPrice.getText().toString().trim().isEmpty())
                    {
                        String price = etPPrice.getText().toString().trim();
                        product.setPrice(Tools.getFormattedPrice(price));
                    }

                    db.open();
                    db.updateProduct(product);
                    MainActivity.updateList(db);
                    db.close();

                    Toast.makeText(getApplicationContext(), "Pomyślnie zaktualizowano informacje",
                            Toast.LENGTH_SHORT).show();

                    EditProduct.this.finish();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db.open();
                db.deleteProduct(product);
                MainActivity.updateList(db);
                db.close();

                Toast.makeText(getApplicationContext(), "Produkt został pomyślnie usunięty",
                        Toast.LENGTH_SHORT).show();

                EditProduct.this.finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        product.setCategory((String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        Toast.makeText(getApplicationContext(), "NIE WYBRANO KATEGORII", Toast.LENGTH_LONG).show();
    }

    private void setSpinnerAdapter()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(adapter);
        spCategories.setOnItemSelectedListener(this);
    }

    private void setFields()
    {
        product = MainActivity.fProducts.get(ProductsList.pIndex);

        String days = Integer.toString(product.getDays());
        etPName.setText(product.getName());

        if (!(product.getDays() == -1))
        {
            etPDays.setText(days);
        }

        if (!(product.getPrice().equals("nie podano")))
        {
            etPPrice.setText(product.getPrice());
        }

        String[] categories = getResources().getStringArray(R.array.categories);
        String pCategory = product.getCategory();
        for (int i = 0; i < categories.length; i++)
        {
            if (pCategory.equals(categories[i]))
            {
                spCategories.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void setMapping()
    {
        etPName = findViewById(R.id.etPName);
        etPDays = findViewById(R.id.etPDays);
        etPPrice = findViewById(R.id.etPPrice);

        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        spCategories = findViewById(R.id.spCateg);

        layout = findViewById(R.id.tInputLayout);
    }
}
