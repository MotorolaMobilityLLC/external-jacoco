/*******************************************************************************
 * Copyright (c) 2009, 2010 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak -initial API and implementation
 *    
 * $Id: $
 *******************************************************************************/
package org.jacoco.report.xml;

import static java.lang.String.format;

import java.io.IOException;

import org.jacoco.core.analysis.ClassCoverage;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.ICoverageNode.ElementType;
import org.jacoco.report.IReportVisitor;

/**
 * Wrapper for an {@link XMLElement} that contains package coverage data
 * 
 * @author Brock Janiczak
 * @version $Revision: $
 */
public class PackageNode extends NodeWithCoverage {

	/**
	 * Creates a new Package coverage element under the supplied group element
	 * 
	 * @param parent
	 *            Parent element that will own this class element
	 * @param packageNode
	 *            Package coverage node
	 * @throws IOException
	 *             IO Error creating the element
	 */
	public PackageNode(final GroupNode parent, final ICoverageNode packageNode)
			throws IOException {
		super(parent, "package", packageNode);
	}

	public IReportVisitor visitChild(final ICoverageNode node)
			throws IOException {
		final ElementType type = node.getElementType();
		switch (type) {
		case CLASS:
			return new ClassNode(this, (ClassCoverage) node);
		case SOURCEFILE:
			return IReportVisitor.NOP;
		}
		throw new IllegalStateException(format("Unexpected child node %s.",
				type));
	}
}
