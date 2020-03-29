package com.poc;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.jdbc.ResultSetGenerator;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class RulesTest {

    public static void main(String[] args) {
        try {
            testResultSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testResultSet() throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/drools",
                "postgres", "postgres");
        Statement statement = conn.createStatement();
        String sql="SELECT id, min_age, max_age, status FROM age_rules";

        ResultSet rs = statement.executeQuery(sql);

        DroolsService droolsService = new DroolsService();
        final String drl = droolsService.convertTemplateToRules(rs);
        System.out.println(drl);
        statement.close();

        KieSession kSession = droolsService.getKieSession(drl);

        List<Person> personList = getPersons();
        droolsService.insertObjectsInSession(kSession, personList);

        kSession.fireAllRules();

        personList.forEach(person ->  System.out.println(person.getName() + "," + person.getStatus()));
    }

    private static List<Person> getPersons(){
       return Arrays.asList(
                new Person("A", 22),
                new Person("B", 52),
                new Person("C", 61),
                new Person("AA", 82),
                new Person("BB", 2),
                new Person("CC", 41)
        );
    }

}
