package com.poc.dynamicrules;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representation of Product to buy or sell, depending on context.
 *
 */
@Data
public class Product {

    private final String name;

    private double price;

    private final Date dueDate;

    public Product(String name, double price, String dueDate) throws ParseException
    {
        this.name = name;
        this.price = price;
        this.dueDate = (new SimpleDateFormat("dd/MM/yyyy")).parse(dueDate);
    }

    public Product(String name, double price, Date dueDate)
    {
        this.name = name;
        this.price = price;
        this.dueDate = dueDate;
    }

    /**
     * Apply the discount on product.
     *
     * @param percent Percent to apply.
     */
    public void discount(double percent)
    {
        price = (price - ((percent * price) / 100));
    }

}
