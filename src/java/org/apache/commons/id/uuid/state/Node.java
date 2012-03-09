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

package org.apache.commons.id.uuid.state;

import java.util.Arrays;

import org.apache.commons.id.uuid.clock.Clock;
import org.apache.commons.id.uuid.clock.OverClockedException;


/**
 * <p>The <code>Node</code> class represents the data and accessors for a single
 * node identifier. The node id is generally the IEEE 802 address, the clock
 * sequence, and last timestamp generated are all attributes of a node that need
 * to be maintained.</p>
 *
 * @author Commons-Id team
 * @version $Id: Node.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class Node {

    /** The node identifier bytes this class represents. */
    private byte[] id;

    /** The clock sequence associated with this node. */
    private short clockSequence;

    /** The last time stamp used. */
    private long lastTimestamp;

    /** The Clock implementation instance for this Node. */
    private Clock clock;

    /**
     * <p>Constructor used to create a <node>Node</node> when the lastTimestamp
     * and clock sequence are unavailable.</p>
     *
     * @param   nodeId the byte array representing this nodes identifier.
     *          Usually the  IEEE 802 address bytes.
     */
    public Node(byte[] nodeId) {
        id = nodeId;
        clockSequence = StateHelper.newClockSequence();
        clock = StateHelper.getClockImpl();
    }

    /**
     * <p>Constructor used to create a <node>Node</node> when all persistent state
     * information is available; node bytes, lastTimestamp, and clock sequence.
     * </p>
     *
     * @param   nodeId the byte array representing this nodes identifier.
     *          Usually the  IEEE 802 address bytes.
     * @param   lastTime the last timestamp this <code>Node</code> used.
     * @param   clockSeq the last clock sequence used in generation from this node.
     */
    public Node(byte[] nodeId, long lastTime, short clockSeq) {
        id = nodeId;
        lastTimestamp = lastTime;
        clockSequence = clockSeq;
        clock = StateHelper.getClockImpl();
    }

    /**
     * <p>Returns the node identifier bytes for this node.</p>
     *
     * @return the node identifier bytes for this node.
     */

    public byte[] getNodeIdentifier() {
        return id;
    }

    /**
     * <p>Returns the clock sequence used in this node.</p>
     *
     * @return the clock sequence used in this node.
     */
    public short getClockSequence() {
        return clockSequence;
    }

    /**
     * <p>Increments the clock sequence in this node.</p>
     */
    private void incrementClockSequence() {
        //Increment, but if it's greater than its 14-bits, reset it
        if (++clockSequence > 0x3FFF) {
            clockSequence = 0;
        }
    }

    /**
     * <p>Returns the time in UUID time.</p>
     *
     * @return the time in UUID time.
     * @throws OverClockedException the max number of timestamps generated in
     * this interval has been exceeded.
     */
    public long getUUIDTime() throws OverClockedException {
        long newTime = clock.getUUIDTime();
        if (newTime <= lastTimestamp) {
            incrementClockSequence();
        }
        lastTimestamp = newTime;
        return newTime;
    }

    /**
     * <p>Returns true if the identifier value in this Node is equal to the
     * identifier value in the compare to Node.</p>
     *
     * @param compareTo the Node to compare for equivalence.
     * @return true if the two node's identifiers are equal; otherwise false.
     * @see Object#equals
     */
    public boolean equals(Object compareTo) {
        if (compareTo instanceof Node) {
            byte[] comp = ((Node) compareTo).getNodeIdentifier();
            return Arrays.equals(comp, this.id);
        } else if (compareTo instanceof byte[]) {
            return Arrays.equals((byte[]) compareTo, id);
        }
        return false;
    }

    /**
     * @see Object#hashCode
     */
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < id.length; i++) {
            hash += id[i];
        }
        return hash;
    }
    /**
     * <p>Returns the last uuid timestamp from this <code>Node</code>.</p>
     *
     * @return the last uuid timestamp from this Node.
     */
    public long getLastTimestamp() {
        return lastTimestamp;
    }

}
