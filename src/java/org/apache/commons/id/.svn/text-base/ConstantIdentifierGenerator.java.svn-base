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
 * <code>StringIdentifierGenerator</code> that always returns the same
 * string.  Use with {@link CompositeIdentifierGenerator} to embed constant
 * string identifiers, or to add prefixes or suffixes.
 * <p> 
 * Null constant values are not allowed. The default
 * (assumed by the argumentless constructor) is an empty string.</p>
 * 
 * @author Commons-Id team
 * @version $Id$
 */
public class ConstantIdentifierGenerator extends 
    AbstractStringIdentifierGenerator {

    /** 
     * Constant string value always returned by this generator.
     */
    private final String identifier;

    /**
     * Factory method to create a new <code>ConstantIdentifierGenerator</code>
     * with the given constant value.  Does not allow null values.
     * 
     * @param identifier constant value returned by the newly created generator.
     *      Must not be null.
     * @return a new ConstantIdentifierGenerator
     * @throws IllegalArgumentException if identifier is null
     */
    public static StringIdentifierGenerator getInstance(String identifier) {
        return new ConstantIdentifierGenerator(identifier);
    }
    
    /**
     * Creates a new ConstantIdentifierGenerator using the 
     * default (empty string) constant value.
     * 
     */
    public ConstantIdentifierGenerator() {
        super();
        this.identifier = "";
    }
    
    /**
     * Creates a new ConstantIdentifierGenerator using the
     * given constant value.
     * <p> 
     * Null constant values are not allowed.</p>
     * 
     * @param identifier constant value returned by the generator.  Must not
     *   be null.
     * @throws IllegalArgumentExeption if identifier is null
     */
    public ConstantIdentifierGenerator(String identifier) {
        super();
        if (identifier == null) {
            throw new IllegalArgumentException
                ("Constant identifier value must not be null");
        }
        this.identifier = identifier;
    }
    
    public String nextStringIdentifier() {
        return identifier;
    }
    
    /**
     * Returns the length of the constant string returned by this generator.
     * If the constant is null or an empty string, 0 is returned.
     *
     * @return the length of the constant string returned by this generator
     */
    public long maxLength() {
       if (identifier == null) {
           return 0;
       } else {
           return identifier.length();
       }
    }
    
    /**
     * Returns the length of the constant string returned by this generator.
     * If the constant is null or an empty string, 0 is returned.
     *
     * @return the length of the constant string returned by this generator
     */
    public long minLength() {
        return maxLength();
    }     
}
