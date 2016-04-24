package com.hydrogen.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "hydrogen-build")
public class BuildManager extends AbstractMojo {

	public void execute() throws MojoExecutionException {
		getLog().info("Starting building hydrides...");
		File f = new File("src/main/resources");
		Collection<File> hydrides = FileUtils.listFiles(f, new WildcardFileFilter("*.hydrides.xml"), null);
		getLog().info("hydrides " + hydrides);
		for (File file : hydrides) {
			try {
				HydridesContext context = new HydridesContext(file.toURI().toURL());
				System.out.println(context.getHydrides());
				generate(context);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private void generate(HydridesContext context) {

		try {
			File dir = new File("target" + File.separator + context.getAppName());
			if (dir.exists())
				FileUtils.forceDelete(dir);

			FileUtils.forceMkdir(dir);

			context.setTargetDir(dir);
			context.setLog(getLog());
			HydrogenEngine engine = HydrogenEngine.getEngine(context);
			
			engine.build();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
