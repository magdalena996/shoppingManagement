package com.magdalena.shoppingmanagementnonvd.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magdalena.shoppingmanagementnonvd.NonPurchasedProduct;
import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.PurchasedProduct;
import com.magdalena.shoppingmanagementnonvd.R;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;
import com.magdalena.shoppingmanagementnonvd.Tools;
import com.magdalena.shoppingmanagementnonvd.Utils;
import com.magdalena.shoppingmanagementnonvd.adapters.BuyListAdapter;
import com.magdalena.shoppingmanagementnonvd.fragments.FragmentUtils;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ProductsDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;
import com.magdalena.shoppingmanagementnonvd.sqlitedb.ShoppingDBDAO;

public class ShoppingList extends AppCompatActivity implements BuyListAdapter.ItemClicked, Utils
{
    ActionBar actionBar;

    FragmentManager fragmentManager;
    Fragment buyListFrag;

    EditText etShopName;
    TextView tvInfo, tvInfo2;
    Button btnDone;
    LinearLayout lnLayShop;

    private ShoppingDBDAO dbDAO;
    private ProductsDAO db;
    private PurchasedProductDAO ppDb;

    private ShoppingActualList shoppingList;
    private NonPurchasedProduct nonPurchasedProduct;

    private String currentDate;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        ppDb = new PurchasedProductDAO(this);
        dbDAO = new ShoppingDBDAO(this);
        db = new ProductsDAO(this);

        shoppingList = new ShoppingActualList();

        actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.title_shopping_list);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setMapping();
        checkExistsAndHide();

        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbDAO.open();
                dbDAO.loadActualListFromDb();
                dbDAO.loadNonPurchasedProductsFromDb();

                if (!ShoppingDBDAO.actualShoppingList.isEmpty())
                {
                    for (ShoppingActualList p : ShoppingDBDAO.actualShoppingList)
                    {
                        nonPurchasedProduct = new NonPurchasedProduct(p.getId_product(), p.getCountToBuy(), p.getUnit());
                        dbDAO.saveNonPurchasedProduct(nonPurchasedProduct);
                    }

                    dbDAO.deleteAllFromActualList();
                    dbDAO.loadActualListFromDb();
                }
                dbDAO.close();

                Toast.makeText(getApplicationContext(), "Pomyślnie zapisano dane",
                        Toast.LENGTH_SHORT).show();
                ShoppingList.this.finish();
            }
        });
    }

    @Override
    public void onItemClicked(int index)
    {
        int countToBuy;

        currentDate = Tools.getCurrentDate();

        shopName = etShopName.getText().toString().trim();
        shoppingList = ShoppingDBDAO.actualShoppingList.get(index);

        countToBuy = shoppingList.getCountToBuy();
        shoppingList.setCountPurchased(countToBuy);
        update();
        addPurchased();
        delete();

        if (Product.getActiveProductById(shoppingList.getId_product()).getDays() < 0)
        {
            db.open();
            db.changeDays(shoppingList.getId_product(), 0);
            db.close();
        }

        FragmentUtils.refreshFrag(fragmentManager, buyListFrag);

        Context context = getApplicationContext();
        CharSequence text = "Zakupiono " + Product.getActiveProductById(shoppingList.getId_product())
                .getName().toLowerCase();
        int duration = Toast.LENGTH_SHORT;

        if (ShoppingDBDAO.actualShoppingList.isEmpty())
        {
            text = "Pomyślnie zapisano dane";
            ShoppingList.this.finish();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onResumeFragments()
    {
        super.onResumeFragments();
    }

    public void delete()
    {
        dbDAO.open();
        dbDAO.deleteProductFromList(shoppingList);
        dbDAO.loadActualListFromDb();
        dbDAO.close();
    }

    public void update()
    {
        dbDAO.open();
        dbDAO.updateProductInActualList(shoppingList);
        dbDAO.loadActualListFromDb();
        dbDAO.close();
    }

    public void addPurchased()
    {
        PurchasedProduct purchasedProduct = new PurchasedProduct(
                shoppingList.getId_product(),
                shoppingList.getCountPurchased(),
                currentDate,
                shopName,
                shoppingList.getUnit());
        ppDb.open();
        ppDb.savePurchasedProduct(purchasedProduct);
        ppDb.close();
    }

    public void checkExistsAndHide()
    {
        tvInfo2.setVisibility(View.GONE);

        if (ShoppingDBDAO.actualShoppingList.isEmpty())
        {
            fragmentManager.beginTransaction()
                    .hide(buyListFrag)
                    .commit();
            lnLayShop.setVisibility(View.GONE);
            tvInfo2.setText(R.string.info2);
            tvInfo2.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMapping()
    {
        fragmentManager = getSupportFragmentManager();
        buyListFrag = fragmentManager.findFragmentById(R.id.buyListFrag);

        etShopName = findViewById(R.id.etShopName);
        tvInfo = findViewById(R.id.tvInfo);
        btnDone = findViewById(R.id.btnDone);
        tvInfo2 = findViewById(R.id.tvInfo2);
        lnLayShop = findViewById(R.id.lnlayShop);
    }
}
