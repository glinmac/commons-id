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
package org.apache.commons.id.serial;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.id.SerializationTestContext;
import org.apache.commons.id.StringIdentifierGenerator;
import org.apache.commons.id.test.AssertSerialization;

import java.io.Serializable;

/**
 * @author Commons-Uid team
 * @version $Id: NumericGeneratorTest.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class NumericGeneratorTest extends TestCase {
    
    /** Test String Numeric identifier */
    public void testStringNumericLong() {
        StringIdentifierGenerator f = new NumericGenerator(true, 0);
        assertEquals("0", f.nextStringIdentifier());
        assertEquals("1", f.nextStringIdentifier());
        assertEquals("2", f.nextIdentifier());
        assertEquals("3", f.nextStringIdentifier());
        assertEquals("4", f.nextIdentifier());
        assertEquals("5", f.nextStringIdentifier());
        assertEquals("6", f.nextIdentifier());
        assertEquals("7", f.nextIdentifier());
    }

    /** Test String Numeric identifier initialization */
    public void testStringNumericInit() {
        StringIdentifierGenerator f = new NumericGenerator(true, 100);
        assertEquals("100", f.nextStringIdentifier());
        assertEquals("101", f.nextStringIdentifier());
    }

    /** Test String Numeric identifier wrapping */
    public void testStringNumericWrap() {
        StringIdentifierGenerator f = new NumericGenerator(true, Long.MAX_VALUE - 1);
        assertEquals(Long.toString(Long.MAX_VALUE - 1), f.nextStringIdentifier());
        assertEquals(Long.toString(Long.MAX_VALUE), f.nextStringIdentifier());
        assertEquals(Long.toString(Long.MIN_VALUE), f.nextStringIdentifier());
    }

    /** Test String Numeric identifier without wrapping */
    public void testStringNumericNoWrap() {
        StringIdentifierGenerator f = new NumericGenerator(false, Long.MAX_VALUE);
        try {
            f.nextStringIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
    }
    
    /** Test String Numeric generator with no wrapping is not resetted */
    public void testStringNumericNoWrapIsNotResetted() {
        StringIdentifierGenerator f = new NumericGenerator(false, Long.MAX_VALUE);
        try {
            f.nextStringIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
        try {
            f.nextStringIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
    }
    
    /**
     * {@link TestSuite} for SessionIdGenerator. Ensures serialization.
     * 
     * @return the TestSuite
     */
    public static TestSuite suite() {
        final TestSuite suite = new TestSuite(NumericGeneratorTest.class);
        suite.addTest(AssertSerialization.createSerializationTestSuite(new SerializationTestContext() {

            public void verify(Object serialized, long uid) {
                NumericGenerator test = (NumericGenerator)serialized;
                NumericGenerator idGenerator = (NumericGenerator)createReference();
                assertEquals(idGenerator.maxLength(), test.maxLength());
                assertEquals(idGenerator.minLength(), test.minLength());
                assertEquals(idGenerator.isWrap(), test.isWrap());
                assertEquals("3", test.nextStringIdentifier());
            }

            public Serializable createReference() {
                return new NumericGenerator(false, 3);
            }

            public Class getType() {
                return NumericGenerator.class;
            }
        }));
        return suite;
    }
}
