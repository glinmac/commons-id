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
import java.io.Serializable;
import java.util.Set;


/**
 * <p>The <code>State</code> interface. Implementors are responsible for storing
 * UUIDGenerator stateful information. Each node contains the last timestamp,
 * the last clock sequence used, and the node identifier bytes. The implementor
 * should be prepared to load, store, and return the <code>Node</code>s from
 * some stateful storage.</p>
 *
 * @author Commons-Id team
 * @version $Id: State.java 480488 2006-11-29 08:57:26Z bayard $
 */
public interface State extends Serializable {

    /** The default <code>State</code> implementation class name if not configured. */
    String DEFAULT_STATE_IMPL = "org.apache.commons.id.uuid.state.ReadOnlyResourceStateImpl";

    /**
     * <p>Method loads the Systems node identifier information usually from some
     * stateful storage area.</p>
     * <p><b>Note<b> each JVM instance should have a separate configuration since
     * a system wide Mutex is not feasible, then in a system with multiple
     * concurrent jvm instances running - each instance should utilize a different
     * configuration with <b>distinct</b> node identifiers.</p>
     *
     * @throws IllegalStateException likely the system is not configured or the state of the system is incorrect.
     */
    void load(  ) throws Exception;

    /**
     * <p>Returns the collection of <code>Nodes</code> for this uuid state
     * information.</p>
     *
     * @return the collection of <code>Nodes</code> for this uuid state.
     */
    Set getNodes(  );

    /**
     * <p>Stores the state information.</p>
     *
     * <p>To increase the quality of the UUID uniqueness, a system that may
     * write to stable storage SHOULD perist the state of the UUIDGenerator in
     * order to reduce the likelihood of duplicate identifiers and increase the
     * quality of the identifiers generated.</p>
     *
     * @param nodes a Collection of <code>Node</code>s to store.
     * @throws IOException - an input/output Exception perhaps due to StringBuffer#append
     */
    void store( Set nodes ) throws IOException;

    /**
     * <p>Stores the state information using a predetermined timestamp. This timestamp should be a time in the future but a shorter
     * period than what the java virtual machine can restart within. This is normally linked to the sychInterval.</p>
     * <p>Using a point in time in the near future reduces unnecessary I/O, while allowing for the persistent state to be stored
     * periodically, in an efficient manner.</p>
     *
     * @param nodes a Collection of <code>Node</code>s to store.
     * @param timestamp the timestamp to write all <code>Node</code>s last timestamp as.
     */
    void store( Set nodes, long timestamp );

    /**
     * <p>Returns the number of milliseconds to wait between writing to stable storage.</p>
     *
     * @return the number of milliseconds to wait between writing to stable storage.
     */
    long getSynchInterval();
}
