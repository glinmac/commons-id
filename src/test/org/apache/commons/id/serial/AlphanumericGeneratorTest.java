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
 * @version $Id: AlphanumericGeneratorTest.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class AlphanumericGeneratorTest extends TestCase {
    
    /** Test String AlphaNumeric identifier */
    public void testStringAlphanumeric() {
        StringIdentifierGenerator f = new AlphanumericGenerator(true);
        assertEquals("000000000000001", f.nextStringIdentifier());
        assertEquals("000000000000002", f.nextIdentifier());
        assertEquals("000000000000003", f.nextStringIdentifier());
        assertEquals("000000000000004", f.nextStringIdentifier());
        assertEquals("000000000000005", f.nextStringIdentifier());
        assertEquals("000000000000006", f.nextStringIdentifier());
        assertEquals("000000000000007", f.nextStringIdentifier());
        assertEquals("000000000000008", f.nextStringIdentifier());
        assertEquals("000000000000009", f.nextStringIdentifier());
        assertEquals("00000000000000a", f.nextStringIdentifier());
        assertEquals("00000000000000b", f.nextStringIdentifier());
        assertEquals("00000000000000c", f.nextStringIdentifier());
        assertEquals("00000000000000d", f.nextIdentifier());
        assertEquals("00000000000000e", f.nextStringIdentifier());
        assertEquals("00000000000000f", f.nextStringIdentifier());
        assertEquals("00000000000000g", f.nextStringIdentifier());
        assertEquals("00000000000000h", f.nextStringIdentifier());
        assertEquals("00000000000000i", f.nextStringIdentifier());
        assertEquals("00000000000000j", f.nextStringIdentifier());
        assertEquals("00000000000000k", f.nextStringIdentifier());
        assertEquals("00000000000000l", f.nextStringIdentifier());
        assertEquals("00000000000000m", f.nextStringIdentifier());
        assertEquals("00000000000000n", f.nextStringIdentifier());
        assertEquals("00000000000000o", f.nextStringIdentifier());
        assertEquals("00000000000000p", f.nextStringIdentifier());
        assertEquals("00000000000000q", f.nextStringIdentifier());
        assertEquals("00000000000000r", f.nextStringIdentifier());
        assertEquals("00000000000000s", f.nextStringIdentifier());
        assertEquals("00000000000000t", f.nextStringIdentifier());
        assertEquals("00000000000000u", f.nextStringIdentifier());
        assertEquals("00000000000000v", f.nextStringIdentifier());
        assertEquals("00000000000000w", f.nextStringIdentifier());
        assertEquals("00000000000000x", f.nextStringIdentifier());
        assertEquals("00000000000000y", f.nextStringIdentifier());
        assertEquals("00000000000000z", f.nextStringIdentifier());
        assertEquals("000000000000010", f.nextStringIdentifier());
        assertEquals("000000000000011", f.nextStringIdentifier());
        assertEquals("000000000000012", f.nextStringIdentifier());
        assertEquals("000000000000013", f.nextStringIdentifier());
        
        assertEquals(f.minLength(), f.maxLength());
    }

    /** Test String Numeric identifier with initial value */
    public void testStringAlphanumericWithInitialValue() {
        StringIdentifierGenerator f = new AlphanumericGenerator(true, "123");
        assertEquals("124", f.nextStringIdentifier());
        f = new AlphanumericGenerator(true, "129");
        assertEquals("12a", f.nextStringIdentifier());
        f = new AlphanumericGenerator(true, "12z");
        assertEquals("130", f.nextStringIdentifier());
    }

    /** Test String Numeric identifier with illegal initial value */
    public void testStringAlphanumericWithIllegalInitialValue() {
        try {
            StringIdentifierGenerator f = new AlphanumericGenerator(true, "1@3");
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
        }
        try {
            StringIdentifierGenerator f = new AlphanumericGenerator(false, -1);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
        }
        try {
            StringIdentifierGenerator f = new AlphanumericGenerator(true, -1);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
        }
        try {
            StringIdentifierGenerator f = new AlphanumericGenerator(false, 0);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
        }
        try {
            StringIdentifierGenerator f = new AlphanumericGenerator(true, 0);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
        }
    }
    
    /** Test String Numeric identifier wrapping */
    public void testStringAlphanumericWrap() {
        StringIdentifierGenerator f = new AlphanumericGenerator(true, 1);
        assertEquals("1", f.nextStringIdentifier());
        assertEquals("2", f.nextStringIdentifier());
        assertEquals("3", f.nextStringIdentifier());
        assertEquals("4", f.nextStringIdentifier());
        assertEquals("5", f.nextStringIdentifier());
        assertEquals("6", f.nextStringIdentifier());
        assertEquals("7", f.nextStringIdentifier());
        assertEquals("8", f.nextStringIdentifier());
        assertEquals("9", f.nextStringIdentifier());
        assertEquals("a", f.nextStringIdentifier());
        assertEquals("b", f.nextStringIdentifier());
        assertEquals("c", f.nextStringIdentifier());
        assertEquals("d", f.nextStringIdentifier());
        assertEquals("e", f.nextStringIdentifier());
        assertEquals("f", f.nextStringIdentifier());
        assertEquals("g", f.nextStringIdentifier());
        assertEquals("h", f.nextStringIdentifier());
        assertEquals("i", f.nextStringIdentifier());
        assertEquals("j", f.nextStringIdentifier());
        assertEquals("k", f.nextStringIdentifier());
        assertEquals("l", f.nextStringIdentifier());
        assertEquals("m", f.nextStringIdentifier());
        assertEquals("n", f.nextStringIdentifier());
        assertEquals("o", f.nextStringIdentifier());
        assertEquals("p", f.nextStringIdentifier());
        assertEquals("q", f.nextStringIdentifier());
        assertEquals("r", f.nextStringIdentifier());
        assertEquals("s", f.nextStringIdentifier());
        assertEquals("t", f.nextStringIdentifier());
        assertEquals("u", f.nextStringIdentifier());
        assertEquals("v", f.nextStringIdentifier());
        assertEquals("w", f.nextStringIdentifier());
        assertEquals("x", f.nextStringIdentifier());
        assertEquals("y", f.nextStringIdentifier());
        assertEquals("z", f.nextStringIdentifier());
        assertEquals("0", f.nextStringIdentifier());
    }

    /** Test String Numeric identifier with no wrapping */
    public void testStringAlphanumericNoWrap() {
        StringIdentifierGenerator f = new AlphanumericGenerator(false, 1);
        assertEquals("1", f.nextStringIdentifier());
        assertEquals("2", f.nextStringIdentifier());
        assertEquals("3", f.nextStringIdentifier());
        assertEquals("4", f.nextStringIdentifier());
        assertEquals("5", f.nextStringIdentifier());
        assertEquals("6", f.nextStringIdentifier());
        assertEquals("7", f.nextStringIdentifier());
        assertEquals("8", f.nextStringIdentifier());
        assertEquals("9", f.nextStringIdentifier());
        assertEquals("a", f.nextStringIdentifier());
        assertEquals("b", f.nextStringIdentifier());
        assertEquals("c", f.nextStringIdentifier());
        assertEquals("d", f.nextStringIdentifier());
        assertEquals("e", f.nextStringIdentifier());
        assertEquals("f", f.nextStringIdentifier());
        assertEquals("g", f.nextStringIdentifier());
        assertEquals("h", f.nextStringIdentifier());
        assertEquals("i", f.nextStringIdentifier());
        assertEquals("j", f.nextStringIdentifier());
        assertEquals("k", f.nextStringIdentifier());
        assertEquals("l", f.nextStringIdentifier());
        assertEquals("m", f.nextStringIdentifier());
        assertEquals("n", f.nextStringIdentifier());
        assertEquals("o", f.nextStringIdentifier());
        assertEquals("p", f.nextStringIdentifier());
        assertEquals("q", f.nextStringIdentifier());
        assertEquals("r", f.nextStringIdentifier());
        assertEquals("s", f.nextStringIdentifier());
        assertEquals("t", f.nextStringIdentifier());
        assertEquals("u", f.nextStringIdentifier());
        assertEquals("v", f.nextStringIdentifier());
        assertEquals("w", f.nextStringIdentifier());
        assertEquals("x", f.nextStringIdentifier());
        assertEquals("y", f.nextStringIdentifier());
        assertEquals("z", f.nextStringIdentifier());
        try {
            f.nextStringIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
    }
    
    /** Test String Numeric generator with no wrapping is not resetted */
    public void testStringAlphanumericNoWrapIsNotResetted() {
        StringIdentifierGenerator f = new AlphanumericGenerator(false, 1);
        try {
            while(true) f.nextStringIdentifier();
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
        final TestSuite suite = new TestSuite(AlphanumericGeneratorTest.class);
        suite.addTest(AssertSerialization.createSerializationTestSuite(new SerializationTestContext() {

            public void verify(Object serialized, long uid) {
                AlphanumericGenerator test = (AlphanumericGenerator)serialized;
                AlphanumericGenerator idGenerator = (AlphanumericGenerator)createReference();
                assertEquals(idGenerator.maxLength(), test.maxLength());
                assertEquals(idGenerator.minLength(), test.minLength());
                assertEquals(idGenerator.isWrap(), test.isWrap());
                assertEquals("4", test.nextStringIdentifier());
            }

            public Serializable createReference() {
                return new AlphanumericGenerator(false, "3");
            }

            public Class getType() {
                return AlphanumericGenerator.class;
            }
        }));
        return suite;
    }
}
