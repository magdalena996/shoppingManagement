package com.magdalena.shoppingmanagementnonvd.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Pair;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.PurchasedProduct;
import com.magdalena.shoppingmanagementnonvd.Tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PurchasedProductDAO extends ShoppingDBDAO
{
    private Set<Integer> allIds = new HashSet<>();

    public static List<PurchasedProduct> purchasedProducts;

    public PurchasedProductDAO(Context context)

    {
        super(context);
    }

    public void savePurchasedProduct(PurchasedProduct product)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_PRODUCT_ID, product.getId_product());
        values.put(DBHelper.KEY_QUANTITY, product.getCountPurchased());
        values.put(DBHelper.KEY_DATE, product.getPurchaseDate());
        values.put(DBHelper.KEY_SHOP, product.getShopName());
        values.put(DBHelper.KEY_UNIT, product.getUnit());
        myDatabase.insert(DBHelper.PURCHASED_TABLE, null, values);
    }

    public void loadPurchasedProductsFromDb()
    {
        purchasedProducts = new ArrayList<>();
        Cursor cursor = myDatabase.query(DBHelper.PURCHASED_TABLE,
                new String[]{DBHelper.KEY_ID, DBHelper.KEY_QUANTITY, DBHelper.KEY_DATE,
                        DBHelper.KEY_SHOP, DBHelper.KEY_UNIT, DBHelper.KEY_PRODUCT_ID},
                null, null, null, null, DBHelper.KEY_DATE + " DESC", null);
        while (cursor.moveToNext())
        {
            PurchasedProduct purchasedProduct = new PurchasedProduct();
            purchasedProduct.setId(cursor.getInt(0));
            purchasedProduct.setCountPurchased(cursor.getInt(1));
            purchasedProduct.setPurchaseDate(cursor.getString(2));
            purchasedProduct.setShopName(cursor.getString(3));
            purchasedProduct.setUnit(cursor.getString(4));
            purchasedProduct.setId_product(cursor.getInt(5));
            purchasedProducts.add(purchasedProduct);

        }
        cursor.close();
    }

    private int getTotalNumberOfPurchasedProduct(int productId)
    {
        Cursor cursor = myDatabase.rawQuery("SELECT SUM(" + (DBHelper.KEY_QUANTITY) + ") as Total FROM "
                + DBHelper.PURCHASED_TABLE + " WHERE " + DBHelper.KEY_PRODUCT_ID + "=" + productId, null);
        if (cursor.moveToFirst())
        {
            return cursor.getInt(cursor.getColumnIndex("Total"));
        }
        cursor.close();
        return 0;
    }

    private float getTotalProductExpenses(int productId)
    {
        String unit = PurchasedProduct.getPurchasedProdById(productId).getUnit();
        String price = Product.getProductById(productId).getPrice();
        if (!price.equals("nie podano"))
        {
            if (unit.equals("szt") || unit.equals("kg"))
            {
                return getTotalNumberOfPurchasedProduct(productId) * Float.parseFloat(price);
            } else if (unit.equals("dag"))
            {
                return (getTotalNumberOfPurchasedProduct(productId) / 100.0f) * Float.parseFloat(price);
            } else
            {
                return (getTotalNumberOfPurchasedProduct(productId) / 1000.0f) * Float.parseFloat(price);
            }
        } else
        {
            return 0;
        }
    }

    private void setAllIds()
    {
        for (PurchasedProduct p : purchasedProducts)
        {
            allIds.add(p.getId_product());
        }
    }

    private Set<Integer> getProductsIdByCategory(String category)
    {
        Set<Integer> ids = new HashSet<>();
        for (PurchasedProduct p : purchasedProducts)
        {
            String pCategory = Product.getProductById(p.getId_product()).getCategory();
            if (category.equals(pCategory))
            {
                ids.add(p.getId_product());
            }
        }
        return ids;
    }

    public float getExpensesByCategory(String category)
    {
        float totalExp = 0.0f;
        if (category.equals("Wszystkie"))
        {
            setAllIds();
            for (Integer id : allIds)
            {
                totalExp += getTotalProductExpenses(id);
            }
        } else
        {
            Set<Integer> idByCategory = getProductsIdByCategory(category);
            for (Integer id : idByCategory)
            {
                totalExp += getTotalProductExpenses(id);
            }
        }
        return totalExp;
    }

    public static Set<Pair<Integer, String>> getProductsToBuyID()
    {
        Set<Pair<Integer, String>> pToBuy = new HashSet<>();
        String startDate;
        String currentDate = Tools.getCurrentDate();
        int days;
        if (!purchasedProducts.isEmpty())
        {
            for (PurchasedProduct p : purchasedProducts)
            {
                days = Product.getProductById(p.getId_product()).getDays();

                switch (p.getUnit())
                {
                    case "szt":
                    case "kg":
                        days = days * p.getCountPurchased();
                        break;
                    case "dag":
                        days = (int) (days * ((float) p.getCountPurchased() / 100.0f));
                        break;
                    case "g":
                        days = (int) (days * ((float) p.getCountPurchased() / 1000.0f));
                        break;
                }

                startDate = Tools.subtractDaysFromDate(currentDate, days);
                if (days > 0)
                {
                    if (p.getPurchaseDate().compareTo(startDate) <= 0) // && p.getPurchaseDate().compareTo(currentDate) <= 0
                    {
                        pToBuy.add(new Pair<>(p.getId_product(), p.getUnit()));
                    }
                }
            }
        }
        return pToBuy;
    }
}

