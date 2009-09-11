/*******************************************************************************
 * Copyright (c) 2009 Mountainminds GmbH & Co. KG and others
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
package org.jacoco.core.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ExecutionDataReader} and {@link ExecutionDataWriter}.
 * The tests don't care about the written binary format, they just verify
 * symmetry.
 * 
 * @author Marc R. Hoffmann
 * @version $Revision: $
 */
public class ExecutionDataReaderWriterTest {

	private PipedOutputStream pipe;

	private ExecutionDataWriter writer;

	private ExecutionDataReader reader;

	private ExecutionDataStore store;

	private Random random;

	@Before
	public void setup() throws IOException {
		pipe = new PipedOutputStream();
		writer = new ExecutionDataWriter(pipe);
		reader = new ExecutionDataReader(new PipedInputStream(pipe));
		store = new ExecutionDataStore();
		reader.setExecutionDataVisitor(store);
		random = new Random(5);
	}

	@Test
	public void testEmpty() throws IOException {
		pipe.close();
		reader.setExecutionDataVisitor(new IExecutionDataVisitor() {
			public void visitClassExecution(long id, boolean[][] blockdata) {
				fail("No data expected.");
			}
		});
		reader.read();
	}

	@Test
	public void testValidHeader() throws IOException {
		writer.writeHeader();
		pipe.close();
		reader.read();
	}

	@Test(expected = IOException.class)
	public void testInvalidHeaderVersion() throws IOException {
		pipe.write(ExecutionDataWriter.BLOCK_HEADER);
		char version = ExecutionDataWriter.FORMAT_VERSION - 1;
		pipe.write(version >> 8);
		pipe.write(version & 0xFF);
		pipe.close();
		reader.read();
	}

	@Test(expected = IOException.class)
	public void testUnknownBlock() throws IOException {
		pipe.write(0xff);
		pipe.close();
		reader.read();
	}

	@Test
	public void testMinClassId() throws IOException {
		boolean[][] blocks = createBlockdata(0, 0);
		writer.visitClassExecution(Long.MIN_VALUE, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(Long.MIN_VALUE));
	}

	@Test
	public void testMaxClassId() throws IOException {
		boolean[][] blocks = createBlockdata(0, 0);
		writer.visitClassExecution(Long.MAX_VALUE, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(Long.MAX_VALUE));
	}

	@Test
	public void testEmptyClass() throws IOException {
		boolean[][] blocks = createBlockdata(0, 0);
		writer.visitClassExecution(3, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(3));
	}

	@Test
	public void testEmptyMethods() throws IOException {
		boolean[][] blocks = createBlockdata(5, 0);
		writer.visitClassExecution(3, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(3));
	}

	@Test
	public void testOneClass() throws IOException {
		boolean[][] blocks = createBlockdata(5, 10);
		writer.visitClassExecution(3, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(3));
	}

	@Test
	public void testTwoClasses() throws IOException {
		boolean[][] blocks1 = createBlockdata(5, 15);
		boolean[][] blocks2 = createBlockdata(7, 12);
		writer.visitClassExecution(333, blocks1);
		writer.visitClassExecution(-45, blocks2);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks1, store.get(333));
		assertArrayEquals(blocks2, store.get(-45));
	}

	@Test
	public void testBigClass() throws IOException {
		boolean[][] blocks = createBlockdata(43, 40);
		writer.visitClassExecution(123, blocks);
		pipe.close();
		reader.read();
		assertArrayEquals(blocks, store.get(123));
	}

	@Test(expected = RuntimeException.class)
	public void testIOException() throws IOException {
		final boolean[] broken = new boolean[1];
		ExecutionDataWriter writer = new ExecutionDataWriter(
				new OutputStream() {
					@Override
					public void write(int b) throws IOException {
						if (broken[0]) {
							throw new IOException();
						}
					}
				});
		broken[0] = true;
		boolean[][] blocks = createBlockdata(1, 1);
		writer.visitClassExecution(3, blocks);
	}

	private boolean[][] createBlockdata(int methodCount, int maxBlockCount) {
		boolean[][] blocks = new boolean[methodCount][];
		for (int i = 0; i < blocks.length; i++) {
			boolean[] arr = new boolean[random.nextInt(maxBlockCount + 1)];
			for (int j = 0; j < arr.length; j++) {
				arr[j] = random.nextBoolean();
			}
			blocks[i] = arr;
		}
		return blocks;
	}

	private void assertArrayEquals(boolean[][] expected, boolean[][] actual) {
		assertEquals(expected.length, actual.length, 0.0);
		for (int i = 0; i < expected.length; i++) {
			boolean[] b1 = expected[i];
			boolean[] b2 = actual[i];
			assertEquals(b1.length, b2.length, 0.0);
			for (int j = 0; j < b1.length; j++) {
				assertTrue(b1[j] == b2[j]);
			}
		}
	}

}
