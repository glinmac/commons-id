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

import org.apache.commons.id.IdentifierGenerator;
import org.apache.commons.id.SerializationTestContext;
import org.apache.commons.id.StringIdentifierGenerator;
import org.apache.commons.id.test.AssertSerialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;


/**
 * @author Commons-Id team
 */
public class TimeBasedAlphanumericIdentifierGeneratorTest extends TestCase {

    /**
     * Test constant size of generated identifier.
     */
    public void testIdentifierSizeIsConstant() {
        final int maxSize = Long.toString(Long.MAX_VALUE, 36).length();
        StringIdentifierGenerator idGenerator = new TimeBasedAlphanumericIdentifierGenerator(0);
        assertEquals(maxSize, idGenerator.nextStringIdentifier().length());
        assertEquals(maxSize, idGenerator.maxLength());
        assertEquals(maxSize, idGenerator.minLength());
        idGenerator = new TimeBasedAlphanumericIdentifierGenerator(1);
        assertEquals(maxSize + 1, idGenerator.nextStringIdentifier().length());
        assertEquals(maxSize + 1, idGenerator.maxLength());
        assertEquals(maxSize + 1, idGenerator.minLength());
        idGenerator = new TimeBasedAlphanumericIdentifierGenerator(10);
        assertEquals(maxSize + 10, idGenerator.nextStringIdentifier().length());
        assertEquals(maxSize + 10, idGenerator.maxLength());
        assertEquals(maxSize + 10, idGenerator.minLength());
    }

