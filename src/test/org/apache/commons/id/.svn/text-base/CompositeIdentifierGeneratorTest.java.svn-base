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
package org.apache.commons.id;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.id.serial.AlphanumericGenerator;
import org.apache.commons.id.serial.NumericGenerator;
import org.apache.commons.id.test.AssertSerialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests the org.apache.commons.id.CompositeIdentifierGenerator class.
 *
 * @author Commons-id team
 * @version $Id$
 */
public class CompositeIdentifierGeneratorTest extends TestCase {

    public void testGetInstanceArrayInput() {
        // positive test
        CompositeIdentifierGenerator gen = (CompositeIdentifierGenerator)
            CompositeIdentifierGenerator.getInstance(makeTestArray());
        assertEquals(2, gen.getIdentifierGenerators().length);
        
        // null array
        StringIdentifierGenerator[] generators = null;
        try {
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for null array");
        } catch (IllegalArgumentException ex) {}
             
        // empty array
        try {
            CompositeIdentifierGenerator.getInstance(
                    new StringIdentifierGenerator[] {});
            fail("Expecting IllegalArgumentException for empty array");
        } catch (IllegalArgumentException ex) {}
        
        // nulls in array
        generators = makeTestArray();
        try {
            generators[0] = null;
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for array with nulls");
        } catch (IllegalArgumentException ex) {}
        generators = makeTestArray();
        try {
            generators[1] = null;
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for array with nulls");
        } catch (IllegalArgumentException ex) {}              
    }
    
    public void testGetInstanceCollectionInput() {
        // positive test
        CompositeIdentifierGenerator gen = (CompositeIdentifierGenerator)
            CompositeIdentifierGenerator.getInstance(makeTestCollection());
        assertEquals(2, gen.getIdentifierGenerators().length);
        
        // null collection
        ArrayList generators = null;
        try {
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for null collection");
        } catch (IllegalArgumentException ex) {}
             
        // empty collection
        try {
            CompositeIdentifierGenerator.getInstance(
                    new ArrayList());
            fail("Expecting IllegalArgumentException for empty collection");
        } catch (IllegalArgumentException ex) {}
        
        // nulls in collection
        generators = makeTestCollection();
        try {
            generators.add(0, null);
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for collection with nulls");
        } catch (IllegalArgumentException ex) {}
        generators = makeTestCollection();
        try {
            generators.add(2, null);
            CompositeIdentifierGenerator.getInstance(generators);
            fail("Expecting IllegalArgumentException for array with nulls");
        } catch (IllegalArgumentException ex) {}              
    }
    
    public void testNextStringIdentifier() {
        StringIdentifierGenerator generator =
            CompositeIdentifierGenerator.getInstance(makeTestArray());        
        assertEquals("0000000000000001",
                generator.nextStringIdentifier());
        assertEquals("1000000000000002",
                generator.nextStringIdentifier());
        assertEquals("2000000000000003",
                generator.nextStringIdentifier());
    }
    
    public void testLength() {
        StringIdentifierGenerator[] generators = makeTestArray();
        StringIdentifierGenerator generator =
            CompositeIdentifierGenerator.getInstance(generators);        
        long maxLength = 0;
        long minLength = 0;
        for (int i = 0; i < generators.length; i++) {
            maxLength += generators[i].maxLength();
            minLength += generators[i].minLength();
        }
        assertEquals(maxLength, generator.maxLength());
        assertEquals(minLength, generator.minLength());
    }
    
    /**
     * {@link TestSuite} for SessionIdGenerator. Ensures serialization.
     * 
     * @return the TestSuite
     */
    public static TestSuite suite() {
        final TestSuite suite = new TestSuite(CompositeIdentifierGeneratorTest.class);
        suite.addTest(AssertSerialization.createSerializationTestSuite(new SerializationTestContext() {

            public void verify(Object serialized, long uid) {
                CompositeIdentifierGenerator test = (CompositeIdentifierGenerator)serialized;
                CompositeIdentifierGenerator idGenerator = (CompositeIdentifierGenerator)createReference();
                StringIdentifierGenerator[] testGenerators =  test.getIdentifierGenerators();
                StringIdentifierGenerator[] generators =  idGenerator.getIdentifierGenerators();
                assertEquals(generators.length, testGenerators.length);
                assertSame(generators[0].getClass(), testGenerators[0].getClass());
                assertSame(generators[1].getClass(), testGenerators[1].getClass());
            }

            public Serializable createReference() {
                return new CompositeIdentifierGenerator(makeTestArray());
            }

            public Class getType() {
                return CompositeIdentifierGenerator.class;
            }
        }));
        return suite;
    }
    
    protected static StringIdentifierGenerator[] makeTestArray() {
        StringIdentifierGenerator[] out = 
        {new NumericGenerator(false, 0), 
                new AlphanumericGenerator(true)};
        return out;
    }
    
    protected static ArrayList makeTestCollection() {
        return new ArrayList(Arrays.asList(makeTestArray()));
    }
}
