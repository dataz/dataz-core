/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
 */

package org.failearly.dataz.datastore;

import org.apache.commons.lang.StringUtils;
import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.annotation.traverser.TraverseStrategy;
import org.failearly.common.message.InlineMessageTemplate;
import org.failearly.common.message.Message;
import org.failearly.common.message.MessageBuilderBase;
import org.failearly.common.message.TemplateParameters;
import org.failearly.common.proputils.ExtendedProperties;
import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.common.test.With;
import org.failearly.dataz.common.Property;
import org.failearly.dataz.common.PropertyUtility;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.config.DataSetProperties;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.internal.resource.resolver.DataResourcesResolver;
import org.failearly.dataz.internal.resource.resolver.DataResourcesResolvers;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.failearly.dataz.internal.util.ResourceUtils;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;
import org.failearly.dataz.resource.DelegateDataResource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;
import static org.failearly.common.annotation.utils.AnnotationUtils.resolveValueOfAnnotationAttribute;


/**
 * {@code DataStoreBase} should be <b>the</b> base class for any DataStore implementation. {@code DataStoreBase} is
 * responsible for the correct initialization and disposing of the instance.
 * <br><br>
 * <b>Caution</b>: It's possible to override {@link #initialize()}, but not recommended.
 * <br><br>
 * The initialization steps are:<br><br>
 * <ol>
 *    <li>loading and merging properties (file(s) first, dataStoreAnnotation properties last)</li>
 *    <li>execute scripts (TODO: not yet implemented)</li>
 *    <li>establish connection to DataStore</li>
 *    <li>resolve setup and cleanup {@link DataResource}s from {@link NamedDataStore} class</li>
 *    <li>Execute setup {@code DataResource}s (using {@link #applyDataResource(DataResource)})</li>
 * </ol>
 * <br><br>
 * There are some useful abstractions like:
 * <br><br>
 * <ul>
 * <li>{@link org.failearly.dataz.datastore.support.TransactionalSupportDataStoreBase}: provides an implementation for
 * transactional behaviour.</li>
 * <li>{@link org.failearly.dataz.datastore.support.SimpleFileTransactionalSupportDataStoreBase}: extends the
 * transactional resource and
 * add {@link org.failearly.dataz.simplefile.SimpleFileParser} to it.
 * </li>
 * </ul>
 */
public abstract class DataStoreBase extends AbstractDataStore {
    protected static final With with = With.create((description, exception) -> {
        throw new DataStoreException(description, exception);
    }, "standard-datastore-exception");


