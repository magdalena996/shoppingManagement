package com.magdalena.shoppingmanagementnonvd;

public class ShoppingActualList
{
    private int id;
    private int id_product;
    private int countToBuy;
    private int countPurchased = 0;
    private String unit;

    public ShoppingActualList()
    {

    }

    public ShoppingActualList(int id_product, int countToBuy, String unit)
    {
        this.id_product = id_product;
        this.countToBuy = countToBuy;
        this.unit = unit;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCountPurchased(int countPurchased)
    {
        this.countPurchased = countPurchased;
    }

    public int getId()
    {
        return id;
    }

    public int getId_product()
    {
        return id_product;
    }

    public int getCountToBuy()
    {
        return countToBuy;
    }

    public int getCountPurchased()
    {
        return countPurchased;
    }

    public String getUnit()
    {
        return unit;
    }

/*    @Override
    public String toString()
    {
        return "ShoppingActualList{" +
                "id=" + id +
                ", id_product=" + id_product +
                ", countToBuy=" + countToBuy +
                ", countPurchased=" + countPurchased +
                ", unit='" + unit + '\'' +
                '}';
    }
*/
}
