package com.poc.salienceexample;

import com.poc.rulestemplate.Person;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.SneakyThrows;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.jdbc.ResultSetGenerator;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.util.List;

public class DroolsService {

    @SneakyThrows
    public KieSession getKieSession(){
        KnowledgeBuilder kbuilder =
                KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newUrlResource(getRulesStream()),
                ResourceType.DRL);
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kbuilder.getKnowledgePackages());
        return kbase.newKieSession();
    }

    public KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    public URL getRulesStream() throws FileNotFoundException {
        return getClass().getResource("ITManager.drl");
    }

    public void insertObjectsInSession(final KieSession kieSession, List<Person> objectList){
        objectList.forEach(o -> kieSession.insert(o));
    }
}
