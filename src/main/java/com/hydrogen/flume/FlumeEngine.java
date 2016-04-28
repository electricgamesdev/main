package com.hydrogen.flume;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.hydrogen.core.AbstractEngine;
import com.hydrogen.core.Engine;
import com.hydrogen.core.HydridesContext;
import com.hydrogen.core.HydridesContextException;
import com.hydrogen.core.HydrogenEngine;
import com.hydrogen.model.xml.Entity;
import com.hydrogen.model.xml.Hydrides;
import com.hydrogen.model.xml.Source;

public class FlumeEngine extends AbstractEngine {

	public void execute() {
		CommandLine cmdLine = new CommandLine("flume-ng");
		cmdLine.addArgument("agent");
		cmdLine.addArgument("-f");
		cmdLine.addArgument("${file}");
		cmdLine.addArgument("-n");
		cmdLine.addArgument("${name}");
		cmdLine.addArgument("-C");
		cmdLine.addArgument("${classpath}");
		// map.put("file", new File("invoice.pdf"));
		cmdLine.setSubstitutionMap(getConfigMap());

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
		Executor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);

		try {
			executor.execute(cmdLine, resultHandler);

			// some time later the result handler callback was invoked so we
			// can safely request the exit value

			resultHandler.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int exitValue = resultHandler.getExitValue();

	}

	public static void main(String[] args) throws IOException, HydridesContextException {
		FlumeEngine flumeExecutor = new FlumeEngine();
		flumeExecutor.build();
		flumeExecutor.execute();

	}

	public void build() {
		try {
			List<String> confList = IOUtils.readLines(FlumeEngine.class.getResourceAsStream("flume_conf.template"));
			HydridesContext context = getContext();
			Hydrides hydrides = context.getHydrides();
			List<Source> sources = hydrides.getSources();
			for (Source s : sources) {

				String conf = new String();
				String sid = context.getIdInPath(s.getPath());
				for (String c : confList) {
					conf = conf + c + "\n";
				}

				Source source = context.getSource(s.getPath());

				String entities = "";
				String patterns = "";

				for (Entity e : source.getEntities()) {

					String eid = context.getIdInPath(e.getPath());
					conf = conf + "agent.sources.source1.selector.mapping." + eid + " = channel1 \n";
					entities = entities + eid + ",";

					Entity entity = context.getEntity(e.getPath());

					patterns = patterns + entity.getPattern() + ",";

				}

				conf = conf + "agent.sources.source1.interceptors.i2.entities = " + entities + " \n";
				conf = conf + "agent.sources.source1.interceptors.i2.patterns = " + patterns + " \n";
				conf = conf.replaceAll("agent", sid);
				String hdfsdatadir = source.getHdfs_path();
				String hdfserrordir = source.getHdfs_path();

				if (s.getLink() != null)
					conf = conf.replaceAll("source-dir", s.getLink());
				if (hdfsdatadir != null)
					conf = conf.replaceAll("hdfs-dir", hdfsdatadir);
				if (hdfserrordir != null)
					conf = conf.replaceAll("hdfs-error-dir", hdfserrordir);
				conf = conf.replaceAll("sourcefilter", source.getGroup_filter());

				// clean up - fresh build
				File sourceDir = new File("");
				if(s.getLink()!=null)
				sourceDir = new File(s.getLink());
				File data1 = new File(sourceDir.getParent() + File.separator + sid + "_data1");
				File data2 = new File(sourceDir.getParent() + File.separator + sid + "_data2");
				File cp1 = new File(sourceDir.getParent() + File.separator + sid + "_cp1");
				File cp2 = new File(sourceDir.getParent() + File.separator + sid + "_cp2");
				/*
				if (sourceDir.exists())
					FileUtils.forceDelete(sourceDir);
				if (data1.exists())
					FileUtils.forceDelete(data1);
				if (data2.exists())
					FileUtils.forceDelete(data2);
				if (cp1.exists())
					FileUtils.forceDelete(cp1);
				if (cp2.exists())
					FileUtils.forceDelete(cp2);

				FileUtils.forceMkdir(sourceDir);
				FileUtils.forceMkdir(data1);
				FileUtils.forceMkdir(data2);
				FileUtils.forceMkdir(cp1);
				FileUtils.forceMkdir(cp2);
*/
				conf = conf.replaceAll("data1", data1.getAbsolutePath());
				conf = conf.replaceAll("data2", data2.getAbsolutePath());
				conf = conf.replaceAll("checkpoint1", cp1.getAbsolutePath());
				conf = conf.replaceAll("checkpoint2", cp2.getAbsolutePath());

				addConfig("file", getContext().getHomeDir() + File.separator + sid + ".flume");
				writeCode(sid + ".flume", conf);

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
