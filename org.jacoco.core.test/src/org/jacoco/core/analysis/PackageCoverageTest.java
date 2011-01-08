/*******************************************************************************
 * Copyright (c) 2009, 2010 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.core.analysis;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

/**
 * Unit test for {@link PackageCoverage}.
 * 
 * @author Marc R. Hoffmann
 * @version $qualified.bundle.version$
 */
public class PackageCoverageTest {

	@Test
	public void testProperties() {
		Collection<ClassCoverage> classes = Collections
				.singleton(new ClassCoverage("org/jacoco/test/Sample", 0, null,
						"java/lang/Object", new String[0]));
		Collection<SourceFileCoverage> sourceFiles = Collections
				.singleton(new SourceFileCoverage("Sample.java",
						"org/jacoco/test/Sample"));
		PackageCoverage data = new PackageCoverage("org/jacoco/test", classes,
				sourceFiles);
		assertEquals(ICoverageNode.ElementType.PACKAGE, data.getElementType());
		assertEquals("org/jacoco/test", data.getName());
		assertEquals(classes, data.getClasses());
		assertEquals(sourceFiles, data.getSourceFiles());
	}

	@Test
	public void testCountersWithSources() {
		// Classes with source reference will not considered for counters:
		final ClassCoverage classnode = new ClassCoverage(
				"org/jacoco/test/Sample", 0, null, "java/lang/Object",
				new String[0]) {
			{
				classCounter = CounterImpl.getInstance(9, 0);
				methodCounter = CounterImpl.getInstance(9, 0);
				branchCounter = CounterImpl.getInstance(9, 0);
				instructionCounter = CounterImpl.getInstance(9, 0);
			}
		};
		classnode.setSourceFileName("Sample.java");
		// Only source files will be considered for counters:
		final SourceFileCoverage sourceFile = new SourceFileCoverage(
				"Sample.java", "org/jacoco/test/Sample") {
			{
				classCounter = CounterImpl.getInstance(1, 0);
				methodCounter = CounterImpl.getInstance(2, 0);
				branchCounter = CounterImpl.getInstance(3, 0);
				instructionCounter = CounterImpl.getInstance(4, 0);
			}
		};
		PackageCoverage data = new PackageCoverage("org/jacoco/test",
				Collections.singleton(classnode),
				Collections.singleton(sourceFile));
		assertEquals(CounterImpl.getInstance(1, 0), data.getClassCounter());
		assertEquals(CounterImpl.getInstance(2, 0), data.getMethodCounter());
		assertEquals(CounterImpl.getInstance(3, 0), data.getBranchCounter());
		assertEquals(CounterImpl.getInstance(4, 0),
				data.getInstructionCounter());
	}

	@Test
	public void testCountersWithoutSources() {
		// Classes without source reference will be considered for counters:
		final ClassCoverage classnode = new ClassCoverage(
				"org/jacoco/test/Sample", 0, null, "java/lang/Object",
				new String[0]) {
			{
				classCounter = CounterImpl.getInstance(1, 0);
				methodCounter = CounterImpl.getInstance(2, 0);
				branchCounter = CounterImpl.getInstance(3, 0);
				instructionCounter = CounterImpl.getInstance(4, 0);
			}
		};
		final Collection<SourceFileCoverage> sourceFiles = Collections
				.emptySet();
		PackageCoverage data = new PackageCoverage("org/jacoco/test",
				Collections.singleton(classnode), sourceFiles);
		assertEquals(CounterImpl.getInstance(1, 0), data.getClassCounter());
		assertEquals(CounterImpl.getInstance(2, 0), data.getMethodCounter());
		assertEquals(CounterImpl.getInstance(3, 0), data.getBranchCounter());
		assertEquals(CounterImpl.getInstance(4, 0),
				data.getInstructionCounter());
		assertEquals(CounterImpl.getInstance(0, 0), data.getLineCounter());
	}

}
