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
package org.apache.commons.id.uuid;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Commons-Uid team
 * @version $Id: VersionFourGeneratorTest.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class VersionFourGeneratorTest extends TestCase {

    public void testCanGenerateUUIDsWithStandardRandomizer() {
        Set set = new HashSet();
        VersionFourGenerator generator = new VersionFourGenerator();
        // following assertions are valid, if all UUIDs are unique
        assertTrue(set.add(generator.nextIdentifier()));
        assertTrue(set.add(generator.nextIdentifier()));
        assertTrue(set.add(generator.nextIdentifier()));
        assertTrue(set.add(generator.nextIdentifier()));
        assertTrue(set.add(generator.nextIdentifier()));
    }

    public void testCanGenerateUUIDsWithSecureRandomizer() {
        Set set = new HashSet();
        VersionFourGenerator generator = new VersionFourGenerator();
        // following assertions are valid, if all UUIDs are unique
        assertTrue(set.add(generator.nextIdentifier(true)));
        assertTrue(set.add(generator.nextIdentifier(true)));
        assertTrue(set.add(generator.nextIdentifier(true)));
        assertTrue(set.add(generator.nextIdentifier(true)));
        assertTrue(set.add(generator.nextIdentifier(true)));
    }
}
