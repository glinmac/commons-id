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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.id.DecoderException;
import org.apache.commons.discovery.tools.DiscoverClass;
import org.apache.commons.id.uuid.Bytes;
import org.apache.commons.id.uuid.Constants;
import org.apache.commons.id.uuid.clock.Clock;
import org.apache.commons.id.uuid.clock.OverClockedException;
import org.apache.commons.id.DigestUtils;
import org.apache.commons.id.Hex;

/**
 * <p><code>StateHelper</code> provides helper methods for the uuid state
 * implementations.</p>
 *
 * @author Commons-Id team
 * @version $Id: StateHelper.java 480488 2006-11-29 08:57:26Z bayard $
 */
public final class StateHelper implements Constants {
    /** The key for the System.property containing the ClockImpl String. */
    public static final String UUID_CLOCK_IMPL_PROPERTY_KEY = Clock.class.getName();

    /** The key for the System.property containing the StateImpl String. */
    public static final String UUID_STATE_IMPL_PROPERTY_KEY = State.class.getName();

    /** Array length of node bytes. */
    public static final int NODE_ID_BYTE_LENGTH = 6;

    /** Number of bytes in a short. */
    public static final short BYTES_IN_SHORT = 2;

    /** Number of postitions to shift when shifting by one byte. */
    public static final short SHIFT_BY_BYTE = 8;

    /** Number of postitions to shift when shifting by one byte. */
    public static final short HOSTNAME_MAX_CHAR_LEN = 255;

    /** OR-Mask to set the node's multicast bit true. */
    private static final int MULTICAST_BIT_SET = 0x80;

    /** The maximum character length of a long */
    private static final short LONG_CHAR_LEN = 19;

    /** Standard one page buffer size */
    private static final int BUF_PAGE_SZ = 1024;

    /** Start of the XML document used to store state persistence in XML */
    protected static final String XML_DOC_START = "<?xml version=\"1.0\""
        + " encoding=\"UTF-8\" ?>"
        + "\n<!DOCTYPE uuidstate [\n"
        + "   <!ELEMENT uuidstate (node*)>\n"
        + "   <!ELEMENT node EMPTY>\n"
        + "   <!ATTLIST node id ID #REQUIRED>\n"
        + "   <!ATTLIST node clocksequence CDATA #IMPLIED>\n"
        + "   <!ATTLIST node lasttimestamp CDATA #IMPLIED>\n]>"
        + "\n<uuidstate synchInterval=\"";

    /** End of document start */
    protected static final String XML_DOC_START_END = "\">";
    /** Start of XML node tag */
    protected static final String XML_NODE_TAG_START = "\n\t<node id=\"";
    /** After id of XML node tag */
    protected static final String XML_NODE_TAG_AFTER_ID = "\" clocksequence=\"";
    /** After clock sequence of XML node tag */
    protected static final String XML_NODE_TAG_AFTER_CSEQ = "\" timestamp=\"";
    /** End of XML node tag */
    protected static final String XML_NODE_TAG_END = "\" />";
    /** End of the XML document used to store state persistence in XML */
    protected static final String XML_DOC_END = "\n</uuidstate>";

    /** Number of tokens in a MAC address with "-" as separator */
    private static final short MAC_ADDRESS_TOKEN_COUNT = 6;

    /** String length of a MAC address with "-" as separator */
    private static final short MAC_ADDRESS_CHAR_LENGTH = 17;

    /** Default constructor */
    private StateHelper() {
        super();
    }

