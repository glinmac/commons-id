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

/**
 * Tests the org.apache.commons.id.serial.PrefixedNumericGenerator class.
 *
 * @author Commons-Id team
 * @version $Id$
 */
public class PrefixedNumericGeneratorTest extends TestCase {

    public void testConstructor() {
        PrefixedNumericGenerator png0 = new PrefixedNumericGenerator("foo", true, 0l);
        PrefixedNumericGenerator png1 = new PrefixedNumericGenerator("foo", false, 0l);
        PrefixedNumericGenerator png2 = new PrefixedNumericGenerator("foo", true, Long.MAX_VALUE);
        PrefixedNumericGenerator png3 = new PrefixedNumericGenerator("foo", true, Long.MIN_VALUE);
        PrefixedNumericGenerator png4 = new PrefixedNumericGenerator("", true, 0l);

        try
        {
            PrefixedNumericGenerator png = new PrefixedNumericGenerator(null, true, 0l);
            fail("ctr(null,,) expected IllegalArgumentException");
        }
        catch (NullPointerException e)
        {
            // expected
        }
    }

    public void testPrefixedNumericGenerator() {
        PrefixedNumericGenerator png = new PrefixedNumericGenerator("foo", true, 0l);
        assertNotNull("png not null");
        assertTrue("png wrap == true", png.isWrap());
        assertEquals("png prefix == foo", "foo", png.getPrefix());
        assertTrue("png maxLength > prefix length", png.maxLength() > "foo".length());
        assertTrue("png minLength > prefix length", png.minLength() > "foo".length());

        png.setWrap(false);
        assertTrue("png wrap == false", png.isWrap() == false);

        png.setWrap(true);
        assertTrue("png wrap == true", png.isWrap());
    }

    public void testIdentifiers() {
        PrefixedNumericGenerator png = new PrefixedNumericGenerator("foo", true, 0l);
        assertEquals("foo0", png.nextStringIdentifier());
        assertEquals("foo1", png.nextStringIdentifier());
        assertEquals("foo2", png.nextIdentifier());
        assertEquals("foo3", png.nextIdentifier());
        assertEquals("foo4", png.nextStringIdentifier());
    }

    public void testWrap() {
        PrefixedNumericGenerator wrap = new PrefixedNumericGenerator("foo", true, Long.MAX_VALUE);
        wrap.nextStringIdentifier(); // last long identifier
        wrap.nextStringIdentifier(); // wrap
        wrap.nextStringIdentifier();

        try
        {
            PrefixedNumericGenerator noWrap = new PrefixedNumericGenerator("foo", false, Long.MAX_VALUE);
            noWrap.nextStringIdentifier(); // last long identifier
            noWrap.nextStringIdentifier(); // attempt to wrap
            fail("noWrap.nextStringIdentifier expected IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            // expected
        }
    }
}
