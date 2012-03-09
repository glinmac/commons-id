/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.id.test;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * {@link Assert} class for IO operations.
 * <p>
 * Note: This class is intented to be moved to a commons-test component later.
 * </p>
 * 
 * @author J&ouml;rg Schaible
 * @since 1.0
 */
public class AssertIO {

    /**
     * Assert two {@linkplain File files} to have equal content.
     * 
     * @param expected reference file
     * @param current file to compare
     * @since 1.0
     */
    public static void assertFileEquals(final File expected, final File current) {
        assertFileEquals(null, expected, current);
    }

    /**
     * Assert two {@linkplain File files} to have equal content.
     * 
     * @param message the error message
     * @param expected reference file
     * @param current file to compare
     * @since 1.0
     */
    public static void assertFileEquals(
            final String message, final File expected, final File current) {
        try {
            assertInputStreamEquals(
                    new BufferedInputStream(new FileInputStream(expected)),
                    new BufferedInputStream(new FileInputStream(current)));
        } catch (final FileNotFoundException e) {
            Assert.fail((message != null ? message + ": " : "") + e.getMessage());
        }
    }

    /**
     * Assert two {@linkplain InputStream input streams} to deliver equal content.
     * 
     * @param expected reference input
     * @param current input to compare
     * @since 1.0
     */
    public static void assertInputStreamEquals(final InputStream expected, final InputStream current) {
        assertInputStreamEquals(null, expected, current);
    }

    /**
     * Assert two {@linkplain InputStream input streams} to deliver equal content.
     * 
     * @param message the error message
     * @param expected reference input
     * @param current input to compare
     * @since 1.0
     */
    public static void assertInputStreamEquals(
            final String message, final InputStream expected, final InputStream current) {
        long counter = 0;
        int eByte, cByte;
        try {
            for (; (eByte = expected.read()) != -1; ++counter) {
                cByte = current.read();
                if (eByte != cByte) {
                    Assert.assertEquals((message != null ? message + ": " : "")
                            + "Stream not equal at position "
                            + counter, eByte, cByte);
                }
            }
        } catch (final IOException e) {
            Assert.fail((message != null ? message + ": " : "") + e.getMessage());
        }
    }
}
