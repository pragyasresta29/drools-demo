package com.poc.dynamicrules;

public class DroolsService {

    public String getRulesPath(){
        return getClass().getResource("Product.drl").getPath();
    }
}
