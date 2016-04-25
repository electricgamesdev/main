package com.hydrogen.test;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.hydrogen.core.BuildManager;

public class GenerateTest extends AbstractMojoTestCase {
	/** {@inheritDoc} */
	protected void setUp() throws Exception {
		// required
		super.setUp();

	}

	/** {@inheritDoc} */
	protected void tearDown() throws Exception {
		// required
		super.tearDown();

	}

	/**
	 * @throws Exception
	 *             if any
	 */
	public void testGenerate() throws Exception {
		File pom = getTestFile("src/test/resources/generate/pom.xml");
		assertNotNull(pom);
		assertTrue(pom.exists());

		BuildManager myMojo = (BuildManager) lookupMojo("generate-sources", pom);
		assertNotNull(myMojo);
		myMojo.execute();

	}
}