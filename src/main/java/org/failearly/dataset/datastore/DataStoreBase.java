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
package org.failearly.dataset.datastore;

import org.apache.commons.lang.StringUtils;
import org.failearly.dataset.DataStoreSetup;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.internal.util.ResourceUtils;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourceBuilder;
import org.failearly.common.test.ExtendedProperties;
import org.failearly.common.test.With;
import org.failearly.common.test.mb.MessageBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * DataStoreBase is the base class for the actually DataStore implementation. There are some useful abstractions like:
 * <br><br>
 * <ul>
 * <li>{@link org.failearly.dataset.datastore.support.TransactionalSupportDataStoreBase}: provides an implementation for
 * transactional behaviour.</li>
 * <li>{@link org.failearly.dataset.datastore.support.SimpleFileTransactionalSupportDataStoreBase}: extends the
 * transactional resource and
 * add {@link org.failearly.dataset.simplefile.SimpleFileParser} to it.
 * </li>
 * </ul>
 */
public abstract class DataStoreBase extends AbstractDataStore {
    protected static final With with=With.create((description, exception) -> {
        throw new DataStoreException(description, exception);
    }, "standard-datastore-exception");

    private String id="<unknown>";
    private String configFile="<unknown>";

    private String setupSuffix=Constants.DATASET_USE_DEFAULT_SUFFIX;
    private String cleanupSuffix=Constants.DATASET_USE_DEFAULT_SUFFIX;

    private ExtendedProperties properties;
    private final List<DataResource> cleanupDataStoreResources=new LinkedList<>();

    protected DataStoreBase() {
    }

    protected DataStoreBase(String dataStoreId, String dataStoreConfigFile) {
        this.configFile=dataStoreConfigFile;
        this.id=dataStoreId;
    }

    public final void setId(String id) {
        this.id=id;
    }

    public final void setConfigFile(String configFile) {
        this.configFile=configFile;
    }

    public final void setSetupSuffix(String setupSuffix) {
        this.setupSuffix=setupSuffix;
    }

    public final void setCleanupSuffix(String cleanupSuffix) {
        this.cleanupSuffix=cleanupSuffix;
    }


    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final String getConfigFile() {
        return configFile;
    }

    /**
     * The standard implementation returns {@link org.failearly.dataset.config.DataSetProperties#getDefaultSetupSuffix()},
     * if the value is
     * {@link Constants#DATASET_USE_DEFAULT_SUFFIX}.
     *
     * @return the default setup suffix associated with current datastore.
     */
    @Override
    public String getSetupSuffix() {
        if (useDefaultSuffix(this.setupSuffix))
            return DataSetProperties.getDefaultSetupSuffix();
        return this.setupSuffix;
    }

    /**
     * The standard implementation returns {@link org.failearly.dataset.config.DataSetProperties#getDefaultCleanupSuffix()},
     * if the value is
     * {@link Constants#DATASET_USE_DEFAULT_SUFFIX}.
     *
     * @return the default cleanup suffix associated with current datastore.
     */
    @Override
    public String getCleanupSuffix() {
        if (useDefaultSuffix(this.cleanupSuffix))
            return DataSetProperties.getDefaultCleanupSuffix();
        return this.cleanupSuffix;
    }

    private static boolean useDefaultSuffix(String setupSuffix) {
        return Constants.DATASET_USE_DEFAULT_SUFFIX.equals(setupSuffix);
    }

    @Override
    public void initialize() throws DataStoreException {
        with.action("Initialize " + this.getClass().getSimpleName(), () -> {
            checkConfigurationFileForExistence();
            loadDataStoreConfiguration();
        });
        with.action("Establish connection", establishingConnectionFailedMessage(), () -> {
            doEstablishConnection(this.properties);
        });
    }

    protected MessageBuilders.LazyMessage establishingConnectionFailedMessage() {
        return MessageBuilders.createLazyMessage(mb -> {
                return mb.argument("class", this.getClass().getSimpleName())
                    .firstLine("(__class__) Establishing a connection failed!");
            }
        );
    }

    @Override
    public final void setupDataStore(List<DataStoreSetupInstance> dataStoreSetups, TemplateObjects templateObjects) throws DataStoreInitializationException {
        dataStoreSetups.stream()
            .filter(this::belongsToThisDataStore)
            .forEach((dataStoreSetup) -> doCreateDataSetResources(dataStoreSetup, templateObjects));
        Collections.reverse(this.cleanupDataStoreResources);
    }

    @Override
    public void setup(TestMethod testMethod) throws DataStoreException {
        testMethod.handleSetupResource(this.getId(), this::doHandleSetupResources);
    }

    @Override
    public void cleanup(TestMethod testMethod) {
        testMethod.handleCleanupResource(this.getId(), this::doHandleCleanupResources);
    }

