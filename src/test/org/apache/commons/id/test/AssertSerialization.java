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
package org.apache.commons.id.test;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;


/**
 * {@link Assert} class for serialization operations.
 * <p>
 * Note: This class is intented to be moved to a commons-test component later.
 * </p>
 * 
 * @author J&ouml;rg Schaible
 * @since 1.0
 */
public class AssertSerialization {

    /**
     * Assert an object to be {@linkplain Serializable}.
     * 
     * @param serializable object to serialize
     * @return the serialized object
     * @since 1.0
     */
    public static Serializable assertSerializable(final Serializable serializable) {
        return assertSerializable(null, serializable);
    }

    /**
     * Assert an object to be {@linkplain Serializable}.
     * 
     * @param message the error message
     * @param serializable object to serialize
     * @return the serialized object
     * @since 1.0
     */
    public static Serializable assertSerializable(
            final String message, final Serializable serializable) {
        return assertSerializable(message, serializable, serializable.getClass());
    }

    /**
     * Assert an object to be {@linkplain Serializable}.
     * 
     * @param serializable object to serialize
     * @param expectedType the expected type of the result
     * @return the serialized object
     * @since 1.0
     */
    public static Serializable assertSerializable(
            final Serializable serializable, final Class expectedType) {
        return assertSerializable(null, serializable, expectedType);
    }

    /**
     * Assert an object to be {@linkplain Serializable}.
     * 
     * @param message the error message
     * @param serializable object to serialize
     * @param expectedType the expected type of the result
     * @return the serialized object
     * @since 1.0
     */
    public static Serializable assertSerializable(
            String message, final Serializable serializable, final Class expectedType) {
        message = message != null ? message + ": " : "";
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return assertSerializable(baos, message, serializable, expectedType);
    }

