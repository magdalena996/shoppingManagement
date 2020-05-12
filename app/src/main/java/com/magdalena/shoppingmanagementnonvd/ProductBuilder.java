package com.magdalena.shoppingmanagementnonvd;

public class ProductBuilder
{
    private String name;
    private String category;
    private String purchaseDate = "brak";
    private int days = -1;
    private String price = "nie podano";
    private int isActive = 1;

    public ProductBuilder(String name, String category)
    {
        this.name = name;
        this.category = category;
    }

    public ProductBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public ProductBuilder category(String category)
    {
        this.category = category;
        return this;
    }

    public ProductBuilder purchaseDate(String purchaseDate)
    {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public ProductBuilder days(int days)
    {
        this.days = days;
        return this;
    }

    public ProductBuilder price(String price)
    {
        this.price = price;
        return this;
    }

    public ProductBuilder isActive(int isActive)
    {
        this.isActive = isActive;
        return this;
    }

    public Product build()
    {
        return new Product(name, category, purchaseDate, days, price, isActive);
    }
}
