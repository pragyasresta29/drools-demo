package com.poc.salienceexample;

import org.drools.compiler.compiler.DroolsParserException;
import org.kie.api.runtime.KieSession;

import java.io.IOException;

public class SalienceTest {

    public static void main(String[] args) throws DroolsParserException,
            IOException {
        SalienceTest droolsTest = new SalienceTest();
        //droolsTest.executeDrools();
        droolsTest.executeDroolsEmployee("/com/rules/employeeSalience.drl");
    }
    public void executeDroolsEmployee(String ruleFile) throws DroolsParserException, IOException {

        DroolsService droolsService = new DroolsService();
        KieSession kieSession = droolsService.getKieSession();
        Department dep = new Department();
        dep.setName("Civil");
        Department dep1 = new Department();
        dep1.setName("IT");
        Employee emp = new Employee();
        emp.setName("Jane Doe");
        emp.setManager(true);
        emp.setDept(dep1);
        Employee emp2 = new Employee();
        emp2.setName("Amy Tyson");
        emp2.setManager(false);
        emp2.setDept(dep1);
        Employee emp1 = new Employee();
        emp1.setName("John Doe");
        emp1.setManager(true);
        emp1.setDept(dep);
        kieSession.insert(dep);
        kieSession.insert(dep1);
        kieSession.insert(emp);
        kieSession.insert(emp1);
        kieSession.insert(emp2);
        kieSession.fireAllRules();
    }
}
