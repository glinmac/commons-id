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
 * Tests the org.apache.commons.id.serial.PrefixedAlphanumericGenerator class.
 *
 * @author Commons-Id team
 * @version $Id$
 */
public class PrefixedAlphanumericGeneratorTest extends TestCase {

    public void testConstructor() {
        PrefixedAlphanumericGenerator pag0 = new PrefixedAlphanumericGenerator("foo", true, 15);
        PrefixedAlphanumericGenerator pag1 = new PrefixedAlphanumericGenerator("foo", false, 15);
        PrefixedAlphanumericGenerator pag3 = new PrefixedAlphanumericGenerator("foo", true, 4);
        PrefixedAlphanumericGenerator pag4 = new PrefixedAlphanumericGenerator("", true, 15);

        try
        {
            PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator(null, true, 15);
            fail("ctr(null,,) expected IllegalArgumentException");
        }
        catch (NullPointerException e)
        {
            // expected
        }

        try
        {
            PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, 0);
            fail("ctr(,,0) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, -1);
            fail("ctr(,,-1) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, 3); // 3 == "foo".length()
            fail("ctr(,,3) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, 2); // 2 < "foo".length()
            fail("ctr(,,2) expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    public void testPrefixedNumericGenerator() {
        PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, 15);
        assertNotNull("pag not null", pag);
        assertTrue("pag wrap == true", pag.isWrap());
        assertEquals("pag prefix == foo", "foo", pag.getPrefix());
        assertTrue("pag maxLength > prefix length", pag.maxLength() > "foo".length());
        assertTrue("pag minLength > prefix length", pag.minLength() > "foo".length());
        assertTrue("pag size > prefix length", pag.getSize() > "foo".length());

        pag.setWrap(false);
        assertTrue("pag wrap == false", pag.isWrap() == false);

        pag.setWrap(true);
        assertTrue("pag wrap == true", pag.isWrap());
    }

    public void testIdentifiers() {
        PrefixedAlphanumericGenerator pag = new PrefixedAlphanumericGenerator("foo", true, 15);
        assertEquals("foo000000000001", pag.nextStringIdentifier());
        assertEquals("foo000000000002", pag.nextStringIdentifier());
        assertEquals("foo000000000003", pag.nextIdentifier());
        assertEquals("foo000000000004", pag.nextIdentifier());
        assertEquals("foo000000000005", pag.nextStringIdentifier());
        assertEquals("foo000000000006", pag.nextStringIdentifier());
        assertEquals("foo000000000007", pag.nextStringIdentifier());
        assertEquals("foo000000000008", pag.nextStringIdentifier());
        assertEquals("foo000000000009", pag.nextStringIdentifier());
        assertEquals("foo00000000000a", pag.nextStringIdentifier());
        assertEquals("foo00000000000b", pag.nextStringIdentifier());
    }

    public void testWrap() {
        PrefixedAlphanumericGenerator wrap = new PrefixedAlphanumericGenerator("foo", true, 4);
        assertEquals("foo1", wrap.nextStringIdentifier());
        for (int i = 0; i < 33; i++) {
            wrap.nextStringIdentifier();
        }
        assertEquals("fooz", wrap.nextStringIdentifier());  // last identifier
        assertEquals("foo0", wrap.nextStringIdentifier());  // wrap
        assertEquals("foo1", wrap.nextStringIdentifier());

        try
        {
            PrefixedAlphanumericGenerator noWrap = new PrefixedAlphanumericGenerator("foo", false, 4);
            for (int i = 0; i < 34; i++) {
                noWrap.nextStringIdentifier();
            }
            assertEquals("fooz", noWrap.nextStringIdentifier());  // last identifier
            noWrap.nextStringIdentifier(); // attempt to wrap

            fail("noWrap.nextStringIdentifier expected IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            // expected
        }
    }
}
