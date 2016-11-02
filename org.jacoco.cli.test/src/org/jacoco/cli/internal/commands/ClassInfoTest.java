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

import org.jacoco.cli.internal.CommandTestBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for {@link ExecInfo}.
 */
public class ClassInfoTest extends CommandTestBase {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Test
	public void shouldPrintUsage_whenInvalidArgumentIsGiven() throws Exception {
		execute("classinfo", "-invalid");

		assertFailure();
		assertContains("\"-invalid\" is not a valid option", err);
		assertContains(
				"java -jar jacococli.jar classinfo [<classlocations> ...]",
				err);
	}

	@Test
	public void shouldProvideClassInfoInfo() throws Exception {
		execute("classinfo", getClassPath());

		assertOk();
		assertContains(
				"class name:   org/jacoco/cli/internal/commands/ClassInfoTest",
				out);
		assertContains("methods:      3", out);
	}

}
