/*******************************************************************************
 * Copyright (c) 2009, 2010 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak - initial API and implementation
 *    
 * $Id: $
 *******************************************************************************/
package org.jacoco.report.csv;

import java.io.IOException;

/**
 * Column in a CSV report
 * 
 * @author Brock Janiczak
 * @version $Revision: $
 */
public interface ICsvColumn {

	/**
	 * Writes the contents of the column
	 * 
	 * @param writer
	 *            Writer to write column data though
	 * @throws IOException
	 *             Thrown if there is any error writing the column data
	 */
	public void writeContents(final DelimitedWriter writer) throws IOException;
}