    @Override
    public void cleanupDataStore() {
        cleanupDataStoreResources.forEach(this::doApplyResource);
        cleanupDataStoreResources.clear();
    }

    @Override
    public void dispose() {
        LOGGER.info("Dispose data store '{}'", id);
    }

    /**
     * Will be called by {@link DataStore#initialize()}, if there was no error and all properties could be called.
     *
     * @param properties the {@link #properties} instance.
     */
    protected void doEstablishConnection(ExtendedProperties properties) throws Exception {
        LOGGER.warn("doEstablishConnection() should be implemented.");
    }

    /**
     * Do apply the (setup or cleanup) dataset resource.
     *
     * @param dataResource the resource.
     */
    protected abstract void doApplyResource(DataResource dataResource) throws DataStoreException;

    /**
     * @return the loaded properties from {@link #getConfigFile()}.
     */
    protected final ExtendedProperties getProperties() {
        return properties;
    }

    @Override
    public final String getProperty(String key) {
        return properties.getProperty(key);
    }

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    protected final String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private void loadDataStoreConfiguration() {
        try {
            this.properties=new ExtendedProperties(DataSetProperties.getProperties());
            this.properties.load(ResourceUtils.openResource(DataStoreBase.class, configFile));
            this.properties.resolveReferences();
        } catch (IOException e) {
            throw new DataStoreInitializationException("Unexpected IO exception while loading configuration file " + configFile, e);
        }
    }

    private void checkConfigurationFileForExistence() {
        if (!ResourceUtils.resourceExistsInClassPath(DataStoreBase.class, configFile)) {
            LOGGER.error("Missing configuration file '{}' for DataStore '{}'", configFile, id);
            throw new DataStoreInitializationException("Missing configuration file " + configFile + " for DataStore " + id);
        }
    }

    private void doCreateDataSetResources(DataStoreSetupInstance dataStoreSetup, TemplateObjects templateObjects) {
        applyDataStoreSetupResources(dataStoreSetup, templateObjects);
        addDataStoreCleanupResources(dataStoreSetup, templateObjects);
    }

    private void applyDataStoreSetupResources(DataStoreSetupInstance dataStoreSetup, TemplateObjects templateObjects) {
        doHandleDataStoreSetupResources(ResourceType.SETUP, dataStoreSetup, templateObjects, dataStoreSetup.setup(), this::doApplyResource);
    }

    private void addDataStoreCleanupResources(DataStoreSetupInstance dataStoreSetup, TemplateObjects templateObjects) {
        doHandleDataStoreSetupResources(ResourceType.CLEANUP, dataStoreSetup, templateObjects, dataStoreSetup.cleanup(), cleanupDataStoreResources::add);
    }

    private static void doHandleDataStoreSetupResources(
        ResourceType resourceType,
        DataStoreSetupInstance dataStoreSetupInstance,
        TemplateObjects templateObjects,
        String[] resourceNames,
        Consumer<DataResource> dataResourceConsumer) {
        for (String resourceName : resourceNames) {

            dataResourceConsumer.accept(createDataResourceFromDataStoreSetup(resourceType, dataStoreSetupInstance, templateObjects, resourceName));
        }
    }

    private static DataResource createDataResourceFromDataStoreSetup( //
                                                                      ResourceType resourceType,                                //
                                                                      DataStoreSetupInstance dataStoreSetupInstance,            //
                                                                      TemplateObjects templateObjects,                 //
                                                                      String resourceName) {
        final DataStoreSetup dataStoreSetup=dataStoreSetupInstance.getAnnotation();
        String dataSetName=dataStoreSetup.name();
        if (StringUtils.isEmpty(dataStoreSetup.name())) {
            dataSetName=dataStoreSetup.datastore();
        }
        return DataResourceBuilder.createBuilder(dataStoreSetupInstance.getAssociatedClass())  //
            .withResourceType(resourceType) //
            .withDataStoreId(dataStoreSetup.datastore())  //
            .withDataSetName(dataSetName) //
            .withResourceName(resourceName) //
            .withFailOnError(dataStoreSetup.failOnError()) //
            .withTransactional(dataStoreSetup.transactional()) //
            .withTemplateObjects(templateObjects) //
            .build();
    }

    private boolean belongsToThisDataStore(DataStoreSetupInstance dataStoreSetup) {
        return dataStoreSetup.belongsToDataStore(id);
    }

    private void doHandleSetupResources(String methodName, String dataSet, DataResource dataResource) throws DataStoreException {
        LOGGER.debug("Apply setup resource {} of data set {} on test {}", dataResource.getResource(), dataSet, methodName);
        doApplyResource(dataResource);
    }

    private void doHandleCleanupResources(String methodName, String dataSet, DataResource dataResource) throws DataStoreException {
        LOGGER.debug("Apply cleanup resource {} of data set {} on test {}", dataResource.getResource(), dataSet, methodName);
        doApplyResource(dataResource);
    }

}
