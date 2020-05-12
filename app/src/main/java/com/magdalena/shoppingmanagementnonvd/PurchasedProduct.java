package com.magdalena.shoppingmanagementnonvd;

import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;

public class PurchasedProduct
{
    private int id;
    private int id_product;
    private int countPurchased;
    private String purchaseDate;
    private String shopName;
    private String unit;

    public PurchasedProduct()
    {

    }

    public PurchasedProduct(int id_product, int countPurchased, String purchaseDate, String shopName, String unit)
    {
        this.id_product = id_product;
        this.countPurchased = countPurchased;
        this.purchaseDate = purchaseDate;
        this.shopName = shopName;
        this.unit = unit;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setId_product(int id_product)
    {
        this.id_product = id_product;
    }

    public void setCountPurchased(int countPurchased)
    {
        this.countPurchased = countPurchased;
    }

    public void setPurchaseDate(String purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public int getId()
    {
        return id;
    }

    public int getId_product()
    {
        return id_product;
    }

    public int getCountPurchased()
    {
        return countPurchased;
    }

    public String getPurchaseDate()
    {
        return purchaseDate;
    }

    public String getShopName()
    {
        return shopName;
    }

    public String getUnit()
    {
        return unit;
    }

    public static PurchasedProduct getPurchasedProdById(int id)
    {
        for (PurchasedProduct p : PurchasedProductDAO.purchasedProducts)
        {
            if (p.getId_product() == id)
            {
                return p;
            }
        }
        return null;
    }

    public static PurchasedProduct getPurchaseById(int id)
    {
        for (PurchasedProduct p : PurchasedProductDAO.purchasedProducts)
        {
            if (p.getId() == id)
            {
                return p;
            }
        }
        return null;
    }
}
