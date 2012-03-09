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

import org.apache.commons.id.AbstractStringIdentifierGenerator;

/**
 * <code>PrefixedLeftPaddedNumericGenerator</code> is an Identifier Generator
 * that generates a left-padded incrementing number with a prefix as a String object.
 *
 * <p>All generated ids have the same length (prefixed and padded with 0's
 * on the left), which is determined by the <code>size</code> parameter passed
 * to the constructor.<p>
 *
 * <p>The <code>wrap</code> property determines whether or not the sequence wraps
 * when it reaches the largest value that can be represented in <code>size</code>
 * base 10 digits. If <code>wrap</code> is false and the the maximum representable
 * value is exceeded, an {@link IllegalStateException} is thrown.</p>
 *
 * @author Commons-Id team
 * @version $Id$
 */
public class PrefixedLeftPaddedNumericGenerator extends AbstractStringIdentifierGenerator {

    /** Prefix. */
    private final String prefix;

    /** Should the counter wrap. */
    private boolean wrap = true;

    /** The counter. */
    private char[] count = null;

    /** '9' char. */
    private static final char NINE_CHAR = '9';


    /**
     * Create a new prefixed left-padded numeric generator with the specified prefix.
     *
     * @param prefix prefix, must not be null or empty
     * @param wrap should the factory wrap when it reaches the maximum
     *  value that can be represented in <code>size</code> base 10 digits
     *  (or throw an exception)
     * @param size the size of the identifier, including prefix length
     * @throws IllegalArgumentException if size less prefix length is not at least one
     * @throws NullPointerException if prefix is <code>null</code>
     */
    public PrefixedLeftPaddedNumericGenerator(String prefix, boolean wrap, int size) {
        super();

        if (prefix == null) {
            throw new NullPointerException("prefix must not be null");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be at least one");
        }
        if (size <= prefix.length()) {
            throw new IllegalArgumentException("size less prefix length must be at least one");
        }
        this.wrap = wrap;
        this.prefix = prefix;

        int countLength = size - prefix.length();
        this.count = new char[countLength];
        for (int i = 0; i < countLength; i++) {
            count[i] = '0';
        }
    }


    /**
     * Return the prefix for this prefixed numeric generator.
     *
     * @return the prefix for this prefixed numeric generator
     */
    public String getPrefix() {
        return prefix;
    }

    public long maxLength() {
        return this.count.length + prefix.length();
    }

    public long minLength() {
        return this.count.length + prefix.length();
    }

    /**
     * Returns the (constant) size of the strings generated by this generator.
     *
     * @return the size of generated identifiers
     */
    public int getSize() {
        return this.count.length + prefix.length();
    }

    /**
     * Getter for property wrap.
     *
     * @return <code>true</code> if this generator is set up to wrap
     */
    public boolean isWrap() {
        return wrap;
    }

    /**
     * Setter for property wrap.
     *
     * @param wrap should the factory wrap when it reaches the maximum
     *  value that can be represented in <code>size</code> base 10 digits
     *  (or throw an exception)
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public String nextStringIdentifier() {
        for (int i = count.length - 1; i >= 0; i--) {
            switch (count[i]) {
                case NINE_CHAR:  // 9
                    count[i] = '0';
                    if (i == 0 && !wrap) {
                        throw new IllegalStateException
                        ("The maximum number of identifiers has been reached");
                    }
                    break;

                default:
                    count[i]++;
                    i = -1;
                    break;
            }
        }

        StringBuffer sb = new StringBuffer(prefix);
        sb.append(count);
        return sb.toString();
    }
}
