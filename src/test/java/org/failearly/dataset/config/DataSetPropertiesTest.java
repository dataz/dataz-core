/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 marko (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package org.failearly.dataset.config;

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

    private static final String CUSTOM_SETUP_SUFFIX = "custom.dataset.setup";
    private static final String CUSTOM_CLEANUP_SUFFIX = "custom.dataset.cleanup";

    private static void createDatasetPropertiesFile() throws IOException {
        final File propertyFile = dataSetPropertyFileObject();
        propertyFile.createNewFile();
        propertyFile.deleteOnExit();

        try (FileWriter fileWriter = new FileWriter(propertyFile, false)) {
            fileWriter.append(Constants.DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX + "=" + CUSTOM_SETUP_SUFFIX).append(System.lineSeparator());
            fileWriter.append(Constants.DATASET_PROPERTY_DEFAULT_CLEANUP_SUFFIX + "=" + CUSTOM_CLEANUP_SUFFIX).append(System.lineSeparator());
        }
    }

    private static File dataSetPropertyFileObject() {
        final String userDir = System.getProperty("user.dir");
        final String pathname;
        if (userDir.endsWith("/dataset"))
            pathname = userDir + "/build/resources/test" + Constants.DATASET_DEFAULT_CUSTOM_PROPERTY_FILE;
        else
            pathname = userDir + "/dataset/build/resources/test" + Constants.DATASET_DEFAULT_CUSTOM_PROPERTY_FILE;

        LOGGER.debug("Create or fetch file object '{}'", pathname);
        return new File(pathname);
    }

    @BeforeClass
    public static void setupProperties() throws Exception {
        createDatasetPropertiesFile();
        System.clearProperty(Constants.DATASET_CONFIG_OPTION);
        DataSetProperties.reload();
    }

    @AfterClass
    public static void resetProperties() {
        dataSetPropertyFileObject().delete();
        System.clearProperty(Constants.DATASET_CONFIG_OPTION);
        DataSetProperties.reload();
    }

    @Test
    public void loadCustomPropertyFileFromClassPathFirst() throws Exception {
        // arrange / given
        final DataSetProperties.DataSetPropertiesImpl dataSetProperties = new DataSetProperties.DataSetPropertiesImpl();

        // act / when
        dataSetProperties.loadCustomPropertyFile(Constants.DATASET_DEFAULT_PROPERTY_FILE);

        // assert / then
        assertThat("Setup Suffix?", dataSetProperties.getDefaultSetupSuffix(), is("setup"));
        assertThat("Cleanup Suffix?", dataSetProperties.getDefaultCleanupSuffix(), is("cleanup"));
        assertThat("Template Suffix?", dataSetProperties.getTemplateSuffix(), is(".vm"));
    }

    /*
     * Loading from "/dataset.properties"
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
        final String propFile = createTempPropertyFile(Constants.DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX + "=mysetup.suffix");
        System.setProperty(Constants.DATASET_CONFIG_OPTION, propFile);
        DataSetProperties.reload();

        // assert / then
        assertThat("Setup Suffix?", DataSetProperties.getDefaultSetupSuffix(), is("mysetup.suffix"));
        assertThat("Cleanup Suffix?", DataSetProperties.getDefaultCleanupSuffix(), is(CUSTOM_CLEANUP_SUFFIX));
    }

    @Test
    public void resolveReferences() throws Exception {
        // arrange / given
        DataSetProperties.setProperty("dataset.any.prop", "${user.home}/my/dir");

        // act / when
        DataSetProperties.resolveReferences();

        // assert / then
        assertThat("Replace System property?", DataSetProperties.getProperty("dataset.any.prop"), is(System.getProperty("user.home") + "/my/dir"));
    }

//    @Test
//    public void getGeneratorTraversingStrategy_default_strategy() throws Exception {
//        assertThat("Default strategy?", DataSetProperties.getGeneratorTraversingStrategy(), is(TraverseStrategy.TOP_DOWN));
//    }
//
//    @Test
//    public void getGeneratorTraversingStrategy_bottom_up_strategy() throws Exception {
//        // arrange / given
//        DataSetProperties.setProperty(Constants.DATASET_GENERATOR_TRAVERSING_STRATEGY, "BOTTOM_UP");
//
//        // assert / then
//        assertThat("Custom strategy?", DataSetProperties.getGeneratorTraversingStrategy(), is(TraverseStrategy.BOTTOM_UP));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void getGeneratorTraversingStrategy_invalid_strategy() throws Exception {
//        // arrange / given
//        DataSetProperties.setProperty(Constants.DATASET_GENERATOR_TRAVERSING_STRATEGY, "NONE_EXISTING_STRATEGY");
//
//        // act / when
//        DataSetProperties.getGeneratorTraversingStrategy();
//
//        // assert / then
//    }

//    @Test
//    public void getGeneratorTraversingStrategy_missing_strategy() throws Exception {
//        // arrange / given
//        DataSetProperties.setProperty(Constants.DATASET_TEMPLATE_OBJECT_TRAVERSING_DEPTH, null);
//
//        // assert / then
//        assertThat("Missing use default strategy?", DataSetProperties.getGeneratorTraversingStrategy(), is(TraverseStrategy.TOP_DOWN));
//    }
//

    private String createTempPropertyFile(String property) throws IOException {
        final File tempFile = File.createTempFile("dataset-", ".properties");
        tempFile.deleteOnExit();
        final FileOutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(property.getBytes());
        outputStream.close();
        return tempFile.getPath();
    }

}