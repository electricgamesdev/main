package com.hydrogen.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.digester3.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.hydrogen.model.xml.Action;
import com.hydrogen.model.xml.Entity;
import com.hydrogen.model.xml.Event;
import com.hydrogen.model.xml.Field;
import com.hydrogen.model.xml.Form;
import com.hydrogen.model.xml.Function;
import com.hydrogen.model.xml.Hydrides;
import com.hydrogen.model.xml.Model;
import com.hydrogen.model.xml.Param;
import com.hydrogen.model.xml.Rules;
import com.hydrogen.model.xml.Script;
import com.hydrogen.model.xml.Source;
import com.hydrogen.model.xml.Task;
import com.hydrogen.model.xml.Template;
import com.hydrogen.model.xml.Workflow;

public class HydridesContext {

	private Log log = LogFactory.getLog(HydridesContext.class);
	private URL xml = null;
	private Hydrides hydrides = null;
	private String appPath = null;
	private String appName = null;

	public HydridesContext(URL xml) {
		this.xml = xml;
		appPath = xml.toString().substring(0, xml.toString().lastIndexOf(File.separator));
		appName = xml.toString().substring(xml.toString().lastIndexOf(File.separator) + 1, xml.toString().indexOf("."));
		System.out.println("App path " + appPath);
	}

	public String getAppName() {
		return appName;
	}

	public Hydrides getHydrides() {
		try {

			if (hydrides == null) {
				Digester digester = new Digester();
				digester.setValidating(false);
				digester.addObjectCreate("hydrides", "com.hydrogen.model.Hydrides");
				digester.addSetProperties("hydrides","path","path");
				digester.addSetProperties("hydrides/domain", "path", "domainPath");
				digester.addSetProperties("hydrides/prototype", "path", "prototypePath");
				digester.addSetProperties("hydrides/pipeline", "path", "pipelinePath");
				digester.addSetProperties("hydrides/domain", "type", "domainType");
				digester.addSetProperties("hydrides/prototype", "type", "prototypeType");
				digester.addSetProperties("hydrides/pipeline", "type", "pipelineType");

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
				digester.addObjectCreate("hydrides/prototype/template", "com.hydrogen.model.Template");
				digester.addSetProperties("hydrides/prototype/template");
				digester.addSetNext("hydrides/prototype/template", "addTemplate", "com.hydrogen.model.Hydrides");
				
				digester.addObjectCreate("hydrides/prototype/template/workflow", "com.hydrogen.model.Workflow");
				digester.addSetProperties("hydrides/prototype/template/workflow");
				digester.addSetNext("hydrides/prototype/template/workflow", "addWorkflow", "com.hydrogen.model.Template");
				
				hydrides = digester.parse(xml);

				File domp = new File(appPath + File.separator + hydrides.getDomainPath() + File.separator);

				File prop = new File(appPath + File.separator + hydrides.getPrototypePath() + File.separator);

				File pipp = new File(appPath + File.separator + hydrides.getPipelinePath() + File.separator);

				
				appPath = appPath + File.separator +  hydrides.getPath()  + File.separator;

				
				domainProperties
						.load(new FileInputStream(domp.getPath() +File.separator+ hydrides.getDomainType() + ".properties"));
				pipelineProperties.load(
						new FileInputStream(pipp.getPath()+File.separator + hydrides.getPrototypeType() + ".properties"));
				prototypeProperties
						.load(new FileInputStream(prop.getPath()+File.separator + hydrides.getPipelineType() + ".properties"));

				for (Source s : hydrides.getSources()) {
					addComp(s, domp);
				}
				for (Template s : hydrides.getTemplates()) {
					addComp(s, prop);
				}
				for (Workflow s : hydrides.getWorkflows()) {
					addComp(s, pipp);
				}
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

	Properties domainProperties = new Properties();
	Properties prototypeProperties = new Properties();
	Properties pipelineProperties = new Properties();

	Map<String, File> components = new HashMap<String, File>();

	private void addComp(Model s, File f) {
		if (s.getComponent() != null) {

			String c[] = s.getComponent().split(",");
			for (int i = 0; i < c.length; i++) {
				if (!components.containsKey(c)) {
					File a = new File(f.getAbsolutePath() + c);
					if (a.exists()) {
						components.put(c[i], a);
					}else{
						System.err.println(a+" component is missing");
					}
				}
			}
		}
	}

	public Properties getPipelineProperties() {
		return pipelineProperties;
	}

	public Properties getDomainProperties() {
		return domainProperties;
	}

	public Properties getPrototypeProperties() {
		return prototypeProperties;
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

	private void map(Digester digester, String path, Class cls, Class parent, String var) {

		digester.addObjectCreate(path, cls.getName());
		digester.addSetProperties(path);
		digester.addSetNext(path, "add" + var, parent.getName());

	}

	public Template getTemplate(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Template.class);
			map(digester, "template/form", Form.class, Template.class);
			map(digester, "template/form/event", Event.class, Form.class);
			map(digester, "template/form/event/param", Param.class, Event.class);
			map(digester, "template/dimension", Action.class, Template.class);
			add(digester, path, Template.class);

		}
		return (Template) pool.get(path);
	}

	public Workflow getWorkflow(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Workflow.class);
			map(digester, "workflow/action", Action.class, Workflow.class);
			map(digester, "workflow/action/entity", Entity.class, Action.class);
			map(digester, "workflow/action/rules", Rules.class, Action.class);
			map(digester, "workflow/action/rules/event", Event.class, Action.class);
			map(digester, "workflow/action/rules/event/action", Action.class, Event.class);
			map(digester, "workflow/action/rules/event/action/entity", Entity.class, Action.class);
			map(digester, "workflow/action/rules/event/action/rules", Rules.class, Action.class);
			map(digester, "workflow/action/rules/event/action/workflow", Workflow.class, Action.class);
			add(digester, path, Workflow.class);

		}
		return (Workflow) pool.get(path);
	}

	public Entity getEntity(String path) throws HydridesContextException {
		if (pool.get(path) == null) {

			Digester digester = getDigester(Entity.class);
			map(digester, "entity/field", Field.class, Entity.class);
			map(digester, "entity/field/field", Field.class, Field.class);
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
			map(digester, "action/execute/entity", Entity.class, Task.class, "Execute");
			map(digester, "action/execute/entity/function", Function.class, Entity.class);
			map(digester, "action/execute/entity", Entity.class, Task.class, "Result");
			map(digester, "action/execute/form", Form.class, Task.class, "Result");
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

		System.out.println(context.getAppName());
		Hydrides h = context.getHydrides();

		System.out.println("Stage 1 Validation: Anlyzing Hydrides and Its dependecies " + h);
		for (Source s : h.getSources()) {
			Source s1 = context.getSource(s.getPath());
			System.out.println(s1);
		}

		for (Template s : h.getTemplates()) {
			Template s1 = context.getTemplate(s.getPath());
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

		for (Template s : h.getTemplates()) {
			Template s1 = context.getTemplate(s.getPath());
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
		return targetDir.getAbsolutePath();
	}

	public File getFileToCreate(String filename) {
		File file = new File(getHomeDir() + File.separator + filename);
		return file;
	}

	public String getEnv(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private File targetDir = null;

	public void setTargetDir(File dir) {
		this.targetDir = dir;
	}

	public File getTargetDir() {
		return targetDir;
	}

	org.apache.maven.plugin.logging.Log log2 = null;

	public void setLog(org.apache.maven.plugin.logging.Log log2) {
		this.log2 = log2;
	}

	public void log(String s) {
		this.log2.info(s);
	}

}
