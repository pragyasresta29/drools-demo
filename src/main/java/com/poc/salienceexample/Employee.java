package com.poc.salienceexample;

import lombok.Data;

@Data
public class Employee {

    String name;
    boolean manager;
    String message;
    Department dept;
    boolean filter;

}