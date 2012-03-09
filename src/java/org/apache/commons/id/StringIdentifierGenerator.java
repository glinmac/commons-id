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

/**
 * <code>StringIdentifierGenerator</code> defines a simple interface for
 * String based identifier generation.
 *
 * @author Commons-Id team
 * @version $Id: StringIdentifierGenerator.java 480488 2006-11-29 08:57:26Z bayard $
 */
public interface StringIdentifierGenerator extends IdentifierGenerator {

    /**
     * Constant representing unlimited identifier length, returned by {@link #maxLength()}
     * when there is no upper bound to the length of an identifier in the sequence
     */
    public static final int INFINITE_MAX_LENGTH = -1;

    /**
     * Gets the next identifier in the sequence.
     *
     * @return the next String identifier in sequence
     */
    String nextStringIdentifier();

    /**
     * Returns the maximum length (number or characters) for an identifier
     * from this sequence.
     *
     * @return the maximum identifier length, or {@link #INFINITE_MAX_LENGTH} if there is no upper bound
     */
    long maxLength();

    /**
     * Returns the minimum length (number of characters) for an identifier
     * from this sequence.
     *
     * @return the minimum identifier length
     */
    long minLength();
}
