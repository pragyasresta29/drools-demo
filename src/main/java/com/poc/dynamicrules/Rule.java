package com.poc.dynamicrules;


import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business rule representation<br>
 * This class encapsulates all necessary elements to define a specific rule using <b>Drools</b>.
 *
 */
@Data
public class Rule {

    private final String name;

    private String object;

    private List<Condition> conditions = new ArrayList<Condition>();

    private String action;


    public Rule(String name)
    {
        this.name = name;
    }

    /**
     * List of attributes available to use in template.<br>
     * These names must be the same used to write the .drl file template, which is compiled in runtime.
     */
    @Getter
    public enum Attribute
    {

        RULE_NAME("name"),
        DATA_OBJECT("object"),
        CONDITIONAL("conditional"),
        ACTION("action");

        private final String name;

        private Attribute(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }


    /**
     * Converts these conditionals to Drools Rule Language (DRL) format.<br>
     * The formatted conditional is in dialect Java (<i>dialect "java"</i>).
     *
     * @return Rule's conditional expression.
     * @throws IllegalStateException Indicates none conditional declared.
     * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
     */
    public String conditionAsDRL() throws IllegalStateException, IllegalArgumentException
    {
        if ((conditions == null) || (conditions.isEmpty()))
        {
            throw new IllegalStateException("You must declare at least one condition to be evaluated.");
        }

        StringBuilder drl = new StringBuilder();
        //For each condition of this rule, we create its textual representation
        for (int i = 0; i < conditions.size(); i++)
        {
            Condition condition = conditions.get(i);
            drl.append("(");
            drl.append(condition.buildExpression());
            drl.append(")");
            if ((i + 1) < conditions.size())
            {
                drl.append(" && ");
            }
        }

        return drl.toString();
    }

    /**
     * Returns the created rule as a map of its properties to be compiled with template.
     *
     * @return Map of rule's properties.
     * @throws IllegalStateException Indicate a non valid rule.
     */
    public Map<String, Object> asMap() throws IllegalStateException
    {
        if ((name == null) || (object == null) || (action == null))
        {
            throw new IllegalArgumentException("The rule has no name, object to be evaluated or action to be accomplished.");
        }

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Rule.Attribute.RULE_NAME.toString(), name);
        attributes.put(Rule.Attribute.DATA_OBJECT.toString(), object);
        attributes.put(Rule.Attribute.CONDITIONAL.toString(), conditionAsDRL());
        attributes.put(Rule.Attribute.ACTION.toString(), action);

        return attributes;
    }

    /**
     * Create new condition and add it to this rule.
     *
     * @param property Object property to be evaluated.
     * @param operator Operator used to compare the data.
     * @param value Value to be evaluated.
     * @return Condition created.
     */
    public Condition addCondition(String property, Condition.Operator operator, Object value)
    {
        Condition condition = new Condition(property, operator, value);
        conditions.add(condition);

        return condition;
    }

    public Condition getCondition()
    {
        if ((conditions == null) || (conditions.isEmpty()))
        {
            return null;
        }
        else
        {
            return conditions.get(0);
        }
    }

    public void setCondition(Condition condition)
    {
        conditions = new ArrayList<Condition>();
        conditions.add(condition);
    }

}
