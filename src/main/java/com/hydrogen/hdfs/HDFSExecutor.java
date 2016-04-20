package com.hydrogen.hdfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;

import com.hydrogen.core.HydrogenEngine;

public class HDFSExecutor extends HydrogenEngine {

	public HDFSExecutor() {

	}

	@Override
	public void runProcess(Map<String, Object> map) {
		// Remove
		CommandLine rm = new CommandLine("hdfs");
		rm.addArgument("dfs");
		rm.addArgument("-rm");
		rm.addArgument("-R");
		rm.addArgument("-skipTrash");
		rm.addArgument("${file}");
		rm.setSubstitutionMap(map);

		// Remove
		CommandLine mkdir = new CommandLine("hdfs");
		mkdir.addArgument("dfs");
		mkdir.addArgument("-mkdir");
		mkdir.addArgument("${file}");
		mkdir.setSubstitutionMap(map);

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
		Executor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);

		try {
			executor.execute(mkdir, resultHandler);
			executor.execute(rm, resultHandler);
			
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

	public static void main(String[] args) {
		HDFSExecutor flumeExecutor = new HDFSExecutor();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("file", "/user/hydrogen");
		flumeExecutor.runProcess(map);

	}

}