    private static final MetaAnnotationTraverser<DataResourcesFactory.SetupDefinition> SETUP_RESOURCES_TRAVERSER =  //
            metaAnnotationTraverser(DataResourcesFactory.SetupDefinition.class)                                     //
                    .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)                                  //
                    .withTraverseDepth(TraverseDepth.HIERARCHY)                                        //
                    .build();

    private static final DataResourcesResolver setupResolver= DataResourcesResolvers.setupDataResourcesResolver(TraverseDepth.HIERARCHY);
    private static final DataResourcesResolver cleanupResolver= DataResourcesResolvers.cleanupDataResourcesResolver(TraverseDepth.HIERARCHY);

    private final Class<? extends NamedDataStore> namedDataStore;
    private final Annotation dataStoreAnnotation;
    private final String name;
    private final String id;
    private final String configFile;

    private final ExtendedProperties properties=new ExtendedProperties();

    private TemplateObjects templateObjects;
    private List<DataResource> cleanupDataResources = new LinkedList<>();

    private boolean initialized=false;

    protected DataStoreBase(Class<? extends NamedDataStore> namedDataStore, Annotation dataStoreAnnotation) {
        this.namedDataStore = namedDataStore;
        this.dataStoreAnnotation = dataStoreAnnotation;
        this.name       = resolveValueOfAnnotationAttribute(dataStoreAnnotation, "name", String.class);
        this.id         = createDataStoreId(this, namedDataStore, name);
        this.configFile = resolveValueOfAnnotationAttribute(dataStoreAnnotation, "config", String.class);
    }

    private static String createDataStoreId(DataStore dataStore, Class<? extends NamedDataStore> namedDataStore, String dataStoreName) {
        String id;
        if (StringUtils.isNotEmpty(dataStoreName))
            id = namedDataStore.getSimpleName() + "[name=" + dataStoreName + "]";
        else
            id = namedDataStore.getSimpleName();

        return id + "@" + dataStore.getClass().getSimpleName();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public final Class<? extends NamedDataStore> getNamedDataStore() {
        return namedDataStore;
    }

    @Override
    public final String getConfigFile() {
        return configFile;
    }

    @Override
    public void initialize() throws DataStoreException {
        if( initialized ) {
            throw new IllegalStateException("Must no initialized twice.");
        }

        loadProperties();

        executeScripts();

        establishConnection();

        resolveTemplateObjects();

        applySetupDataResources();

        loadCleanupDataResources();

        this.initialized = true;
    }

    private void loadCleanupDataResources() {
        cleanupDataResources = cleanupResolver.resolveFromClass(this.namedDataStore, this.templateObjects);
    }

    private void resolveTemplateObjects() {
        this.templateObjects= TemplateObjectsResolver.resolveFromClass(namedDataStore);
    }

    private void applySetupDataResources() {
        final List<DataResource> setupResources = resolveSetupDataResources();
        with.action("Apply setup resources",
                ()->setupResources.forEach(this::handleDataResource)
        );
    }

    private void handleDataResource(DataResource dataResource) {
        final DataResource delegatedDataResource = new OverwriteAssignedDataStore(dataResource);
        if( delegatedDataResource.isFailOnError() ){
            doApplyResource(delegatedDataResource);
        }
        else {
            With.ignore().action("Apply resource " + delegatedDataResource.getResource(), ()->doApplyResource(delegatedDataResource));
        }
    }

    private List<DataResource> resolveSetupDataResources() {
        return setupResolver.resolveFromClass(this.namedDataStore, this.templateObjects);
    }

    private void executeScripts() {
        // TODO: Implement DataStoreBase#executeScripts
    }

    private void establishConnection() {
        with.action("Establish connection " + this.getId(), establishingConnectionFailedMessage(), () -> {
            doEstablishConnection(this.getProperties());
        });
    }

    private void loadProperties() {
        with.action("Load properties " + this.getId(), () -> {
            if (checkConfigurationFileForExistence()) {
                this.properties.merge(
                        DataSetProperties.getProperties(),
                        loadDataStoreConfigurationFromConfigFile(),
                        loadDataStoreConfigFromAnnotationProperties()
                );
            } else {
                this.properties.merge(
                        DataSetProperties.getProperties(),
                        loadDataStoreConfigFromAnnotationProperties()
                );
            }
        });
    }

    private Properties loadDataStoreConfigFromAnnotationProperties() {
        final Property[] annotationProperties = resolveValueOfAnnotationAttribute(dataStoreAnnotation, "properties", Property[].class);
        return PropertyUtility.toProperties(annotationProperties);
    }

    protected Message establishingConnectionFailedMessage() {
        return new EstablishingConnectionFailed().buildLazyMessage(
                (mb) -> mb.withDataStore(this)
        );
    }

    @Override
    public final void applyDataResource(DataResource dataResource) {
        if( ! initialized )
            throw new IllegalStateException(this.getId() +" not yet initialized!" );
        doApplyResource(dataResource);
    }

    @Override
    public void cleanupDataStore() {
        with.action("Apply cleanup resources",
                ()->cleanupDataResources.forEach(this::handleDataResource)
        );
        cleanupDataResources.clear();
    }

    @Override
    public void dispose() {
        cleanupDataStore();
        LOGGER.info("Dispose data store '{}'", id);
    }

    /**
     * Will be called by {@link DataStore#initialize()}, if there was no error and all properties could be called.
     *
     * @param properties the {@link #properties} instance.
     * @throws Exception any exception while establishing a connection
     */
    protected void doEstablishConnection(PropertiesAccessor properties) throws Exception {
        LOGGER.error("doEstablishConnection() must be implemented.");
    }

    /**
     * Do apply the (setup or cleanup) dataz resource.
     *
     * @param dataResource the resource.
     */
    protected abstract void doApplyResource(DataResource dataResource) throws DataStoreException;

    @Override
    public final PropertiesAccessor getProperties() {
        return properties.toAccessor();
    }

    private Properties loadDataStoreConfigurationFromConfigFile() {
        final Properties properties=new Properties();
        try {
            properties.load(ResourceUtils.openResource(DataStoreBase.class, configFile));
            return properties;
        } catch (IOException e) {
            throw new DataStoreInitializationException("Unexpected IO exception while loading configuration file " + configFile, e);
        }
    }

    private boolean checkConfigurationFileForExistence() {
        if (Constants.DATAZ_NO_CONFIG_FILE.equals(configFile)) {
            return false;
        }

        if (!ResourceUtils.resourceExistsInClassPath(DataStoreBase.class, configFile)) {
            LOGGER.error("Missing configuration file '{}' for DataStore '{}'", configFile, id);
            throw new DataStoreInitializationException("Missing configuration file " + configFile + " for DataStore " + id);
        }

        return true;
    }

    @InlineMessageTemplate("${ds}: Establishing a connection failed on datastore with id ${dsid}!")
    @TemplateParameters({EstablishingConnectionFailed.ARG_DS_CLASS, EstablishingConnectionFailed.ARG_DS_ID})
    private static class EstablishingConnectionFailed extends MessageBuilderBase<EstablishingConnectionFailed> {

        static final String ARG_DS_CLASS = "ds";
        static final String ARG_DS_ID = "dsid";


        EstablishingConnectionFailed() {
            super(EstablishingConnectionFailed.class);
        }

        EstablishingConnectionFailed withDataStore(DataStoreBase dataStore) {
            return with(ARG_DS_CLASS, dataStore.getClass().getSimpleName())
                    .with(ARG_DS_ID, dataStore.getId());
        }
    }


    private class OverwriteAssignedDataStore extends DelegateDataResource {

        OverwriteAssignedDataStore(DataResource origin) {
            super(origin);
        }

        @Override
        public List<Class<? extends NamedDataStore>> getDataStores() {
            return Collections.singletonList(namedDataStore);
        }
    }
}
