package com.hydrogen.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.hydrogen.model.Action;
import com.hydrogen.model.Component;
import com.hydrogen.model.Entity;
import com.hydrogen.model.Event;
import com.hydrogen.model.Field;
import com.hydrogen.model.Form;
import com.hydrogen.model.Hydrides;
import com.hydrogen.model.Param;
import com.hydrogen.model.Rules;
import com.hydrogen.model.Script;
import com.hydrogen.model.Source;
import com.hydrogen.model.Task;
import com.hydrogen.model.Workflow;
//mysql password Star_4321

public class HydridesContext {

	private Log log = LogFactory.getLog(HydridesContext.class);
	private URL xml = null;
	private Hydrides hydrides = null;
	private String appPath = null;

	public HydridesContext(URL xml) {
		this.xml = xml;
		appPath = xml.toString().substring(0, xml.toString().lastIndexOf(File.separator));
		System.out.println("App path " + appPath);
	}

	public Hydrides getHydrides() {
		try {

			if (hydrides == null) {
				Digester digester = new Digester();
				digester.setValidating(false);
				digester.addObjectCreate("hydrides", "com.hydrogen.model.Hydrides");
				digester.addSetProperties("hydrides");

				// sources
				digester.addObjectCreate("hydrides/domain/source", "com.hydrogen.model.Source");
				digester.addSetProperties("hydrides/domain/source");
				digester.addSetNext("hydrides/domain/source", "addSource", "com.hydrogen.model.Hydrides");

				digester.addObjectCreate("hydrides/domain/source/workflow", "com.hydrogen.model.Workflow");
				digester.addSetProperties("hydrides/domain/source/workflow");
				digester.addSetNext("hydrides/domain/source/workflow", "addWorkflow", "com.hydrogen.model.Source");

				// workflow
				digester.addObjectCreate("hydrides/pipeline/workflow", "com.hydrogen.model.Workflow");
				digester.addSetProperties("hydrides/pipeline/workflow");
				digester.addSetNext("hydrides/pipeline/workflow", "addWorkflow", "com.hydrogen.model.Hydrides");

				// components
				digester.addObjectCreate("hydrides/layout/component", "com.hydrogen.model.Component");
				digester.addSetProperties("hydrides/layout/component");
				digester.addSetNext("hydrides/layout/component", "addComponent", "com.hydrogen.model.Hydrides");

				hydrides = digester.parse(xml);

				appPath = appPath + File.separator + hydrides.getPath() + File.separator;

				log.info(hydrides);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hydrides;
	}

	public Source getSource(String path) throws HydridesContextException {
		if (pool.get(path) == null) {
			Digester digester = getDigester(Source.class);
			map(digester, "source/entity", Entity.class, Source.class);
			add(digester, path, Source.class);

		}
		return (Source) pool.get(path);
	}

	private Map<String, Object> pool = new HashMap<String, Object>();

	private void add(Digester digester, String path, Class cls) throws HydridesContextException {
		try {

			pool.put(path,
					digester.parse(appPath + File.separator + path + "." + cls.getSimpleName().toLowerCase() + ".xml"));
		} catch (Exception e) {

			throw new HydridesContextException(e.getMessage(), e);

		}
	}

	private Digester getDigester(Class cls) {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate(cls.getSimpleName().toLowerCase(), cls.getName());
		digester.addSetProperties(cls.getSimpleName().toLowerCase());
		return digester;
	}

	private void map(Digester digester, String path, Class cls, Class parent) {

		digester.addObjectCreate(path, cls.getName());
		digester.addSetProperties(path);
		digester.addSetNext(path, "add" + cls.getSimpleName(), parent.getName());

	}

	public Component getComponent(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Component.class);
			map(digester, "component/form", Form.class, Component.class);
			map(digester, "component/form/event", Event.class, Form.class);
			map(digester, "component/form/event/param", Param.class, Event.class);

			add(digester, path, Component.class);

		}
		return (Component) pool.get(path);
	}

	public Workflow getWorkflow(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Workflow.class);
			map(digester, "workflow/action", Action.class, Workflow.class);
			map(digester, "workflow/action/entity", Entity.class, Action.class);
			map(digester, "workflow/action/entity", Form.class, Action.class);
			map(digester, "workflow/action/rules", Rules.class, Action.class);

			add(digester, path, Workflow.class);

		}
		return (Workflow) pool.get(path);
	}

	public Entity getEntity(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Entity.class);
			map(digester, "entity/field", Field.class, Entity.class);
			add(digester, path, Entity.class);

		}
		return (Entity) pool.get(path);
	}

	public Form getForm(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Form.class);
			map(digester, "form/filter/entity", Entity.class, Form.class);
			map(digester, "form/script", Script.class, Form.class);
			add(digester, path, Entity.class);

		}
		return (Form) pool.get(path);
	}

	public Action getAction(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Action.class);
			map(digester, "action/task", Task.class, Action.class);
			add(digester, path, Action.class);

		}
		return (Action) pool.get(path);
	}

	public Rules getRules(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Rules.class);
			map(digester, "rules/task", Rules.class, Rules.class);
			add(digester, path, Rules.class);

		}
		return (Rules) pool.get(path);
	}

	public static void main(String[] args) throws MalformedURLException, HydridesContextException {
		URL xml = new File("/home/wafiq/workspace/hydrogen/src/main/resources/bank_risk_rating.hydrides.xml").toURL();
		HydridesContext context = new HydridesContext(xml);
		System.out.println(context.getHydrides());
		Hydrides h = context.getHydrides();

		System.out.println("Stage 1 Validation: Anlyzing Hydrides and Its dependecies");
		for (Source s : h.getSources()) {
			Source s1 = context.getSource(s.getPath());
			System.out.println(s1);
		}

		for (Component s : h.getComponents()) {
			Component s1 = context.getComponent(s.getPath());
			System.out.println(s1);
		}

		for (Workflow s : h.getWorkflows()) {
			Workflow s1 = context.getWorkflow(s.getPath());
			System.out.println(s1);
		}

		System.out.println("Stage 2 Validation: Analyzing Middle level components and Its dependecies");
		for (Source s : h.getSources()) {
			Source s1 = context.getSource(s.getPath());
			for (Entity e : s1.getEntities()) {
				Entity e1 = context.getEntity(e.getPath());
			}
		}

		for (Component s : h.getComponents()) {
			Component s1 = context.getComponent(s.getPath());
			for (Form f : s1.getForms()) {
				Form e1 = context.getForm(f.getPath());
			}
		}

		for (Workflow s : h.getWorkflows()) {
			Workflow s1 = context.getWorkflow(s.getPath());
			for (Action f : s1.getActions()) {
				Action e1 = context.getAction(f.getPath());
			}
		}
		

	}

	public String getIdInPath(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
		
	}

	public String getHomeDir() {
		return System.getenv("user.home");
	}

	public File getFileToCreate(String filename) {
		File file=new File(getHomeDir()+File.separator+filename);
		return file;
	}

	public String getEnv(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
