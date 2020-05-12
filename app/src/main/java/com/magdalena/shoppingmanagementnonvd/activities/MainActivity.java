package com.magdalena.shoppingmanagementnonvd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.NonPurchasedProduct;
import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ShoppingDBDAO;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Utils
{
    ActionBar actionBar;

    Button btnPList, btnActual, btnCreate, btnStats, btnGen;
    ImageView ivCloseApp;

    private ShoppingDBDAO dbDAO;
    private PurchasedProductDAO ppDb;

    public static String pFilter = "Wszystkie";
    public static List<Product> products;
    public static List<Product> allProducts;
    public static List<Product> fProducts;

    private Set<Pair<Integer, String>> pToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProductsDAO db = new ProductsDAO(this);
        db.open();
        loadProdData(db);
        updateList(db);
        db.close();

        dbDAO = new ShoppingDBDAO(this);

        ppDb = new PurchasedProductDAO(this);
        ppDb.open();
        ppDb.loadPurchasedProductsFromDb();
        ppDb.close();

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        setMapping();

        btnPList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, ProductsList.class));
            }
        });

        btnActual.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbDAO.open();
                dbDAO.loadActualListFromDb();
                dbDAO.close();

                if (ShoppingDBDAO.actualShoppingList.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "LISTA ZAKUPÓW NIE ZOSTAŁA UTWORZONA",
                            Toast.LENGTH_SHORT).show();
                } else
                {
                    startActivity(new Intent(MainActivity.this, ShoppingList.class));
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, CreateShoppingList.class));
            }
        });

        btnGen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dbDAO.open();
                dbDAO.loadActualListFromDb();
                dbDAO.loadNonPurchasedProductsFromDb();
                dbDAO.close();

                pToBuy = PurchasedProductDAO.getProductsToBuyID();

                if (ShoppingDBDAO.actualShoppingList.isEmpty())
                {
                    if (!pToBuy.isEmpty())
                    {
                        for (Pair<Integer, String> id : pToBuy)
                        {
                            ShoppingActualList product;
                            if (id.second.equals("szt") || id.second.equals("kg"))
                            {
                                product = new ShoppingActualList(id.first, 1, id.second);
                            } else if (id.second.equals("dag"))
                            {
                                product = new ShoppingActualList(id.first, 10, id.second);
                            } else // (id.second.equals("g")
                            {
                                product = new ShoppingActualList(id.first, 100, id.second);
                            }
                            ShoppingDBDAO.actualShoppingList.add(product);
                        }
                        pToBuy.clear();
                    }

                    if (!ShoppingDBDAO.nonPurchasedProducts.isEmpty())
                    {
                        for (NonPurchasedProduct p : ShoppingDBDAO.nonPurchasedProducts)
                        {
                            ShoppingActualList prod = new ShoppingActualList(p.getId_product(),
                                    p.getCountToBuy(), p.getUnit());
                            ShoppingDBDAO.actualShoppingList.add(prod);
                        }
                    }

                    dbDAO.open();
                    dbDAO.saveShoppingList(ShoppingDBDAO.actualShoppingList);
                    dbDAO.loadActualListFromDb();
                    dbDAO.deleteAllFromNonPurchased();
                    dbDAO.loadNonPurchasedProductsFromDb();
                    dbDAO.close();

                    if (!ShoppingDBDAO.actualShoppingList.isEmpty())
                    {
                        startActivity(new Intent(MainActivity.this, ShoppingList.class));
                    } else
                    {
                        Toast.makeText(getApplicationContext(), "BRAK MOŻLIWOŚCI WYGENEROWANIA LISTY",
                                Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(getApplicationContext(), "LISTA ZAKUPÓW JUŻ ISTNIEJE",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, Statistics.class));
            }
        });

        ivCloseApp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                MainActivity.this.finish();
            }
        });
    }

    public void loadProdData(ProductsDAO db)
    {
        db.open();
        db.addInitialProducts();
        products = db.getActiveProducts(db.getProducts());
        allProducts = db.getProducts();
        db.close();
    }

    public static void updateList(ProductsDAO db)
    {
        db.open();
        products = db.getActiveProducts(db.getProducts());
        fProducts = db.getActiveProducts(db.getFilteredProducts());
        db.close();
    }

    @Override
    public void setMapping()
    {
        btnPList = findViewById(R.id.btnProducts);
        btnActual = findViewById(R.id.btnActual);
        btnCreate = findViewById(R.id.btnCreateList);
        btnStats = findViewById(R.id.btnStats);
        btnGen = findViewById(R.id.btnGenList);

        ivCloseApp = findViewById(R.id.ivCloseApp);
    }
}