    /**
     * <p>Creates a Random node identifier as described in IEFT UUID URN
     * specification.</p>
     *
     * @return a random node idenfifier based on MD5 of system information.
     */
    public static byte[] randomNodeIdentifier() {
        //Holds the 16 byte MD5 value
        byte[] seed = new byte[UUID_BYTE_LENGTH];
        //Set the initial string buffer capacity
        //Time + Object.hashCode + HostName + Guess of all system properties
        int bufSize = (LONG_CHAR_LEN * 2) + HOSTNAME_MAX_CHAR_LEN + (2 * BUF_PAGE_SZ);
        StringBuffer randInfo = new StringBuffer(bufSize);
        //Add current time
        long time = 0;
        try {
            time = getClockImpl().getUUIDTime();
        } catch (OverClockedException oce) {
            time = System.currentTimeMillis();
        }
        randInfo.append(time);

        //Add hostname
        try {
            InetAddress address = InetAddress.getLocalHost();
            randInfo.append(address.getHostName());
        } catch (UnknownHostException ukhe) {
            randInfo.append("Host Unknown");
        }
        //Add something else "random"
        randInfo.append(new Object().hashCode());

        //Add system properties
        Collection info = System.getProperties().values();
        Iterator it = info.iterator();
        while (it.hasNext()) {
            randInfo.append(it.next());
        }
        //MD5 Hash code the system information to get a node id.
        seed = DigestUtils.md5(randInfo.toString());

        //Return upper 6 bytes of hash
        byte[] raw = new byte[NODE_ID_BYTE_LENGTH];
        System.arraycopy(seed, 0, raw, 0, NODE_ID_BYTE_LENGTH);

        //Per draft set multi-cast bit true
        raw[0] |= MULTICAST_BIT_SET;

        return raw;
    }

    /**
     * <p>Generates a new security quality random clock sequence.</p>
     *
     * @return a new security quality random clock sequence.
     */
    public static short newClockSequence() {
        Random random = new Random();
        byte[] bytes = new byte[BYTES_IN_SHORT];
        random.nextBytes(bytes);
        return (short) (Bytes.toShort(bytes) & 0x3FFF);
    }

    /**
     * <p>Returns the Clock implementation using commons discovery.</p>
     *
     * @return the Clock implementation using commons discovery.
     */
    public static Clock getClockImpl() {
        Clock c = null;
        try {
             DiscoverClass dc = new DiscoverClass();
             c = (Clock) dc.newInstance(
             Clock.class,
             Clock.DEFAULT_CLOCK_IMPL);
        } catch (Exception ex) {
             // ignore as default implementation will be used.
        }
        return c;
    }

    /**
     * <p>Returns the <code>State</code> implementation in use.</p>
     *
     * @return the <code>State</code> implementation in use.
     */
    public static State getStateImpl() {
        State s = null;
        try {
             DiscoverClass dc = new DiscoverClass();
             s = (State) dc.newInstance(
             State.class,
             State.DEFAULT_STATE_IMPL);
        } catch (Exception ex) {
             // ignore as default implementation will be used.
        }
        return s;
    }

    /**
     * <p>Utility method decodes a valid MAC address in the form of
     * XX-XX-XX-XX-XX-XX where each XX represents a hexidecimal value.</p>
     * 
     * <p> Returns null if the address can not be decoded. </p>
     *
     * @param address the String hexidecimal dash separated MAC address.
     * @return a byte array representing the the address. Null if not a valid address.
     */
    public static byte[] decodeMACAddress(String address) {
        StringBuffer buf = new StringBuffer(MAC_ADDRESS_TOKEN_COUNT * 2);
        StringTokenizer tokens = new StringTokenizer(address, "-");
        if (tokens.countTokens() != MAC_ADDRESS_TOKEN_COUNT) {
            return null;
        } else {
            for (int i = 0; i < MAC_ADDRESS_TOKEN_COUNT; i++) {
                buf.append(tokens.nextToken());
            }
        }
        try {
            char[] c = buf.toString().toCharArray();
            return Hex.decodeHex(c);
        } catch (DecoderException de) {
            de.printStackTrace();
            return null;
        }
    }

    /**
     * <p>Returns the node id / address byte array in it's hexidecimal string
     * representation in the following format: XX-XX-XX-XX-XX-XX where each XX
     * represents a hexidecimal value.</p>
     *
     * @param address the 6 byte node id / address.
     * @return the node id /address byte array in as hexidecimal with dash
     * separating each octet.
     * @throws IOException an Input Output Exception.
     */
    public static String encodeMACAddress(byte[] address) throws IOException {
       char[] chars = Hex.encodeHex(address);
       StringBuffer buf = new StringBuffer(MAC_ADDRESS_CHAR_LENGTH);
       for (int i = 0; i < chars.length; i++) {
               buf.append(chars[i]);
               if (i != chars.length - 1 && i % 2 != 0) {
                   buf.append("-");
               }
       }
       return buf.toString().toUpperCase();
    }
}