    /**
     * Assert an object to be {@linkplain Serializable}.
     * 
     * @param baos the {@link ByteArrayOutputStream} with the serialized reference object
     * @param message the error message
     * @param serializable object to serialize
     * @param expectedType the expected type of the result
     * @return the serialized object
     * @since 1.0
     */
    private static Serializable assertSerializable(
            final ByteArrayOutputStream baos, String message, final Serializable serializable,
            final Class expectedType) {
        message = message != null ? message + ": " : "";
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(serializable);
            oos.close();

            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final ObjectInputStream ois = new ObjectInputStream(bais);
            final Serializable serialized = (Serializable)ois.readObject();
            ois.close();
            Assert.assertNotNull(message + "Serialized object is null", serialized);
            Assert.assertSame(
                    message + "Serialized object has wrong type", serialized.getClass(),
                    expectedType);
            return serialized;
        } catch (final Exception e) {
            throw new AssertionFailedError(message + e.getMessage());
        }
    }

    /**
     * Interface for the serialization test context.
     * 
     * @author J&ouml;rg Schaible
     * @since 1.0
     */
    public static interface Context {

        /**
         * Verify the (de-)serialized object. Test the object with appropriate assert methods and
         * try to check all members, especially newly added.
         * 
         * @param serialized the object itself
         * @param uid the <code>serialVersionUID</code> of the deserialized object
         * @since 1.0
         */
        void verify(Object serialized, long uid);

        /**
         * Create a reference object of the class to serialize.
         * 
         * @return the reference object
         * @since 1.0
         */
        Serializable createReference();

        /**
         * Retrieve the base directory for the serialized object files. Each type has an own
         * directory that contains files with serialized compatible object of that type, but
         * different <code>serialVersionUID</code>. This id is used for the filename.
         * 
         * @return the base directory
         * @since 1.0
         */
        File getReferenceBaseDir();

        /**
         * Retrieve the type of the serialized class.
         * 
         * @return the type
         * @since 1.0
         */
        Class getType();
    }

    /**
     * A test case used to test the serialization capability and the compatibility with previous
     * versions of the type. Instances of the class are internally generated.
     * 
     * @author J&ouml;rg Schaible
     * @since 1.0
     */
    protected static class SerializationTest extends TestCase {
        private final Context context;
        private final ByteArrayOutputStream baos;

        private SerializationTest(final String name, final Context context) {
            super(name);
            this.context = context;
            this.baos = new ByteArrayOutputStream();
        }

        /**
         * Test the serializability of the object.
         */
        public void testSerialization() {
            final Class type = context.getType();
            final ObjectStreamClass osc = ObjectStreamClass.lookup(type);
            assertNotNull(type.getName() + " is not serializable", osc);
            final Serializable reference = context.createReference();
            assertNotNull("No reference object for " + type.getName() + " retrieved", reference);
            final Serializable serialized = assertSerializable(baos, "", reference, type);
            context.verify(serialized, osc.getSerialVersionUID());

            createReferenceFile(osc);
        }

        private void createReferenceFile(final ObjectStreamClass osc) {
            final File referenceDir = context.getReferenceBaseDir();
            final File referenceFile = new File(new File(referenceDir, osc.getName()), Long
                    .toString(osc.getSerialVersionUID())
                    + ".ser");
            if (!referenceFile.isFile()) {
                if (!referenceDir.isDirectory()) {
                    throw new AssertionFailedError("Missing serialization reference directory: "
                            + referenceDir.getAbsolutePath());
                }
                if (!referenceFile.getParentFile().isDirectory()) {
                    referenceFile.getParentFile().mkdir();
                }
                try {
                    try {
                        final FileOutputStream out = new FileOutputStream(referenceFile);
                        try {
                            out.write(baos.toByteArray());
                        } catch (final IOException e) {
                            throw new AssertionFailedError("Cannot write missing reference file "
                                    + referenceFile.getAbsolutePath()
                                    + ". "
                                    + e.getMessage());
                        } finally {
                            out.close();
                        }
                    } catch (final FileNotFoundException e) {
                        throw new AssertionFailedError("Cannot create missing reference file "
                                + referenceFile.getAbsolutePath()
                                + ". "
                                + e.getMessage());
                    } catch (final IOException e) {
                        throw new AssertionFailedError("Cannot close missing reference file "
                                + referenceFile.getAbsolutePath()
                                + ". "
                                + e.getMessage());
                    }

                } catch (final AssertionFailedError e) {
                    if (referenceFile.exists()) {
                        referenceFile.delete();
                    }
                    throw e;
                }
                throw new AssertionFailedError("Missing reference file "
                        + referenceFile.getAbsolutePath()
                        + " created.");
            }
        }

        /**
         * Ensure that serialized reference is still the same.
         * 
         * @throws IOException
         * @since 1.0
         */
        public void testBinaryRepresentationIsStillValid() throws IOException {
            final Class type = context.getType();
            final ObjectStreamClass osc = ObjectStreamClass.lookup(type);
            final Serializable reference = context.createReference();
            assertNotNull("No reference object for " + type.getName() + " retrieved", reference);
            assertSerializable(baos, "", reference, type);
            final File referenceDir = context.getReferenceBaseDir();
            final File referenceFile = new File(new File(referenceDir, osc.getName()), Long
                    .toString(osc.getSerialVersionUID())
                    + ".ser");
            final InputStream expected = new FileInputStream(referenceFile);
            try {
                AssertIO.assertInputStreamEquals(
                        "Serialization of "
                                + type.getName()
                                + " has changed regarding the reference", expected,
                        new ByteArrayInputStream(baos.toByteArray()));
            } finally {
                expected.close();
            }
        }

        /**
         * Test the compatibility with a version of the type.
         * 
         * @param filename the filename of the file with the serialized object
         */
        public void checkCompatibility(final String filename) {
            final Class type = context.getType();
            final ObjectStreamClass osc = ObjectStreamClass.lookup(type);
            assertNotNull(type.getName() + " is not serializable", osc);

            final File referenceDir = new File(context.getReferenceBaseDir(), type.getName());
            if (referenceDir.isDirectory()) {
                final File file = new File(referenceDir, filename);
                try {
                    final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    final Serializable serialized = (Serializable)ois.readObject();
                    context.verify(serialized, osc.getSerialVersionUID());
                } catch (final FileNotFoundException e) {
                    throw new AssertionFailedError("Missing reference file "
                            + file.getAbsolutePath()
                            + ". "
                            + e.getMessage());
                } catch (final InvalidClassException e) {
                    throw new AssertionFailedError("Cannot load object from reference file "
                            + file.getAbsolutePath()
                            + ". Adjust class implementation. "
                            + e.getMessage());
                } catch (final IOException e) {
                    throw new AssertionFailedError("Cannot read from reference file "
                            + file.getAbsolutePath()
                            + ". "
                            + e.getMessage());
                } catch (final ClassNotFoundException e) {
                    throw new AssertionFailedError("Class of reference file "
                            + file.getAbsolutePath()
                            + " not found. "
                            + e.getMessage());
                }
            }
        }

        protected void runTest() throws Throwable {
            final String name = getName();
            if (name.endsWith(".ser")) {
                checkCompatibility(name);
            } else {
                super.runTest();
            }
        }
    }

    /**
     * Create a {@link TestSuite} for the serializability of a type. A caller must provide a proper
     * {@link Context}, that enables the suite to test the serializability of the provided type and
     * ensures backward compatibility for older versions of the type. For each type version a file
     * is provided with the <code>serialVersionUID</code> as filename in a directory named like
     * the type to test. If no reference file is stored in the reference DB, the test fails, but the
     * file is created automatically, so the next run will succeed.
     * 
     * @param context the test Context
     * @return the TestSuite with the tests
     * @since 1.0
     */
    public static TestSuite createSerializationTestSuite(final Context context) {
        final TestSuite suite = new TestSuite("Serialization Tests");
        suite.addTest(new SerializationTest("testSerialization", context));
        suite.addTest(new SerializationTest("testBinaryRepresentationIsStillValid", context));

        final Class type = context.getType();
        final File referenceDir = new File(context.getReferenceBaseDir(), type.getName());
        if (referenceDir.isDirectory()) {
            final File[] files = referenceDir.listFiles(new FilenameFilter() {
                public boolean accept(final File dir, final String name) {
                    return name.endsWith(".ser");
                }
            });
            for (int i = 0; i < files.length; i++) {
                suite.addTest(new SerializationTest(files[i].getName(), context));
            }
        }

        return suite;
    }
}
