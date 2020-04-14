package com.poc.dynamicrules;


import lombok.Data;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Representation of condition to apply a business rule.
 *
 */
@Data
public class Condition {

    private String property;

    private Object value;

    private Operator operator;

    @Getter
    public static enum Operator
    {
        NOT_EQUAL_TO("Not equal to", "!=", Arrays.asList(
                String.class, Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class)),

        EQUAL_TO("Equal to", "==", Arrays.asList(
                String.class, Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class)),

        CONTAINS("Contains this", "?", Arrays.asList(String.class)),

        GREATER_THAN("Greater than", ">", Arrays.asList(
                Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class)),

        LESS_THAN("Less than", "<", Arrays.asList(
                Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class)),

        GREATER_THAN_OR_EQUAL_TO("Greater or equal to", ">=", Arrays.asList(
                Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class)),

        LESS_THAN_OR_EQUAL_TO("Less or equal to", "<=", Arrays.asList(
                Double.class, Float.class, Integer.class, Short.class, Long.class, Date.class));


        private final String description;

        private final String operation;

        private final List<Class> acceptables;

        private Operator(String description, String operation, List<Class> acceptables)
        {
            this.description = description;
            this.operation = operation;
            this.acceptables = acceptables;
        }


        /**
         * Indicates when the specified Class is comparable using this operator.
         *
         * @param clazz Class to verify.
         * @return True when this operator can be used.
         */
        public boolean isComparable(Class clazz)
        {
            for (Class accept : acceptables)
            {
                if (accept.equals(clazz))
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Gets the operator related to description.
         *
         * @param description Description for an operation.
         * @return Type of operator.
         * @throws EnumConstantNotPresentException When the description is not related to a valid operator.
         */
        public static Operator fromDescription(String description) throws EnumConstantNotPresentException
        {
            for (Operator operator : Operator.values())
            {
                if (operator.getDescription().equals(description))
                {
                    return operator;
                }
            }

            throw new EnumConstantNotPresentException(Operator.class, "? (" + description + ")");
        }
    }

    /**
     * Create a new empty condition.
     */
    public Condition()
    {
    }

    /**
     * Create a complete condition.
     *
     * @param property Data property to be evaluated.
     * @param operator Operator used to compare the data.
     * @param value Value to be evaluated.
     */
    public Condition(String property, Operator operator, Object value)
    {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Convert the condition to textual expression.
     *
     * @return The expression of this condition in dialect.
     * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
     */
    public String buildExpression() throws IllegalArgumentException
    {
        StringBuilder drl = new StringBuilder();

        if (value instanceof String)
        {
            drl.append(expressionForStringValue());
        }
        else if (value instanceof Number)
        {
            drl.append(expressionForNumberValue());
        }
        else if (value instanceof Date)
        {
            drl.append(expressionForDateValue());
        }
        else
        {
            throw new IllegalArgumentException("The class " + value.getClass().getSimpleName() + " of value is not acceptable.");
        }

        return drl.toString();
    }

    /**
     * Convert the condition for <b>String</b> value in expression.
     *
     * @return Expression in dialect.
     * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
     */
    private String expressionForStringValue() throws IllegalArgumentException
    {
        StringBuilder drl = new StringBuilder();

        if (operator.isComparable(String.class))
        {
            if (operator.equals(Condition.Operator.CONTAINS))
            {
                drl.append(property).append(".toUpperCase().contains(\"").append(((String)value).toUpperCase()).append("\")");
            }
            else
            {
                drl.append(property).append(" ").append(operator.getOperation()).append(" ").append("\"").append(value).append("\"");
            }
        }
        else
        {
            throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
        }

        return drl.toString();
    }

    /**
     * Convert the condition for <b>Integer</b>, <b>Double</b> or <b>Float</b> value in expression.
     *
     * @return Expression in dialect.
     * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
     */
    private String expressionForNumberValue() throws IllegalArgumentException
    {
        StringBuilder drl = new StringBuilder();

        if ((operator.isComparable(Short.class)) || (operator.isComparable(Integer.class)) || (operator.isComparable(Long.class))
                || (operator.isComparable(Double.class)) || (operator.isComparable(Float.class)))
        {
            drl.append(property).append(" ").append(operator.getOperation()).append(" ").append(value);
        }
        else
        {
            throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
        }

        return drl.toString();
    }

    /**
     * Convert the condition for <b>Date</b> value in expression.
     *
     * @return Expression in dialect.
     * @throws IllegalArgumentException Indicates the use of invalid pair of value and condition.
     */
    private String expressionForDateValue() throws IllegalArgumentException
    {
        StringBuilder drl = new StringBuilder();

        if (operator.isComparable(Date.class))
        {
            drl.append(property).append(" ").append(operator.getOperation()).append(" (new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\")).parse(\"" + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format((Date)value) + "\")");
        }
        else
        {
            throw new IllegalArgumentException("Is not possible to use the operator " + operator.getDescription() + " to a " + value.getClass().getSimpleName() + " object.");
        }

        return drl.toString();
    }

}