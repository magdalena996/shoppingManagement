package com.magdalena.shoppingmanagementnonvd.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.magdalena.shoppingmanagementnonvd.NonPurchasedProduct;
import com.magdalena.shoppingmanagementnonvd.ShoppingActualList;

import java.util.ArrayList;
import java.util.List;


public class ShoppingDBDAO
{
    private DBHelper dbHelper;
    private Context myContext;
    protected SQLiteDatabase myDatabase;
    private static final String WHERE_ID_EQUALS = DBHelper.KEY_ID + " =?";

    public static List<ShoppingActualList> actualShoppingList;
    public static List<NonPurchasedProduct> nonPurchasedProducts;

    public ShoppingDBDAO(Context context)
    {
        this.myContext = context;
        dbHelper = DBHelper.getHelper(myContext);
        open();
    }

    public void open() throws SQLException
    {
        if (dbHelper == null)
        {
            dbHelper = DBHelper.getHelper(myContext);
        }
        myDatabase = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
        myDatabase = null;
    }

    //Shopping list table DAO

    public void saveShoppingList(List<ShoppingActualList> list)
    {
        for (ShoppingActualList product : list)
        {
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_PRODUCT_ID, product.getId_product());
            values.put(DBHelper.KEY_TO_BUY, product.getCountToBuy());
            values.put(DBHelper.KEY_PURCHASED, product.getCountPurchased());
            values.put(DBHelper.KEY_UNIT, product.getUnit());

            myDatabase.insert(DBHelper.SHOPPING_TABLE, null, values);
        }
    }

    public void loadActualListFromDb()
    {
        actualShoppingList = new ArrayList<>();
        Cursor cursor = myDatabase.query(DBHelper.SHOPPING_TABLE,
                new String[]{DBHelper.KEY_ID, DBHelper.KEY_TO_BUY, DBHelper.KEY_PURCHASED,
                        DBHelper.KEY_UNIT, DBHelper.KEY_PRODUCT_ID}, null,
                null, null, null, null, null);
        while (cursor.moveToNext())
        {
            ShoppingActualList product = new ShoppingActualList(
                    cursor.getInt(4),
                    cursor.getInt(1),
                    cursor.getString(3));
            product.setId(cursor.getInt(0));
            product.setCountPurchased(cursor.getInt(2));
            actualShoppingList.add(product);
        }
        cursor.close();
    }

    public void updateProductInActualList(ShoppingActualList product)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_TO_BUY, product.getCountToBuy());
        values.put(DBHelper.KEY_PURCHASED, product.getCountPurchased());

        myDatabase.update(DBHelper.SHOPPING_TABLE, values, WHERE_ID_EQUALS,
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteProductFromList(ShoppingActualList product)
    {
        myDatabase.delete(DBHelper.SHOPPING_TABLE, WHERE_ID_EQUALS,
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteAllFromActualList()
    {
        myDatabase.execSQL("DELETE FROM " + DBHelper.SHOPPING_TABLE);
    }

    //Non-purchased table DAO

    public void saveNonPurchasedProduct(NonPurchasedProduct product)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_PRODUCT_ID, product.getId_product());
        values.put(DBHelper.KEY_QUANTITY, product.getCountToBuy());
        values.put(DBHelper.KEY_UNIT, product.getUnit());

        myDatabase.insert(DBHelper.NONPURCHASED_TABLE, null, values);
    }

    public void loadNonPurchasedProductsFromDb()
    {
        nonPurchasedProducts = new ArrayList<>();
        Cursor cursor = myDatabase.query(DBHelper.NONPURCHASED_TABLE,
                new String[]{DBHelper.KEY_ID, DBHelper.KEY_QUANTITY, DBHelper.KEY_UNIT,
                        DBHelper.KEY_PRODUCT_ID}, null,
                null, null, null, null, null);
        while (cursor.moveToNext())
        {
            NonPurchasedProduct product = new NonPurchasedProduct(
                    cursor.getInt(3),
                    cursor.getInt(1),
                    cursor.getString(2));
            product.setId(cursor.getInt(0));
            nonPurchasedProducts.add(product);
        }
        cursor.close();
    }

    public void deleteAllFromNonPurchased()
    {
        myDatabase.execSQL("DELETE FROM " + DBHelper.NONPURCHASED_TABLE);
    }

}
