package com.hydrogen.oozie;

import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.hydrogen.core.AbstractEngine;
import com.hydrogen.core.HydridesContext;
import com.hydrogen.model.xml.Entity;
import com.hydrogen.model.xml.Source;

public class OozieEngine extends AbstractEngine {

	

	
	public void execute() {

	
	}

	public static void main(String[] args) {
		OozieEngine flumeExecutor = new OozieEngine();
		flumeExecutor.execute();

	}

	public void build() {
		HydridesContext context = getContext();
		
		Source source = null;
		try {
			StringBuilder ds = new StringBuilder();
			StringBuilder ie = new StringBuilder();
			StringBuilder fs = new StringBuilder();

			for (Source src : context.getHydrides().getSources()) {
				source = context.getSource(src.getPath());

				String mv = "<move source=\"${nameNode}" + source.getHdfs_path()
						+ "/${#1}'\" target=\"${nameNode}" + source.getHdfs_path() + "/${wf:id()}/${#1}\"/>";
				String srcid=context.getIdInPath(src.getPath());
				
				for (Entity e : source.getEntities()) {
					
					String id = context.getIdInPath(e.getPath());

					String s = IOUtils.toString(OozieEngine.class.getResourceAsStream("ds.xml"));
					s = StringUtils.replace(s, "#1", id);
					ds.append(StringUtils.replace(s, "#2", id));

					String i = IOUtils.toString(OozieEngine.class.getResourceAsStream("ie.xml"));
					i = StringUtils.replace(i, "#1", id + "IN");
					ie.append(StringUtils.replace(i, "#2", id));

					fs.append(StringUtils.replace(i, "#1", id));
					mv = StringUtils.replace(mv, "#1", id);
				}

				Properties srcProp = new Properties();
				srcProp.load(OozieEngine.class.getResourceAsStream("co.properties"));
				srcProp.setProperty("oozie.coord.application.path",
						"${nameNode}/user/hydrogen/workflow/" + source + "_coordinator.xml");
				srcProp.setProperty("workflowPath", "${nameNode}/user/hydrogen/workflow/" + source + "_workflow.xml");

				StringBuilder data = new StringBuilder();
				for (Object key : srcProp.keySet()) {
					data.append(key + "=" + srcProp.getProperty(key.toString()) + "\n");
				}

			

				writeCode(srcid + ".properties", data.toString());
				String co = IOUtils.toString(OozieEngine.class.getResourceAsStream("co.xml"));
				co = StringUtils.replace(co, "#1", ds.toString());
				co = StringUtils.replace(co, "#2", ie.toString());
				co = StringUtils.replace(co, "coordinator1", srcid + "_coordinator");
				System.out.println("co=" + co);
				String fc = IOUtils.toString(OozieEngine.class.getResourceAsStream("fc.xml"));
				fc = StringUtils.replace(fc, "#1", "dpf");
				fc = StringUtils.replace(fc, "#2", mv);
				fc = StringUtils.replace(fc, "hydridesdpf", srcid + "_workflow");
				fc = StringUtils.replace(fc, "mvdpf", srcid + "_move");

				writeCode(srcid + "_coordinator.xml", co);
				writeCode(srcid + "_workflow.xml", fc);

				System.out.println("oozie script completed ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
