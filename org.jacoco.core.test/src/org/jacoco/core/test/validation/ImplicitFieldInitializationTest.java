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
package org.jacoco.core.test.validation;

import static org.jacoco.core.analysis.ILine.FULLY_COVERED;
import static org.jacoco.core.analysis.ILine.NO_CODE;

import org.jacoco.core.test.validation.targets.Target08;
import org.junit.Test;

/**
 * Test of a implicit field initialization.
 */
public class ImplicitFieldInitializationTest extends ValidationTestBase {

	public ImplicitFieldInitializationTest() {
		super(Target08.class);
	}

	@Override
	protected void run(final Class<?> targetClass) throws Exception {
		targetClass.newInstance();
	}

	@Test
	public void testCoverageResult() {

		assertLine("classdef", FULLY_COVERED);
		assertLine("field1", NO_CODE);
		assertLine("field2", FULLY_COVERED);
		assertLine("field3", NO_CODE);
		assertLine("field4", FULLY_COVERED);

	}

}
