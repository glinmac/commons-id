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
package org.apache.commons.id.uuid.task;

import junit.framework.TestCase;

import org.apache.commons.id.uuid.UUID;
import org.apache.commons.id.uuid.state.ReadOnlyResourceStateImpl;
import org.apache.commons.id.uuid.task.UUIDTask.UUIDVersion;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.util.Properties;

/**
 * @author Commons-Id team
 */
public class UUIDTaskTest extends TestCase {

    private Project project;
    private UUIDVersion uuidVersion;

    protected void setUp() throws Exception {
        super.setUp();
        project = new Project();
        uuidVersion = new UUIDTask.UUIDVersion();
    }

    public void testVersion1() {
        final Properties sysProps = new Properties(System.getProperties());
        try {
            System.setProperty(ReadOnlyResourceStateImpl.CONFIG_FILENAME_KEY, "uuid1.state");
            uuidVersion.setValue("VERSION_ONE");
            final UUIDTask uuidTask = new UUIDTask();
            uuidTask.setProject(project);
            uuidTask.setVersion(uuidVersion);
            uuidTask.execute();
            final UUID uuid = new UUID(uuidTask.getProject().getProperty("uuid"));
            assertEquals(UUID.VERSION_ONE, uuid.version());
        } finally {
            System.setProperties(sysProps);
        }
    }

    public void testVersion2() {
        try {
            uuidVersion.setValue("VERSION_TWO");
            final UUIDTask uuidTask = new UUIDTask();
            uuidTask.setProject(project);
            uuidTask.setVersion(uuidVersion);
            fail("Thrown " + BuildException.class.getName() + " expected");
        } catch (final BuildException e) {
            assertTrue(e.getMessage().indexOf("VERSION_TWO") >= 0);
        }
    }

    public void testVersion3() {
        final UUID namespace = new UUID();
        uuidVersion.setValue("VERSION_THREE");
        final UUIDTask uuidTask = new UUIDTask();
        uuidTask.setProject(project);
        uuidTask.setVersion(uuidVersion);
        uuidTask.setNamespace(namespace.toUrn());
        uuidTask.setName("jakarta.apache.org");
        uuidTask.execute();
        final UUID uuid = new UUID(uuidTask.getProject().getProperty("uuid"));
        assertEquals(UUID.VERSION_THREE, uuid.version());
        assertEquals(UUID.nameUUIDFromString("jakarta.apache.org", namespace, UUID.MD5_ENCODING), uuid);
    }

    public void testVersion4() {
        uuidVersion.setValue("VERSION_FOUR");
        final UUIDTask uuidTask = new UUIDTask();
        uuidTask.setProject(project);
        uuidTask.setVersion(uuidVersion);
        uuidTask.execute();
        final UUID uuid = new UUID(uuidTask.getProject().getProperty("uuid"));
        assertEquals(UUID.VERSION_FOUR, uuid.version());
    }

    public void testVersion5() {
        final UUID namespace = new UUID();
        uuidVersion.setValue("VERSION_FIVE");
        final UUIDTask uuidTask = new UUIDTask();
        uuidTask.setProject(project);
        uuidTask.setVersion(uuidVersion);
        uuidTask.setNamespace(namespace.toUrn());
        uuidTask.setName("jakarta.apache.org");
        uuidTask.execute();
        final UUID uuid = new UUID(uuidTask.getProject().getProperty("uuid"));
        assertEquals(UUID.VERSION_FIVE, uuid.version());
        assertEquals(UUID.nameUUIDFromString("jakarta.apache.org", namespace, UUID.SHA1_ENCODING), uuid);
    }
}
