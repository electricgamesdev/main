package com.hydrogen.drools;

import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

public class RuleTest {

	public static void main(String[] args) {
		sheets();
	}

	public static void sheets() {
		DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtableconfiguration.setInputType(DecisionTableInputType.XLS);

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		knowledgeBuilder.add(ResourceFactory.newClassPathResource("integrity.xls"), ResourceType.DTABLE, dtableconfiguration);

		if (knowledgeBuilder.hasErrors()) {
			throw new RuntimeException(knowledgeBuilder.getErrors().toString());
		}

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

		StatelessKieSession kSession = knowledgeBase.newStatelessKnowledgeSession();
		Record r = new Record();
		r.put("age", new Integer(20));

		kSession.execute(r);
		System.out.println(" record " + r);
		
	}

	public static void rules() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.getKieClasspathContainer();
		StatelessKieSession kSession = kContainer.newStatelessKieSession();

		Record r = new Record();
		r.put("age", new Integer(20));

		kSession.execute(r);
		System.out.println(" record " + r);

	}

}
