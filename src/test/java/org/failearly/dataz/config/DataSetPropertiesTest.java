/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.config;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataSetPropertiesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSetPropertiesTest.class);

    private static final String CUSTOM_SETUP_SUFFIX = "custom.dataz.setup";
    private static final String CUSTOM_CLEANUP_SUFFIX = "custom.dataz.cleanup";
    private static final String DATAZ_CORE_MODULE_RELATIVE_PATH = "/dataz-core";

    private static void createDatasetPropertiesFile() throws IOException {
        final File propertyFile = propertyFileObject();
        propertyFile.createNewFile();
        propertyFile.deleteOnExit();

        try (FileWriter fileWriter = new FileWriter(propertyFile, false)) {
            fileWriter.append(Constants.DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX + "=" + CUSTOM_SETUP_SUFFIX).append(System.lineSeparator());
            fileWriter.append(Constants.DATAZ_PROPERTY_DEFAULT_CLEANUP_SUFFIX + "=" + CUSTOM_CLEANUP_SUFFIX).append(System.lineSeparator());
        }
    }

    private static File propertyFileObject() {
        final String userDir = System.getProperty("user.dir");
        final String pathname;
        if (userDir.endsWith(DATAZ_CORE_MODULE_RELATIVE_PATH))
            pathname = userDir + "/build/resources/test" + Constants.DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE;
        else
            pathname = userDir + DATAZ_CORE_MODULE_RELATIVE_PATH + "/build/resources/test" + Constants.DATAZ_DEFAULT_CUSTOM_PROPERTY_FILE;

        LOGGER.info("Create or fetch file object '{}'", pathname);
        return new File(pathname);
    }

    @BeforeClass
    public static void setupProperties() throws Exception {
        createDatasetPropertiesFile();
        System.clearProperty(Constants.DATAZ_CONFIG_OPTION);
        DataSetProperties.reload();
    }

    @AfterClass
    public static void resetProperties() {
        propertyFileObject().delete();
        System.clearProperty(Constants.DATAZ_CONFIG_OPTION);
        DataSetProperties.reload();
    }

    @Test
    public void loadCustomPropertyFileFromClassPathFirst() throws Exception {
        // arrange / given
        final DataSetProperties.DataSetPropertiesImpl dataSetProperties = new DataSetProperties.DataSetPropertiesImpl();

        // act / when
        dataSetProperties.loadCustomPropertyFile(Constants.DATAZ_DEFAULT_PROPERTY_FILE);

        // assert / then
        assertThat("Setup Suffix?", dataSetProperties.getDefaultSetupSuffix(), is("setup"));
        assertThat("Cleanup Suffix?", dataSetProperties.getDefaultCleanupSuffix(), is("cleanup"));
        assertThat("Template Suffix?", dataSetProperties.getTemplateSuffix(), is(".vm"));
    }

    /*
     * Loading from "/dataz.properties"
     */
    @Test
    public void load_default_property_file() {
        // assert / then
        assertThat("Setup Suffix?", DataSetProperties.getDefaultSetupSuffix(), is(CUSTOM_SETUP_SUFFIX));
        assertThat("Cleanup Suffix?", DataSetProperties.getDefaultCleanupSuffix(), is(CUSTOM_CLEANUP_SUFFIX));
    }


    /*
     * Loading -Ddataset.config=...
     */
    @Test
    public void load_with_minus_D_option() throws Exception {
        // arrange / given
        final String propFile = createTempPropertyFile(Constants.DATAZ_PROPERTY_DEFAULT_SETUP_SUFFIX + "=mysetup.suffix");
        System.setProperty(Constants.DATAZ_CONFIG_OPTION, propFile);
        DataSetProperties.reload();

        // assert / then
        assertThat("Setup Suffix?", DataSetProperties.getDefaultSetupSuffix(), is("mysetup.suffix"));
        assertThat("Cleanup Suffix?", DataSetProperties.getDefaultCleanupSuffix(), is(CUSTOM_CLEANUP_SUFFIX));
    }

    @Test
    public void resolveReferences() throws Exception {
        // arrange / given
        DataSetProperties.setProperty("dataz.any.prop", "${user.home}/my/dir");

        // act / when
        final String propertyValue = DataSetProperties.getProperty("dataz.any.prop");

        // assert / then
        assertThat("Replace System property?", propertyValue, is(System.getProperty("user.home") + "/my/dir"));
    }

    private String createTempPropertyFile(String property) throws IOException {
        final File tempFile = File.createTempFile("dataz-", ".properties");
        tempFile.deleteOnExit();
        final FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(property.getBytes());
        outputStream.close();
        return tempFile.getPath();
    }

}