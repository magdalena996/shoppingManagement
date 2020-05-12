package com.magdalena.shoppingmanagementnonvd;

import com.magdalena.shoppingmanagementnonvd.activities.MainActivity;

public class Product
{
    private int id;
    private String name;
    private String category;
    private String purchaseDate;
    private String price;
    private int days;
    private int isActive;

    public Product(String name, String category)
    {
        this.name = name;
        this.category = category;
        this.purchaseDate = "brak";
        this.days = -1;
        this.price = "nie podano";
        this.isActive = 1;
    }

    public Product(String name, String category, String purchaseDate, int days, String price, int isActive)
    {
        this.name = name;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.days = days;
        this.price = price;
        this.isActive = isActive;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public void setIsActive(int isActive)
    {
        this.isActive = isActive;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public int getDays()
    {
        return days;
    }

    public int isActive()
    {
        return isActive;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getPrice()
    {
        return price;
    }

    public static Product getActiveProductById(int id)
    {
        for (Product p : MainActivity.products)
        {
            if (p.getId() == id)
            {
                return p;
            }
        }
        return null;
    }

    public static Product getProductById(int id)
    {
        for (Product p : MainActivity.allProducts)
        {
            if (p.getId() == id)
            {
                return p;
            }
        }
        return null;
    }

/*    @Override
    public String toString()
    {
        return "Name: " + name + " Category: " + category
                + "\nPrice: " + price
                + "\nDays: " + days;
    }
*/
}

