/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.id;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Identifier generator that concatenates the results of a list of string
 * identifier generators.
 * 
 * @since 1.0
 * @version $Revision$ $Date$
 * 
 */
public class CompositeIdentifierGenerator extends AbstractStringIdentifierGenerator implements Serializable {

    /**
     * <code>serialVersionUID</code> is the serializable UID for the binary version of the class.
     */
    private static final long serialVersionUID = 20060206L;
    
    /** The identifier generators to concatenate */
    private final StringIdentifierGenerator[] identifierGenerators;

    /**
     * Factory method to create a new <code>CompositeIdentifierGenerator</code>
     * from an input array of <code>StringIdentifierGenerator</code> instances.
     * <p>
     * The input array is (shallow) copied - i.e., the object references in the
     * input array are copied into a new array used internally.  The array is
     * expected to be non-empty and not to contain nulls.
     * 
     * @param generators the identifiers to concatenate, copied by reference
     * @return the composite identifier generator
     * @throws IllegalArgumentException if the generators array is null
     * @throws IllegalArgumentException if any generator in the array is null
     */
    public static StringIdentifierGenerator getInstance(
            StringIdentifierGenerator[] generators) {
        if (generators == null) {
            throw new IllegalArgumentException(
                    "Generator array must not be null");
        }
        if (generators.length == 0) {
            throw new IllegalArgumentException(
                    "Generator array must not be empty");
        }
        StringIdentifierGenerator[] generatorsCopy = 
            new StringIdentifierGenerator[generators.length];
        for (int i = 0; i < generators.length; i++) {
            if (generators[i] == null) {
                throw new IllegalArgumentException(
                        "Generators must not be null");
            }
            generatorsCopy[i] = generators[i];
        }  
        return new CompositeIdentifierGenerator(generatorsCopy);
    }

    /**
     * Create a new <code>CompositeIdentifierGenerator</code> that concatenates
     * the results of the provided collection of generators. Order is
     * determined by the <code>iterator()</code> method on the collection.
     * 
     * @param generators a collection of string identifier generators to
     * concatenate
     * @return the composite identifier generator
     * @throws IllegalArgumentException if the generators collection is null,
     * empty, or contains nulls
     */
    public static StringIdentifierGenerator getInstance(Collection generators) {
        if (generators == null) {
            throw new IllegalArgumentException(
                    "Generator collection must not be null");
        }
        if (generators.size() == 0) {
            throw new IllegalArgumentException(
                    "Generator collection must not be empty");
        }
        StringIdentifierGenerator[] generatorsCopy = 
            new StringIdentifierGenerator[generators.size()];
        int i = 0;
        Iterator it = generators.iterator();
        while (it.hasNext()) {
            generatorsCopy[i] = (StringIdentifierGenerator) it.next();
            if (generatorsCopy[i] == null) {
                throw new IllegalArgumentException(
                        "Generators must not be null");
            }
            i++;
        }
        return new CompositeIdentifierGenerator(generatorsCopy);
    }

    
    /**
     * Constructor that does not check for nulls. Use 
     * {@link #getInstance(StringIdentifierGenerator[])}
     * to validate the input array.
     * 
     * @param identifierGenerators the identifier generators to concatenate
     */
    public CompositeIdentifierGenerator(
            StringIdentifierGenerator[] identifierGenerators) {
        super();
        this.identifierGenerators = identifierGenerators;
    }

    public String nextStringIdentifier() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < identifierGenerators.length; i++) {
            buffer.append(identifierGenerators[i].nextStringIdentifier());
        }
        return buffer.toString();
    }

    public long maxLength() {
        long length = 0;
        for (int i = 0; i < identifierGenerators.length; i++) {
            length += identifierGenerators[i].maxLength();
        }
        return length;
    }

    public long minLength() {
        long length = 0;
        for (int i = 0; i < identifierGenerators.length; i++) {
            length += identifierGenerators[i].minLength();
        }
        return length;
    }

    /**
     * Returns a (shallow) copy of the array of identifier generators
     * concatenated by this generator.
     * 
     * @return the identifier generators
     */
    public StringIdentifierGenerator[] getIdentifierGenerators() {
        int len = identifierGenerators.length;
        StringIdentifierGenerator[] out = 
            new StringIdentifierGenerator[len];
        System.arraycopy(identifierGenerators, 0, out, 0, len);
        return out;
    }

}
