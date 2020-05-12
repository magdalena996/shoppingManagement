package com.magdalena.shoppingmanagementnonvd;

public class NonPurchasedProduct extends ShoppingActualList
{
    private int id;
    private int id_product;
    private int countToBuy;
    private String unit;

    public NonPurchasedProduct(int id_product, int countToBuy, String unit)
    {
        super(id_product, countToBuy, unit);

        this.id_product = id_product;
        this.countToBuy = countToBuy;
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

    public void setCountToBuy(int countToBuy)
    {
        this.countToBuy = countToBuy;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public int getId_product()
    {
        return id_product;
    }

    public int getCountToBuy()
    {
        return countToBuy;
    }

    public String getUnit()
    {
        return unit;
    }

    public int getId()
    {
        return id;
    }

/*    @Override
    public String toString()
    {
        return "NonPurchasedProduct{" +
                "id=" + id +
                ", id_product=" + id_product +
                ", countToBuy=" + countToBuy +
                ", unit='" + unit + '\'' +
                '}';
    }
*/
}
