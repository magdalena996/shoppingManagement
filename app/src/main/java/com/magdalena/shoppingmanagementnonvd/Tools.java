package com.magdalena.shoppingmanagementnonvd;

import com.magdalena.shoppingmanagementnonvd.sqlitedb.PurchasedProductDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Tools
{
    private static final Map<Integer, Integer> monthDay = new HashMap<Integer, Integer>()
    {
        {
            put(1, 31);
            put(2, 28);
            put(3, 31);
            put(4, 30);
            put(5, 31);
            put(6, 30);
            put(7, 31);
            put(8, 31);
            put(9, 30);
            put(10, 31);
            put(11, 30);
            put(12, 31);
        }
    };
    public static String startDate, endDate;

    //General tools

    public static void setDefaultDates()
    {
        startDate = "1900-1-1";
        endDate = "2099-1-1";
    }

    public static String getCurrentDate()
    {
        int year, month, day;
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        String sMonth = Integer.toString(month), sDay = Integer.toString(day);

        if (month < 10)
        {
            sMonth = "0" + sMonth;
        }

        if (day < 10)
        {
            sDay = "0" + sDay;
        }
        return year + "-" + sMonth + "-" + sDay;
    }

    public static String getFormattedPrice(String price)
    {
        if (!price.isEmpty())
        {
            String[] strings = price.split("[.]");
            String changedPrice = strings[0];

            if (strings.length == 1)
            {
                changedPrice += ".00";
            } else if (strings[1].length() == 1)
            {
                changedPrice += "." + strings[1] + "0";
            } else
            {
                changedPrice += "." + strings[1].substring(0, 2);
            }

            return changedPrice;
        }
        return null;
    }

    //Statistics Tools

    public static String subtractDate(String currentDate, String timePeriod)
    {
        String[] dateParts = currentDate.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        if (timePeriod.equals("tydzień"))
        {
            if (day - 7 > 0)
            {
                day = day - 7;
            } else
            {
                if (month - 1 > 0)
                {
                    month = month - 1;

                    if (month == 2 && year % 4 == 0 && year % 100 != 0)
                    {
                        day = monthDay.get(month) - 7 + day + 1;
                    } else
                    {
                        day = monthDay.get(month) - 7 + day;
                    }
                } else
                {
                    month = 12;
                    day = monthDay.get(month) - 7 + day;
                    year = year - 1;
                }
            }
        } else if (timePeriod.equals("miesiąc"))
        {
            if (month - 1 > 0)
            {
                month = month - 1;
            } else
            {
                month = 12;
                year = year - 1;
            }
        } else if (timePeriod.equals("kwartał"))
        {
            if (month - 3 > 0)
            {
                month = month - 3;
            } else
            {
                month = 12 + (month - 3);
                year = year - 1;
            }
        } else //(timePeriod.equals("rok"))
        {
            year = year - 1;
        }

        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDay = Integer.toString(day);

        if (month + 1 < 10)
        {
            sMonth = "0" + sMonth;
        }
        if (day < 10)
        {
            sDay = "0" + sDay;
        }
        return sYear + "-" + sMonth + "-" + sDay;
    }

    public static String subtractDaysFromDate(String date, int days)
    {
        String[] firstDateParts = date.split("-");

        int year = Integer.parseInt(firstDateParts[0]);
        int month = Integer.parseInt(firstDateParts[1]);
        int day = Integer.parseInt(firstDateParts[2]);

        if (day - days > 0)
        {
            day = day - days;
        } else
        {
            while (days > 0)
            {
                days = days - day;

                if (month - 1 > 0)
                {
                    month = month - 1;
                    day = monthDay.get(month);

                    if (month == 2 && year % 4 == 0 && year % 100 != 0)
                    {
                        day = day + 1;
                    }

                    if (day - days > 0)
                    {
                        day = day - days;
                        days = 0;
                    } else
                    {
                        days = days - day;
                        if (days != 0)
                        {
                            day = 0;
                        } else
                        {
                            if (month - 1 > 0)
                            {
                                month = month - 1;
                                day = monthDay.get(month);

                                if (month == 2 && year % 4 == 0 && year % 100 != 0)
                                {
                                    day = day + 1;
                                }
                            } else
                            {
                                year = year - 1;
                                month = 12;
                                day = monthDay.get(month);
                            }
                        }
                    }
                } else
                {
                    year = year - 1;
                    month = 12;
                    day = monthDay.get(month);

                    if (day - days > 0)
                    {
                        day = day - days;
                        days = 0;
                    } else
                    {
                        days = days - day;
                        if (days != 0)
                        {
                            day = 0;
                        } else
                        {
                            month = month - 1;
                            day = monthDay.get(month);

                            if (month == 2 && year % 4 == 0 && year % 100 != 0)
                            {
                                day = day + 1;
                            }
                        }
                    }
                }
            }
        }

        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDay = Integer.toString(day);

        if (month + 1 < 10)
        {
            sMonth = "0" + sMonth;
        }
        if (day < 10)
        {
            sDay = "0" + sDay;
        }
        return sYear + "-" + sMonth + "-" + sDay;
    }

    private static List<Integer> getPurchaseByDate(String startDate, String endDate)
    {
        List<Integer> prods = new ArrayList<>();

        for (PurchasedProduct p : PurchasedProductDAO.purchasedProducts)
        {
            if (p.getPurchaseDate().compareTo(startDate) >= 0 && p.getPurchaseDate().compareTo(endDate) <= 0)
            {
                prods.add(p.getId());
            }
        }
        return prods;
    }

    public static float getExpensesByDatesAndCategory(String category, String startDate, String endDate)
    {
        List<Integer> purchases = getPurchaseByDate(startDate, endDate);
        int count;
        String price, unit, prodCateg;
        float expenses, totalExp = 0.0f;

        for (Integer id : purchases)
        {
            PurchasedProduct purchasedP = PurchasedProduct.getPurchaseById(id);
            count = purchasedP.getCountPurchased();
            Product product = Product.getActiveProductById(purchasedP.getId_product());
            price = product.getPrice();
            unit = purchasedP.getUnit();
            prodCateg = product.getCategory();
            expenses = 0.0f;

            if (!price.equals("nie podano"))
            {
                if (unit.equals("szt") || unit.equals("kg"))
                {
                    expenses += count * Float.parseFloat(price);
                } else if (unit.equals("dag"))
                {
                    expenses += (count / 100.0f) * Float.parseFloat(price);
                } else
                {
                    expenses += (count / 1000.0f) * Float.parseFloat(price);
                }
            }

            if (category.equals("Wszystkie"))
            {
                totalExp += expenses;
            } else if (prodCateg.equals(category))
            {
                totalExp += expenses;
            }
        }
        return totalExp;
    }

}