    /**
     * Test illegal values for the postfix size.
     */
    public void testIllegalPostfixSize() {
        try {
            new TimeBasedAlphanumericIdentifierGenerator(-1);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
            // OK
        }
        final int maxSize = Long.toString(Long.MAX_VALUE, 36).length();
        try {
            new TimeBasedAlphanumericIdentifierGenerator(maxSize + 1);
            fail("Thrown " + IllegalArgumentException.class.getName() + " expected");
        } catch (final IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test that an IllegalStateException is thrown if no more unique identifiers can be generated.
     */
    public void testIllegalStateWhenTooManyIdentifiersGenerated() {
        final IdentifierGenerator idGenerator = new TimeBasedAlphanumericIdentifierGenerator(0);
        try {
            idGenerator.nextIdentifier();
            idGenerator.nextIdentifier();
            // Normally this should have been enough. Unfortunately the test fails, if the OS is doing
            // forward time shifts, then we get new ids anyway. So force the exception ...
            for(int i = 1000; i-- > 0; ) {
                idGenerator.nextIdentifier();
            }
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
            // OK
        }
    }

    /**
     * Test that the generator can be tweaked to start with '0'.
     */
    public void testMayStartWithIdentifierOfZeros() {
        final int maxSize = Long.toString(Long.MAX_VALUE, 36).length();
        final char[] zeros = new char[maxSize];
        Arrays.fill(zeros, '0');

        final TimeSliceSynchronizer synchronizer = new TimeSliceSynchronizer() {
            void runTest() {
                // ... next id should be in same time slice if it is used as current offset
                final StringIdentifierGenerator idGenerator = new TimeBasedAlphanumericIdentifierGenerator(0, next);
                // note, that this might still fail on time shifting machines occasionally
                assertEquals(new String(zeros), idGenerator.nextStringIdentifier());
            }
            
        };
        synchronizer.runSynced();
    }

    /**
     * Ensure that the default postfix size is big enough for a lot faster computers.
     */
    public void testDefaultPostfixSizeIsGoodEnough() {
        final List idList = new ArrayList();

        final IdentifierGenerator idGenerator = new TimeBasedAlphanumericIdentifierGenerator();

        // synchronize with current time slice ...
        long waitForNextPeriod = System.currentTimeMillis();
        long next = waitForNextPeriod;
        while (next <= waitForNextPeriod) {
            next = System.currentTimeMillis();
        }

        // ... and now generate in the current one as much as possible
        waitForNextPeriod = next;
        while (next == waitForNextPeriod) {
            idList.add(idGenerator.nextIdentifier());
            next = System.currentTimeMillis();
        }

        // should not exceed the 10th number of possible values
        assertTrue(idList.size() < (36 * 36 * 36) / 10);
    }

    /**
     * Test if generator can handle an internal overflow as it may happen for time values and still
     * delivers increasing ids.
     */
    public void testInternalOverflowStillIncreasesIds() {
        final List idList = new ArrayList();
        final TimeZone utc = TimeZone.getTimeZone("UTC");
        final long first = Calendar.getInstance(utc).getTime().getTime();
        // 100 ms before internal overflow
        final long offset = first < 0 ? 0 : -(Long.MAX_VALUE - first - 100);
        final IdentifierGenerator idGenerator = new TimeBasedAlphanumericIdentifierGenerator(
                3, offset);
        while (Calendar.getInstance(utc).getTime().getTime() - first < 200) {
            idList.add(idGenerator.nextIdentifier());
        }
        final List sorted = new ArrayList(idList);
        Collections.sort(sorted);
        final Iterator idIter = idList.iterator();
        final Iterator sortIter = sorted.iterator();
        for (int i = 0; idIter.hasNext(); ++i) {
            assertEquals("Index " + i, idIter.next(), sortIter.next());
        }
    }

    /**
     * Test ensures, that generator can recalculate the time from the id if internally no overflow
     * had happened.
     */
    public void testCanRetrieveTimeFromIdWithoutInternalOverflow() {
        final TimeSliceSynchronizer synchronizer = new TimeSliceSynchronizer() {
            TimeBasedAlphanumericIdentifierGenerator idGenerator;
            void initialize() {
                idGenerator = new TimeBasedAlphanumericIdentifierGenerator(
                        4, waitForNextPeriod - 1000);
            }

            void runTest() {
                // note, that this might still fail on time shifting machines occasionally
                final String id = idGenerator.nextStringIdentifier();
                assertEquals(next, idGenerator.getMillisecondsFromId(id, waitForNextPeriod - 1000));
            }
            
        };
        synchronizer.runSynced();
    }

    /**
     * Test ensures, that generator can recalculate the time from the id even if internally an
     * overflow had happened.
     */
    public void testCanRetrieveTimeFromIdWithInternalOverflow() {
        final TimeSliceSynchronizer synchronizer = new TimeSliceSynchronizer() {
            TimeBasedAlphanumericIdentifierGenerator idGenerator;
            void initialize() {
                idGenerator = new TimeBasedAlphanumericIdentifierGenerator(
                        4, waitForNextPeriod + 1000);
            }

            void runTest() {
                // note, that this might still fail on time shifting machines occasionally
                final String id = idGenerator.nextStringIdentifier();
                assertEquals(next, idGenerator.getMillisecondsFromId(id, waitForNextPeriod + 1000));
            }
            
        };
        synchronizer.runSynced();
    }

    /**
     * Test if different instances of the generator still deliver unique ids.
     */
    public void testDifferentGeneratorInstancesWillProduceStillUniqueIds() {
        final IdentifierGenerator idGenerators[] = new IdentifierGenerator[]{
                new TimeBasedAlphanumericIdentifierGenerator(),
                new TimeBasedAlphanumericIdentifierGenerator(),
                new TimeBasedAlphanumericIdentifierGenerator(),};

        final int loop = 1000;
        final List idList = new ArrayList();
        for (int i = loop; i-- > 0;) {
            for (int j = 0; j < idGenerators.length; ++j) {
                idList.add(idGenerators[j].nextIdentifier());
            }
        }

        assertEquals(loop * idGenerators.length, idList.size());
        assertEquals(loop * idGenerators.length, new HashSet(idList).size());
    }

    /**
     * {@link TestSuite} for TimeBasedAlphanumericIdentifierGenerator. Ensures serialization.
     * 
     * @return the TestSuite
     */
    public static TestSuite suite() {
        final TestSuite suite = new TestSuite(TimeBasedAlphanumericIdentifierGeneratorTest.class);
        suite.addTest(AssertSerialization.createSerializationTestSuite(new SerializationTestContext() {

            public void verify(Object serialized, long uid) {
                TimeBasedAlphanumericIdentifierGenerator test = (TimeBasedAlphanumericIdentifierGenerator)serialized;
                TimeBasedAlphanumericIdentifierGenerator idGenerator = (TimeBasedAlphanumericIdentifierGenerator)createReference();
                assertEquals(idGenerator.maxLength(), test.maxLength());
                assertEquals(idGenerator.minLength(), test.minLength());
            }

            public Serializable createReference() {
                return new TimeBasedAlphanumericIdentifierGenerator(1);
            }

            public Class getType() {
                return TimeBasedAlphanumericIdentifierGenerator.class;
            }
        }));
        return suite;
    }
 
    /**
     * A guardian class that tries hard to synchronize the test to run in the same time slice of
     * the CPU even on time shifting machines.
     */
    abstract static class TimeSliceSynchronizer {

        long waitForNextPeriod;
        long next;

        void runSynced() {
            // idea here is to generate an id in the same time slice of the CPU as the last
            // System.currentTimeMillis() call
            int fails = 0;
            for (int tries = 10; tries-- > 0;) {
                waitForNextPeriod = System.currentTimeMillis();
                initialize();

                next = waitForNextPeriod;
                // allow time shifts back into time, the generator can handle such shifts
                while (next <= waitForNextPeriod) {
                    next = System.currentTimeMillis();
                }

                // time shift into future, no problem for the generator, but breaks tests that
                // assume to run in the same time slice
                if (next > System.currentTimeMillis()) {
                    ++fails;
                }
            }
            // we tried 10 times to be sure not running on a time shifting machine
            if (fails > 0) {
                fail("Cannot perform this test on a machine, that is steadily time shifting into future!");
            }

            runTest();
        }

        void initialize() {
            // do nothing
        }

        abstract void runTest();
    }
}
