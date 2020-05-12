package com.magdalena.shoppingmanagementnonvd.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "ShoppingManagementDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String PRODUCTS_TABLE = "ProductsTable";
    public static final String SHOPPING_TABLE = "ShoppingListTable";
    public static final String PURCHASED_TABLE = "PurchasedProductsTable";
    public static final String NONPURCHASED_TABLE = "NonPurchasedProductsTable";

    // Common column name
    public static final String KEY_ID = "_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_UNIT = "unit";

    // PRODUCTS table - column names
    public static final String KEY_PRODUCT = "product_name";
    public static final String KEY_CATEGORY = "_category";
    public static final String KEY_PRICE = "_price";
    public static final String KEY_DAYS = "_days";
    public static final String KEY_ACTIVE = "is_active";

    // SHOPPING table - column names
    public static final String KEY_TO_BUY = "quantity_to_buy";
    public static final String KEY_PURCHASED = "quantity_purchased";

    // PURCHASED table - column names
    public static final String KEY_DATE = "date_of_purchase";
    public static final String KEY_SHOP = "shop_name";

    // Table Create Statements
    // Products table create statement
    public static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + PRODUCTS_TABLE
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_PRODUCT
            + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_PRICE + " TEXT, "
            + KEY_DAYS + " INTEGER, " + KEY_ACTIVE + " INTEGER);";

    // Shopping list table create statement
    public static final String CREATE_SHOPPING_TABLE = "CREATE TABLE " + SHOPPING_TABLE
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TO_BUY
            + " INTEGER, " + KEY_PURCHASED + " INTEGER, " + KEY_UNIT + " TEXT, "
            + KEY_PRODUCT_ID + " INTEGER, " + "FOREIGN KEY(" + KEY_PRODUCT_ID + ") REFERENCES "
            + PRODUCTS_TABLE + "(_id) );";

    // Purchased products table create statement
    public static final String CREATE_PURCHASED_TABLE = "CREATE TABLE " + PURCHASED_TABLE
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUANTITY
            + " INTEGER, " + KEY_DATE + " TEXT, " + KEY_SHOP + " TEXT, " + KEY_UNIT + " TEXT, "
            + KEY_PRODUCT_ID + " INTEGER, " + "FOREIGN KEY(" + KEY_PRODUCT_ID + ") REFERENCES "
            + PRODUCTS_TABLE + "(_id) );";

    // Non-purchased products table create statement
    public static final String CREATE_NONPURCHASED_TABLE = "CREATE TABLE " + NONPURCHASED_TABLE
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUANTITY + " INTEGER, "
            + KEY_UNIT + " TEXT, " + KEY_PRODUCT_ID + " INTEGER, " + "FOREIGN KEY(" + KEY_PRODUCT_ID
            + ") REFERENCES " + PRODUCTS_TABLE + "(_id) );";

    private static DBHelper instance;

    public static synchronized DBHelper getHelper(Context context)
    {
        if (instance == null)
        {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Creating required tables
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_SHOPPING_TABLE);
        db.execSQL(CREATE_PURCHASED_TABLE);
        db.execSQL(CREATE_NONPURCHASED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // drop older tables and create new tables
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHOPPING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASED_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NONPURCHASED_TABLE);

        onCreate(db);

    }

}