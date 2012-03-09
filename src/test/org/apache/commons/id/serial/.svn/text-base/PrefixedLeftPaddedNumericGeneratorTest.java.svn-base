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
 * Tests the org.apache.commons.id.serial.PrefixedLeftPaddedNumericGenerator class.
 *
 * @author Commons-Id team
 * @version $Id$
 */
public class PrefixedLeftPaddedNumericGeneratorTest extends TestCase {

    public void testConstructor() {
        PrefixedLeftPaddedNumericGenerator plpng0 = new PrefixedLeftPaddedNumericGenerator("foo", true, 15);
        PrefixedLeftPaddedNumericGenerator plpng1 = new PrefixedLeftPaddedNumericGenerator("foo", false, 15);
        PrefixedLeftPaddedNumericGenerator plpng2 = new PrefixedLeftPaddedNumericGenerator("foo", true, 4);
        PrefixedLeftPaddedNumericGenerator plpng3 = new PrefixedLeftPaddedNumericGenerator("", true, 15);

        try
        {
            PrefixedLeftPaddedNumericGenerator plpag = new PrefixedLeftPaddedNumericGenerator(null, true, 15);
            fail("ctr(null,,) expected IllegalArgumentException");
        }
        catch (NullPointerException e)
        {
            // expected
        }

        try
        {
            PrefixedLeftPaddedNumericGenerator plpag = new PrefixedLeftPaddedNumericGenerator("foo", true, 0);
            fail("ctr(,,0) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedLeftPaddedNumericGenerator plpag = new PrefixedLeftPaddedNumericGenerator("foo", true, -1);
            fail("ctr(,,-1) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedLeftPaddedNumericGenerator plpag = new PrefixedLeftPaddedNumericGenerator("foo", true, 3); // 3 == "foo".length()
            fail("ctr(,,3) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedLeftPaddedNumericGenerator plpag = new PrefixedLeftPaddedNumericGenerator("foo", true, 2); // 2 < "foo".length()
            fail("ctr(,,2) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testPrefixedNumericGenerator() {
        PrefixedLeftPaddedNumericGenerator plpng = new PrefixedLeftPaddedNumericGenerator("foo", true, 15);
        assertNotNull("plpng not null");
        assertTrue("plpng wrap == true", plpng.isWrap());
        assertEquals("plpng prefix == foo", "foo", plpng.getPrefix());
        assertTrue("plpng maxLength > prefix length", plpng.maxLength() > "foo".length());
        assertTrue("plpng minLength > prefix length", plpng.minLength() > "foo".length());
        assertTrue("plpng size > prefix length", plpng.getSize() > "foo".length());

        plpng.setWrap(false);
        assertTrue("plpng wrap == false", plpng.isWrap() == false);

        plpng.setWrap(true);
        assertTrue("plpng wrap == true", plpng.isWrap());
    }

    public void testIdentifiers() {
        PrefixedLeftPaddedNumericGenerator plpng = new PrefixedLeftPaddedNumericGenerator("foo", true, 15);
        assertEquals("foo000000000001", plpng.nextStringIdentifier());
        assertEquals("foo000000000002", plpng.nextStringIdentifier());
        assertEquals("foo000000000003", plpng.nextIdentifier());
        assertEquals("foo000000000004", plpng.nextIdentifier());
        assertEquals("foo000000000005", plpng.nextStringIdentifier());
        assertEquals("foo000000000006", plpng.nextStringIdentifier());
        assertEquals("foo000000000007", plpng.nextStringIdentifier());
        assertEquals("foo000000000008", plpng.nextStringIdentifier());
        assertEquals("foo000000000009", plpng.nextStringIdentifier());
        assertEquals("foo000000000010", plpng.nextStringIdentifier());
        assertEquals("foo000000000011", plpng.nextStringIdentifier());
    }

    public void testWrap() {
        PrefixedLeftPaddedNumericGenerator wrap = new PrefixedLeftPaddedNumericGenerator("foo", true, 4);
        assertEquals("foo1", wrap.nextStringIdentifier());
        assertEquals("foo2", wrap.nextStringIdentifier());
        assertEquals("foo3", wrap.nextIdentifier());
        assertEquals("foo4", wrap.nextIdentifier());
        assertEquals("foo5", wrap.nextStringIdentifier());
        assertEquals("foo6", wrap.nextStringIdentifier());
        assertEquals("foo7", wrap.nextStringIdentifier());
        assertEquals("foo8", wrap.nextStringIdentifier());
        assertEquals("foo9", wrap.nextStringIdentifier()); // last identifier
        assertEquals("foo0", wrap.nextStringIdentifier()); // wrap
        assertEquals("foo1", wrap.nextStringIdentifier());

        try
        {
            PrefixedLeftPaddedNumericGenerator noWrap = new PrefixedLeftPaddedNumericGenerator("foo", false, 4);
            assertEquals("foo1", noWrap.nextStringIdentifier());
            assertEquals("foo2", noWrap.nextStringIdentifier());
            assertEquals("foo3", noWrap.nextIdentifier());
            assertEquals("foo4", noWrap.nextIdentifier());
            assertEquals("foo5", noWrap.nextStringIdentifier());
            assertEquals("foo6", noWrap.nextStringIdentifier());
            assertEquals("foo7", noWrap.nextStringIdentifier());
            assertEquals("foo8", noWrap.nextStringIdentifier());
            assertEquals("foo9", noWrap.nextStringIdentifier()); // last identifier
            noWrap.nextStringIdentifier(); // attempt to wrap

            fail("noWrap.nextStringIdentifier expected IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            // expected
        }
    }
}
