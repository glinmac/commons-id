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
 * Tests the org.apache.commons.id.ConstantIdentifierGenerator class.
 *
 * @author Commons-id team
 * @version $Id$
 */
public class ConstantIdentifierGeneratorTest extends TestCase {

    protected String testConstant = "testConstant";
    
    public void testGetInstance() {
        StringIdentifierGenerator identifier = 
            ConstantIdentifierGenerator.getInstance(testConstant);
        assertEquals(testConstant,identifier.nextStringIdentifier());                
    }
    
    public void testNextStringIdentifier() {
        ConstantIdentifierGenerator identifier = 
            new ConstantIdentifierGenerator(testConstant);
        assertEquals(testConstant,identifier.nextStringIdentifier());          
    }
    
    public void testNullConstant() { 
        try {
            ConstantIdentifierGenerator identifier = 
                new ConstantIdentifierGenerator(null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        try {
            StringIdentifierGenerator identifier = 
                ConstantIdentifierGenerator.getInstance(null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    public void testEmptyConstant() {
        ConstantIdentifierGenerator identifier = 
            new ConstantIdentifierGenerator("");
        assertEquals("",identifier.nextStringIdentifier()); 
    }
    
    public void testDefault() {
        ConstantIdentifierGenerator identifier = 
            new ConstantIdentifierGenerator();
        assertEquals("",identifier.nextStringIdentifier()); 
    }
    
    public void testMaxMinLength() {
        ConstantIdentifierGenerator identifier = 
            new ConstantIdentifierGenerator(testConstant);
        long len = testConstant.length();
        assertEquals(len, identifier.maxLength());
        assertEquals(len, identifier.minLength());  
        
        identifier = new ConstantIdentifierGenerator("");
        assertEquals(0, identifier.maxLength());
        assertEquals(0, identifier.minLength());        
    }
}
