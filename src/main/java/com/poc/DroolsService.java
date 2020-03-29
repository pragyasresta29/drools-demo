package com.poc;

import lombok.SneakyThrows;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.jdbc.ResultSetGenerator;
import org.kie.api.KieServices;
import org.kie.api.internal.utils.KieService;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.List;

public class DroolsService {
    public KieSession getKieSession(String drl){
        KnowledgeBuilder kbuilder =
                KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newByteArrayResource(drl.getBytes()),
                ResourceType.DRL);
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kbuilder.getKnowledgePackages());
        return kbase.newKieSession();
    }

    public KieServices getKieServices() {
       return KieServices.Factory.get();
    }

    public InputStream getRulesStream() throws FileNotFoundException {
        return getClass().getResourceAsStream("AgeRules.drl");
    }

    @SneakyThrows
    public String convertTemplateToRules(ResultSet rs){
        ResultSetGenerator converter = new ResultSetGenerator();
        DroolsService ks = new DroolsService();
        return converter.compile(rs, getRulesStream());
    }

    public void insertObjectsInSession(final KieSession kieSession, List<Person> objectList){
        objectList.forEach(o -> kieSession.insert(o));
    }
}
