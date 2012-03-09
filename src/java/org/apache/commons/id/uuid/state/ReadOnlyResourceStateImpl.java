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
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>The <code>ReadOnlyResourceStateImpl</code> is an implementation of the
 * <code>State</code> interface. This implementation provides better guarantees
 * that no duplicate UUID's will be generated; however since the only stateful
 * information provided is the IEEE 802 address the generator should use a
 * better choice is to use an implementation that also writes to persistent
 * storage each time the state is loaded a new clock sequence is used. If the
 * system time is adjusted backwards there is a possibility that a UUID generated
 * with the same clock sequence and time could be generated.
 *
 * @author Commons-Id team
 * @version $Id: ReadOnlyResourceStateImpl.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class ReadOnlyResourceStateImpl implements State {

    /** How often to write to stable storage - since this is read-only make it largest. */
    static long synchronizeInterval = Long.MAX_VALUE;

    /** Collection of nodes to load or store. */
    static HashSet nodes = new HashSet();

    /**
     * The key to use in locating the uuid configuration xml file from System
     * properties.
     */
    public static final String CONFIG_FILENAME_KEY = "org.apache.commons.id.uuid.config.resource.filename";

    /**
     * <p>Constructs a ReadOnlyResouceStateImpl.</p>
     */
    public ReadOnlyResourceStateImpl() {
        super();
    }

    /**
     * <p>Loads the System.property &quot;commons.uuid.configFileName&quot;
     * (default is &quot;uuid.conf&quot;) using commons.discovery.</p>
     * <p>
     * The uuid-[n].conf file is an xml file with the following syntax:<br>
     * <pre>
     * <?xml version="1.0" encoding="UTF-8" ?>
     * <!DOCTYPE uuidstate [
     * <!ELEMENT uuidstate (node*)>
     * <!ELEMENT node EMPTY>
     * <!ATTLIST node id ID #REQUIRED>
     * <!ATTLIST node clocksequence CDATA #IMPLIED>
     * <!ATTLIST node lasttimestamp CDATA #IMPLIED>
     * ]>
     * <uuidstate>
     * <node id="XX-XX-XX-XX-XX-XX" />
     * <node id="YY-YY-YY-YY-YY-YY" />
     * </uuidstate>
     * </pre>
     * </p><p>See the documentation for further information on configuration
     * tasks.</p>
     *
     * @throws IllegalStateException if the &quot;commons.uuid.configFileName&quot;
     * system property is not set or the resource cannot be loaded.
     * @throws SAXException if an xml parsing error occurs
     * @throws ParserConfigurationException if the parser cannot be loaded
     * @throws IOException if an error occurs reading the file
     * 
     * @see org.apache.commons.id.uuid.state.State#load()
     */
    public void load() throws Exception {
        // Get the resource name
        String resourceName = System.getProperty(CONFIG_FILENAME_KEY);
        if (resourceName == null) {
            throw new IllegalStateException("No value set for system property: " 
                    + CONFIG_FILENAME_KEY);
        }

        // Load the resource
        InputStream in = null;
        try {
            in = ClassLoader.getSystemResourceAsStream(resourceName);
            if (in == null) {
                throw new IllegalStateException(resourceName + 
                        " loaded as system resource is null");
            }
            //Do the XML parsing
            parse(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    //Nothing to do at this point.
                }
            }
        }
    }

    /**
     * @see State#getSynchInterval
     */
    public long getSynchInterval() {
        //Return Long.MAX_VALUE since this is readonly.
        return Long.MAX_VALUE;
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#getNodes()
     */
    public Set getNodes() {
        return nodes;
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#store(java.util.Set)
     */
    public void store(Set nodeSet) throws IOException {
        // Nothing to do - this is a ReadOnly implementation.
        return;
    }

    /**
     * @see org.apache.commons.id.uuid.state.State#store(java.util.Set, long)
     */
    public void store(Set nodeSet, long timestamp) {
        // Nothing to do - this is a ReadOnly implementation.
        return;
    }

    /**
     * <p>Parses the XML configuration into the <code>Node</code>s and places
     * into this instances node collection.</p>
     *
     * @param in the XML input stream to parse.
     */
    protected void parse(InputStream in) throws Exception {
        DefaultHandler handler = new StateConfigHandler();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(true);
        SAXParser parser = saxFactory.newSAXParser();
        parser.parse(in, handler);
    }
   
    //--------------------------------------------------------------------------
    /**
     * Inner class to handle document processing of the configuration file.
     */
    class StateConfigHandler extends DefaultHandler {
        /** Constant for the uuidstate tag */
        static final short UUID_STATE_TAG = 1;
        /** Constant string value for uuidstate tag */
        static final String UUID_STATE_TAG_STR = "uuidstate";
        /** Constant string value for the synchinterval attribute */
        static final String SYNCH_INTERVAL_STR = "synchinterval";
        /** Constant for the node tag */
        static final short NODE_TAG = 2;
        /** Constant string value for the node tag */
        static final String NODE_TAG_STR = "node";
        /** Constant string value for the id attribute */
        static final String ATTR_ID_STR = "id";
        /** Constant string value for the last clock sequence attribute */
        static final String ATTR_CLOCKSEQ_STR = "clocksequence";
        /** Constant string value for the last time stamp attribute */
        static final String ATTR_LASTIMESTAMP_STR = "timestamp";

        /**
         * Handle start of tag.
         * @see org.xml.sax.helpers.DefaultHandler#startElement(String, String, String, Attributes)
         */
        public void startElement(
            String namespaceURI,
            String simpleName,
            String qualifiedName,
            Attributes attributes)
            throws SAXException {

            short currentTag = 0;

            String element = simpleName;
            if ("".equals(simpleName)) {
                element = qualifiedName;
            }
            if (element.equalsIgnoreCase(UUID_STATE_TAG_STR)) {
                currentTag = UUID_STATE_TAG;
            } else if (element.equalsIgnoreCase(NODE_TAG_STR)) {
                currentTag = NODE_TAG;
            }
            //Process attributes
            if (attributes != null) {
                switch (currentTag) {
                    case 1 :
                        processBodyTag(attributes);
                        break;
                    case 2 :
                        processNodeTag(attributes);
                        break;
                    default :
                        break;
                }
            }
        }

        /**
         * <p>Processes the main body tag of document.</p>
         *
         * @param attributes - sax Attributes to process.
         */
        private void processBodyTag(Attributes attributes) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attributeName = attributes.getLocalName(i);
                if ("".equals(attributeName)) {
                    attributeName = attributes.getQName(i);
                }
                String attributeValue = attributes.getValue(i);
                if (attributeName.equalsIgnoreCase(SYNCH_INTERVAL_STR)) {
                    try {
                        synchronizeInterval = Long.parseLong(attributeValue);
                    } catch (NumberFormatException nfe) {
                        synchronizeInterval = 0;
                    }
                }
            }
        }

        /**
         * <p>Process a node tag</p>
         *
         * @param attributes - sax Attributes to process.
         */
        private void processNodeTag(Attributes attributes) {
            byte[] node = null;
            long lastTS = 0;
            short lastClockSeq = 0;
            for (int i = 0; i < attributes.getLength(); i++) {
                String attributeName = attributes.getLocalName(i);
                if ("".equals(attributeName)) {
                    attributeName = attributes.getQName(i);
                }
                String attributeValue = attributes.getValue(i);

                if (attributeName.equalsIgnoreCase(ATTR_ID_STR)) {
                    node = StateHelper.decodeMACAddress(attributeValue);
                } else if (attributeName.equalsIgnoreCase(ATTR_CLOCKSEQ_STR)) {
                    try {
                        lastClockSeq = Short.parseShort(attributeValue);
                    } catch (NumberFormatException nfe) {
                        lastClockSeq = 0;
                    }
                } else if ( attributeName.equalsIgnoreCase(ATTR_LASTIMESTAMP_STR)) {
                    try {
                        lastTS = Long.parseLong(attributeValue);
                    } catch (NumberFormatException nfe) {
                        lastTS = 0;
                    }
                }
            }
            if (node != null) {
                if (lastClockSeq != 0) {
                    nodes.add(new Node(node, lastTS, lastClockSeq));
                } else {
                    nodes.add(new Node(node));
                }
            }
        }
    }
    //--------------------------------------------------------------------------
}
