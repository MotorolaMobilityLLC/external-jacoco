/*******************************************************************************
 * Copyright (c) 2009, 2011 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.report;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.analysis.IPackageCoverage;
import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.internal.analysis.BundleCoverageImpl;
import org.jacoco.core.internal.analysis.ClassCoverageImpl;
import org.jacoco.core.internal.analysis.CounterImpl;
import org.jacoco.core.internal.analysis.MethodCoverageImpl;
import org.jacoco.core.internal.analysis.PackageCoverageImpl;
import org.jacoco.core.internal.analysis.SourceFileCoverageImpl;

/**
 * Creates a simple hierarchy of coverage nodes and feeds it into
 * {@link IReportVisitor} instances.
 */
public class ReportStructureTestDriver {

	private final List<SessionInfo> sessions = Collections.emptyList();

	private final Collection<ExecutionData> executionData = Collections
			.emptyList();

	public final ISourceFileLocator sourceFileLocator = new ISourceFileLocator() {

		public Reader getSourceFile(String packageName, String fileName)
				throws IOException {
			return null;
		}

		public int getTabWidth() {
			return 4;
		}
	};

	private final IMethodCoverage methodCoverage;

	private final IClassCoverage classCoverage;

	private final ISourceFileCoverage sourceFileCoverage;

	private final IPackageCoverage packageCoverage;

	private final BundleCoverageImpl bundleCoverage;

	public ReportStructureTestDriver() {
		methodCoverage = new MethodCoverageImpl("fooMethod", "()V", null) {
			{
				instructionCounter = CounterImpl.getInstance(2, 22);
				branchCounter = CounterImpl.getInstance(3, 33);
			}
		};

		final ClassCoverageImpl classCoverageImpl = new ClassCoverageImpl(
				"org/jacoco/example/FooClass", 1001, null, "java/lang/Object",
				new String[0]);
		classCoverageImpl.setSourceFileName("FooClass.java");
		classCoverageImpl.addMethod(methodCoverage);
		classCoverage = classCoverageImpl;

		final SourceFileCoverageImpl sourceFileCoverageImpl = new SourceFileCoverageImpl(
				"FooClass.java", "org/jacoco/example");
		sourceFileCoverageImpl.increment(classCoverage);
		sourceFileCoverage = sourceFileCoverageImpl;

		packageCoverage = new PackageCoverageImpl("org/jacoco/example",
				Collections.singleton(classCoverage),
				Collections.singleton(sourceFileCoverage));
		bundleCoverage = new BundleCoverageImpl("bundle",
				Collections.singleton(packageCoverage));
	}

	public void sendGroup(IReportVisitor reportVisitor) throws IOException {
		reportVisitor.visitInfo(sessions, executionData);
		final IReportGroupVisitor group = reportVisitor.visitGroup("group");
		sendBundle(group);
		reportVisitor.visitEnd();
	}

	public void sendGroup(IReportGroupVisitor groupVisitor) throws IOException {
		final IReportGroupVisitor group = groupVisitor.visitGroup("group");
		sendBundle(group);
	}

	public void sendBundle(IReportVisitor reportVisitor) throws IOException {
		reportVisitor.visitInfo(sessions, executionData);
		reportVisitor.visitBundle(bundleCoverage, sourceFileLocator);
		reportVisitor.visitEnd();
	}

	public void sendBundle(IReportGroupVisitor groupVisitor) throws IOException {
		groupVisitor.visitBundle(bundleCoverage, sourceFileLocator);
	}

}
