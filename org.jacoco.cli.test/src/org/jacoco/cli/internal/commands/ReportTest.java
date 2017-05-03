/*******************************************************************************
 * Copyright (c) 2009, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.cli.internal.commands;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;

import org.jacoco.cli.internal.CommandTestBase;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for {@link Report}.
 */
public class ReportTest extends CommandTestBase {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Test
	public void should_print_usage_when_no_options_are_given()
			throws Exception {
		execute("report");

		assertFailure();
		assertContains("Option \"-classfiles\" is required", err);
		assertContains(
				"Usage: java -jar jacococli.jar report [<execfiles> ...]", err);
	}

	@Test
	public void should_print_number_of_analyzed_classes() throws Exception {
		execute("report", "-classfiles", getClassPath());

		assertOk();
		assertContains("[INFO] Writing report with 12 classes.", out);
	}

	@Test
	public void should_print_warning_when_exec_data_does_not_match()
			throws Exception {
		File exec = new File(tmp.getRoot(), "jacoco.exec");
		final FileOutputStream execout = new FileOutputStream(exec);
		ExecutionDataWriter writer = new ExecutionDataWriter(execout);
		// Add probably invalid id for this test class:
		writer.visitClassExecution(
				new ExecutionData(0x123, getClass().getName().replace('.', '/'),
						new boolean[] { true }));
		execout.close();

		execute("report", exec.getAbsolutePath(), "-classfiles",
				getClassPath());

		assertOk();
		assertContains("[WARN] Some classes do not match with execution data.",
				out);
		assertContains(
				"[WARN] For report generation the same class files must be used as at runtime.",
				out);
		assertContains(
				"[WARN] Execution data for class org/jacoco/cli/internal/commands/ReportTest does not match.",
				out);
	}

	@Test
	public void should_create_xml_report_when_xml_option_is_provided()
			throws Exception {
		File xml = new File(tmp.getRoot(), "coverage.xml");

		execute("report", "-classfiles", getClassPath(), "-xml",
				xml.getAbsolutePath());

		assertOk();
		assertTrue(xml.isFile());
	}

	@Test
	public void should_create_csv_report_when_csv_option_is_provided()
			throws Exception {
		File csv = new File(tmp.getRoot(), "coverage.csv");

		execute("report", "-classfiles", getClassPath(), "-csv",
				csv.getAbsolutePath());

		assertOk();
		assertTrue(csv.isFile());
	}

	@Test
	public void should_create_html_report_when_html_option_is_provided()
			throws Exception {
		File html = new File(tmp.getRoot(), "coverage");

		execute("report", "-classfiles", getClassPath(), "-sourcefiles",
				"./src", "-html", html.getAbsolutePath());

		assertOk();
		assertTrue(html.isDirectory());
		assertTrue(new File(html,
				"org.jacoco.cli.internal.commands/ReportTest.html").isFile());
		assertTrue(new File(html,
				"org.jacoco.cli.internal.commands/ReportTest.java.html")
						.isFile());
	}

}
