package com.magdalena.shoppingmanagementnonvd.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.magdalena.shoppingmanagementnonvd.Product;
import com.magdalena.shoppingmanagementnonvd.ProductBuilder;
import com.magdalena.shoppingmanagementnonvd.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductsDAO extends ShoppingDBDAO
{
    private static final String WHERE_ID_EQUALS = DBHelper.KEY_ID + " =?";

    public ProductsDAO(Context context)
    {
        super(context);
    }

    public void saveProduct(Product product)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_PRODUCT, product.getName());
        values.put(DBHelper.KEY_CATEGORY, product.getCategory());
        values.put(DBHelper.KEY_PRICE, product.getPrice());
        values.put(DBHelper.KEY_DAYS, product.getDays());
        values.put(DBHelper.KEY_ACTIVE, product.isActive());

        myDatabase.insert(DBHelper.PRODUCTS_TABLE, null, values);
    }

    public void updateProduct(Product product)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_PRODUCT, product.getName());
        values.put(DBHelper.KEY_CATEGORY, product.getCategory());
        values.put(DBHelper.KEY_PRICE, product.getPrice());
        values.put(DBHelper.KEY_DAYS, product.getDays());
        values.put(DBHelper.KEY_ACTIVE, product.isActive());

        myDatabase.update(DBHelper.PRODUCTS_TABLE, values, WHERE_ID_EQUALS,
                new String[]{String.valueOf(product.getId())});
    }


    public void changeDays(int id, int days)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_DAYS, days);

        myDatabase.update(DBHelper.PRODUCTS_TABLE, values, WHERE_ID_EQUALS,
                new String[]{String.valueOf(id)});
    }

    public void deleteProduct(Product product)
    {
        if (product.getDays() == -1)
        {
            myDatabase.delete(DBHelper.PRODUCTS_TABLE, WHERE_ID_EQUALS,
                    new String[]{String.valueOf(product.getId())});
        } else if (product.getDays() > -1)
        {
            product.setIsActive(0);
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_ACTIVE, product.isActive());

            myDatabase.update(DBHelper.PRODUCTS_TABLE, values, WHERE_ID_EQUALS,
                    new String[]{String.valueOf(product.getId())});
        }
    }

    public List<Product> getProducts()
    {
        List<Product> products = new ArrayList<>();
        Cursor cursor = myDatabase.query(DBHelper.PRODUCTS_TABLE,
                new String[]{DBHelper.KEY_ID, DBHelper.KEY_PRODUCT, DBHelper.KEY_CATEGORY,
                        DBHelper.KEY_PRICE, DBHelper.KEY_DAYS, DBHelper.KEY_ACTIVE},
                null, null, null, null, DBHelper.KEY_PRODUCT + " ASC", null);
        while (cursor.moveToNext())
        {
            Product product = new ProductBuilder(cursor.getString(1), cursor.getString(2))
                    .price(cursor.getString(3))
                    .days(cursor.getInt(4))
                    .isActive(cursor.getInt(5))
                    .build();
            product.setId(cursor.getInt(0));
            products.add(product);
        }
        cursor.close();
        return products;
    }

    public List<Product> getFilteredProducts()
    {
        List<Product> filteredProducts = new ArrayList<>();
        Cursor cursor = myDatabase.query(DBHelper.PRODUCTS_TABLE,
                new String[]{DBHelper.KEY_ID, DBHelper.KEY_PRODUCT, DBHelper.KEY_CATEGORY,
                        DBHelper.KEY_PRICE, DBHelper.KEY_DAYS, DBHelper.KEY_ACTIVE},
                null, null, null, null, DBHelper.KEY_PRODUCT + " ASC", null);
        while (cursor.moveToNext())
        {
            Product product = new ProductBuilder(cursor.getString(1), cursor.getString(2))
                    .price(cursor.getString(3))
                    .days(cursor.getInt(4))
                    .isActive(cursor.getInt(5))
                    .build();
            product.setId(cursor.getInt(0));

            if (MainActivity.pFilter.equals("Wszystkie"))
            {
                filteredProducts.add(product);
            } else if (product.getCategory().equals(MainActivity.pFilter))
            {
                filteredProducts.add(product);
            }
        }
        cursor.close();
        return filteredProducts;
    }

    public List<Product> getActiveProducts(List<Product> products)
    {
        List<Product> activeProd = new ArrayList<>();

        for (Product p : products)
        {
            if (p.isActive() == 1)
            {
                activeProd.add(p);
            }
        }
        return activeProd;
    }

    public void addInitialProducts()
    {
        if (isEmpty())
        {
            List<Product> products = new ArrayList<>();
            products.add(new Product("Chleb", "Artykuły spożywcze"));
            products.add(new Product("Masło", "Artykuły spożywcze"));
            products.add(new Product("Cukier", "Artykuły spożywcze"));
            products.add(new Product("Makaron", "Artykuły spożywcze"));
            products.add(new Product("Olej", "Artykuły spożywcze"));
            products.add(new Product("Sok", "Artykuły spożywcze"));
            products.add(new Product("Woda", "Artykuły spożywcze"));

            products.add(new Product("Krem", "Kosmetyki"));
            products.add(new Product("Mydło", "Kosmetyki"));
            products.add(new Product("Pasta do zębów", "Kosmetyki"));
            products.add(new Product("Antyperspirant", "Kosmetyki"));
            products.add(new Product("Szampon", "Kosmetyki"));

            products.add(new Product("Płyn do naczyń", "Chemia gospodarcza"));
            products.add(new Product("Płyn do płukania", "Chemia gospodarcza"));
            products.add(new Product("Proszek do prania", "Chemia gospodarcza"));
            products.add(new Product("Papier toaletowy", "Chemia gospodarcza"));
            products.add(new Product("Chusteczki", "Chemia gospodarcza"));

            products.add(new Product("Karma dla psa", "Dla zwierząt"));
            products.add(new Product("Pedigree", "Dla zwierząt"));
            products.add(new Product("Żwirek", "Dla zwierząt"));
            products.add(new Product("Whiskas", "Dla zwierząt"));
            products.add(new Product("Karma dla kota", "Dla zwierząt"));

            products.add(new Product("Pampersy", "Dla dzieci"));
            products.add(new Product("Blok rysunkowy", "Dla dzieci"));
            products.add(new Product("Oliwka", "Dla dzieci"));
            products.add(new Product("Chusteczki nawilżane", "Dla dzieci"));
            products.add(new Product("Kaszka BoboVita", "Dla dzieci"));

            products.add(new Product("Bateria", "Inne"));
            products.add(new Product("Żarówka", "Inne"));
            products.add(new Product("Długopis", "Inne"));

            for (Product prod : products)
            {
                ContentValues values = new ContentValues();
                values.put(DBHelper.KEY_PRODUCT, prod.getName());
                values.put(DBHelper.KEY_CATEGORY, prod.getCategory());
                values.put(DBHelper.KEY_PRICE, prod.getPrice());
                values.put(DBHelper.KEY_DAYS, prod.getDays());
                values.put(DBHelper.KEY_ACTIVE, prod.isActive());
                myDatabase.insert(DBHelper.PRODUCTS_TABLE, null, values);
            }
        }
    }

    public boolean isEmpty()
    {
        return getProducts().isEmpty();
    }

}


