package com.poc.dynamicrules;


import org.kie.api.runtime.StatelessKieSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation showing how to use drools to create rules dynamically and execute them over object data in memory.
 *
 */
public class DynamicRulesTest
{
    public static void main(String args[]) throws Exception
    {
        //List to keep all rules
        List<Rule> rules = new ArrayList<Rule>();
        //Load each business rule
        rules.add(createDiscountOverpriced());
        rules.add(createDiscountSoonDueDate());
        rules.add(createDiscountBeans());

        //Create a session to operate Drools in memory
        DroolsUtility utility = new DroolsUtility();
        DroolsService droolsService = new DroolsService();
        StatelessKieSession session = utility.loadSession(rules, "Product.drl");

        //Define the products to be processed using our rules
        Product blackBeans = new Product("Black Beans", 2.20, "30/12/2017");
        Product cannelliniBeans = new Product("Cannellini Beans", 4.15, "05/02/2018");
        Product kidneyBeans = new Product("Kidney Beans", 2.05, "20/11/2017");
        Product rice = new Product("Rice", 1.10, "28/10/2017");
        Product milk = new Product("Milk", 3.50, "10/11/2017");

		/*
		Now, the magic happens!
		For each product to be processed, we have to face it over rules to get, or not, a discounted price.
		*/
        System.out.println("Applying over " + rice.getName() + " with price $" + rice.getPrice() + "...");
        session.setGlobal("product", rice);
        session.execute(rice);
        System.out.println("...price after review: $" + rice.getPrice());

        System.out.println("Applying over " + blackBeans.getName() + " with price $" + blackBeans.getPrice() + "...");
        session.setGlobal("product", blackBeans);
        session.execute(blackBeans);
        System.out.println("...price after review: $" + blackBeans.getPrice());

        System.out.println("Applying over " + milk.getName() + " with price $" + milk.getPrice() + "...");
        session.setGlobal("product", milk);
        session.execute(milk);
        System.out.println("...price after review: $" + milk.getPrice());

        System.out.println("Applying over " + kidneyBeans.getName() + " with price $" + kidneyBeans.getPrice() + "...");
        session.setGlobal("product", kidneyBeans);
        session.execute(kidneyBeans);
        System.out.println("...price after review: $" + kidneyBeans.getPrice());

        System.out.println("Applying over " + cannelliniBeans.getName() + " with price $" + cannelliniBeans.getPrice() + "...");
        session.setGlobal("product", cannelliniBeans);
        session.execute(cannelliniBeans);
        System.out.println("...price after review: $" + cannelliniBeans.getPrice());
    }

    /**
     * In this example of rule creation, our boss wanna be nice and give a discount of 10% on all products with price over $4.00.
     *
     * @return Business rule
     */
    private static Rule createDiscountOverpriced()
    {
        //First of all, we create a rule giving it a friendly name
        Rule rule = new Rule("Give some discount on overpriced");
        //Here we need to say what kind of object will be processed
        rule.setObject(Product.class.getName());

        //As expected, a rule needs condition to exists. So, let's create it...
        Condition condition = new Condition();
        //What data, or property, will be checked
        condition.setProperty("price");
        //What kind of check to do
        condition.setOperator(Condition.Operator.GREATER_THAN_OR_EQUAL_TO);
        //What is the value to check
        condition.setValue(new Double(4.0));

        //Next, is needed to set rule's condition
        rule.setCondition(condition);
        //Finally, this is what will be done when ours condition is satisfied
        rule.setAction("10");

        return rule;
    }

    /**
     * Well, some products are becoming not so good. Sell it soon as possible!
     *
     */
    private static Rule createDiscountSoonDueDate() throws Exception
    {
        Rule rule = new Rule("Apply discount on all soon due date");
        rule.setObject(Product.class.getName());

        //Is possible to create multiple conditions, therefore, data range or more complex situations could be expressed
        Condition greaterThan = new Condition();
        greaterThan.setProperty("dueDate");
        greaterThan.setOperator(Condition.Operator.GREATER_THAN);
        greaterThan.setValue((new SimpleDateFormat("dd/MM/yyyy").parse("23/10/2017")));

        Condition lessThan = new Condition();
        lessThan.setProperty("dueDate");
        lessThan.setOperator(Condition.Operator.LESS_THAN);
        lessThan.setValue((new SimpleDateFormat("dd/MM/yyyy").parse("30/10/2017")));

        //You can define as many as necessary conditions to achieve your necessity
        rule.setConditions(Arrays.asList(greaterThan, lessThan));
        rule.setAction("45");

        return rule;
    }

    /**
     * Beans for all!
     */
    private static Rule createDiscountBeans()
    {
        Rule rule = new Rule("Discounting on all beans");
        rule.setObject(Product.class.getName());

        //This is the simplest way to define the rule' condition
        rule.addCondition("name", Condition.Operator.CONTAINS, "Beans");

        rule.setAction("5");

        return rule;
    }
}
