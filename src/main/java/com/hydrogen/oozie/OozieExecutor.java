package com.hydrogen.oozie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;

import com.hydrogen.core.HydridesContext;
import com.hydrogen.core.HydridesContextException;
import com.hydrogen.core.HydrogenEngine;
import com.hydrogen.model.Entity;
import com.hydrogen.model.Source;

public class OozieExecutor extends HydrogenEngine {

	public OozieExecutor() {

	}

	@Override
	public void runProcess() {

	
	}

	public static void main(String[] args) {
		OozieExecutor flumeExecutor = new OozieExecutor();
		flumeExecutor.runProcess();

	}

	@Override
	public void generateCode() throws IOException, HydridesContextException {
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

				
				for (Entity e : source.getEntities()) {
					
					String id = context.getIdInPath(e.getPath());

					String s = IOUtils.toString(OozieExecutor.class.getResourceAsStream("ds.xml"));
					s = StringUtils.replace(s, "#1", id);
					ds.append(StringUtils.replace(s, "#2", id));

					String i = IOUtils.toString(OozieExecutor.class.getResourceAsStream("ie.xml"));
					i = StringUtils.replace(i, "#1", id + "IN");
					ie.append(StringUtils.replace(i, "#2", id));

					fs.append(StringUtils.replace(i, "#1", id));
					mv = StringUtils.replace(mv, "#1", id);
				}

				Properties srcProp = new Properties();
				srcProp.load(OozieExecutor.class.getResourceAsStream("co.properties"));
				srcProp.setProperty("oozie.coord.application.path",
						"${nameNode}/user/hydrogen/workflow/" + source + "_coordinator.xml");
				srcProp.setProperty("workflowPath", "${nameNode}/user/hydrogen/workflow/" + source + "_workflow.xml");

				StringBuilder data = new StringBuilder();
				for (Object key : srcProp.keySet()) {
					data.append(key + "=" + srcProp.getProperty(key.toString()) + "\n");
				}

			

				writeCode(source + ".properties", data.toString());
				String co = IOUtils.toString(OozieExecutor.class.getResourceAsStream("co.xml"));
				co = StringUtils.replace(co, "#1", ds.toString());
				co = StringUtils.replace(co, "#2", ie.toString());
				co = StringUtils.replace(co, "coordinator1", source + "_coordinator");
				System.out.println("co=" + co);
				String fc = IOUtils.toString(OozieExecutor.class.getResourceAsStream("fc.xml"));
				fc = StringUtils.replace(fc, "#1", "dpf");
				fc = StringUtils.replace(fc, "#2", mv);
				fc = StringUtils.replace(fc, "hydridesdpf", source + "_workflow");
				fc = StringUtils.replace(fc, "mvdpf", source + "_move");

				writeCode(source + "_coordinator.xml", co);
				writeCode(source + "_workflow.xml", fc);

				System.out.println("oozie script completed ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
