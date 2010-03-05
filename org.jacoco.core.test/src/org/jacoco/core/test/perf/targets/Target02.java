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
 * $Id: $
 *******************************************************************************/
package org.jacoco.core.test.perf.targets;

/**
 * Simple Loop.
 * 
 * @author Marc R. Hoffmann
 * @version $Revision: $
 */
public class Target02 implements Runnable {

	public void run() {
		int count = 0;
		for (int i = 0; i < 10000000; i++) {
			count++;
		}
	}

}
