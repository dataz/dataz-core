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

import org.failearly.common.annotation.utils.AnnotationUtils;
import org.failearly.common.annotation.elementresolver.AnnotationElementResolver;
import org.failearly.common.annotation.elementresolver.AnnotationElementResolvers;
import org.failearly.common.annotation.traverser.*;
import org.failearly.common.test.ObjectCreator;
import org.failearly.dataz.AdhocDataStore;
import org.failearly.dataz.DataStoreSetup;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * DataStores is responsible for creating and holding all data stores.
 */
@SuppressWarnings("UnusedDeclaration")
public final class DataStores {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStores.class);

    private static DataStores INSTANCE = new DataStores();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DataStores::shutdown));
    }

    private static final Annotation DEFAULT_DATA_STORE_ANNOTATION = DefaultDataStoreDefinition.class.getAnnotation(AdhocDataStore.class);

    private final Map<String, DataStore> dataStoreById = new ConcurrentHashMap<>();
    private final Map<Class<?>, DataStore> dataStoreByTestClass = new ConcurrentHashMap<>();

    private final AnnotationTraverser<DataStoreSetup> dataStoreSetupAnnotationTraverser = AnnotationTraverserBuilder.annotationTraverser(DataStoreSetup.class)
            .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
            .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
            .build();

    private final MetaAnnotationTraverser<DataStoreFactory.Definition> metaDataStoreFactoryAnnotationTraverser = AnnotationTraverserBuilder.metaAnnotationTraverser(DataStoreFactory.Definition.class)
            .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
            .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
            .build();

    private DataStores() {
    }

    /**
     * Load the data store(s) associated with current test class.
     *
     * @param testClass the test class.
     * @return a {@link org.failearly.dataz.datastore.DataStore} or {@link org.failearly.dataz.datastore.DataStoreCollection}.
     */
    public static DataStore loadDataStore(Class<?> testClass) {
        return loadDataStore(testClass, null);
    }

    /**
     * Load the data store(s) associated with current test class (and context object).
     *
     * @param testClass the test class.
     * @param context any context to be used for
     *      {@link org.failearly.dataz.datastore.DataStoreFactory#createDataStore(java.lang.annotation.Annotation, Object)}.
     * @return a {@link org.failearly.dataz.datastore.DataStore} or {@link org.failearly.dataz.datastore.DataStoreCollection}.
     *
     * @see org.failearly.dataz.datastore.DataStoreFactory#createDataStore(java.lang.annotation.Annotation, Object)
     */
    public static DataStore loadDataStore(Class<?> testClass, Object context) {
        return INSTANCE.doLoadDataStore(testClass);
    }


    /**
     * Add a data store instance.
     *
     * @param testClass the associated test class.
     * @param dataStore the data store instance.
     */
    public static void addDataStore(Class<?> testClass, DataStore dataStore) {
        INSTANCE.doAddDataStore(testClass, dataStore);
    }

    private void doAddDataStore(Class<?> testClass, DataStore dataStore) {
        dataStoreById.computeIfAbsent(dataStore.getId(), (id) -> doInitializeDataStore(testClass, dataStore));
        dataStoreByTestClass.putIfAbsent(testClass, dataStore);
    }

    private DataStore doInitializeDataStore(Class<?> testClass, DataStore dataStore) {
        final List<DataStoreSetupInstance> dataStoreSetupAnnotations = collectDataStoreSetupAnnotationsFromTestClass(testClass);
        final TemplateObjects templateObjects = resolveTemplateObjects(testClass);
        dataStore.initialize();
        dataStore.setupDataStore(dataStoreSetupAnnotations, templateObjects);
        return dataStore;
    }

    private static TemplateObjects resolveTemplateObjects(Class<?> testClass) {
        return TemplateObjectsResolver.resolveFromTestClass(testClass);
    }

    /**
     * Dispose all {@link org.failearly.dataz.datastore.DataStore}s. {@link DataStore#dispose()} will be called only once.
     */
    public static void dispose() {
        INSTANCE.doDispose();
    }

    /**
     * Reset current INSTANCE and dispose all {@link org.failearly.dataz.datastore.DataStore}.
     */
    public static void reset() {
        INSTANCE.doDispose();
        INSTANCE = new DataStores();
    }

    public static DataStore getDataStore(Class<?> testClass) {
        return INSTANCE.lookupDataStoreByTestClass(testClass);
    }

    /**
     * Access to the DataStore identified by {@code dataStoreId}.
     *
     * @param dataStoreId the {@link org.failearly.dataz.datastore.DataStore#getId()}.
     * @param test        the test instance.
     * @return the DataStore with given {@code dataStoreId}.
     * @throws AssertionError no DataStore found with given {@code dataStoreId}.
     */
    public static DataStore getDataStore(String dataStoreId, Object test) {
        return getDataStore(test.getClass(), dataStoreId);
    }

    /**
     * Access to the DataStore identified by {@code dataStoreId} and associated to the test class.
     *
     * @param testClass   the test class.
     * @param dataStoreId the {@link DataStore#getId()}.
     * @return the DataStore with given {@code dataStoreId}.
     * @throws AssertionError no DataStore found with given {@code dataStoreId}.
     */
    public static DataStore getDataStore(Class<?> testClass, String dataStoreId) {
        return INSTANCE.lookupDataStoreByTestClassAndId(testClass, dataStoreId);
    }

    /**
     * Convenient access method for the default data store (associated to current test class).
     *
     * @param testClass the test class.
     * @return the default data store.
     * @see Constants#DATAZ_DEFAULT_DATASTORE_ID
     */
    public static DataStore getDefaultDataStore(Class<?> testClass) {
        return getDataStore(testClass, Constants.DATAZ_DEFAULT_DATASTORE_ID);
    }

    /**
     * Convenient access method for the default data store.
     *
     * @return the default data store.
     * @see Constants#DATAZ_DEFAULT_DATASTORE_ID
     */
    public static DataStore getDefaultDataStore() {
        return getDataStore(Constants.DATAZ_DEFAULT_DATASTORE_ID);
    }

    /**
     * Access to the DataStore identified by {@code dataStoreId}.
     *
     * @param dataStoreId the {@link org.failearly.dataz.datastore.DataStore#getId()}.
     * @return the DataStore with given {@code dataStoreId}.
     * @throws AssertionError no DataStore found with given {@code dataStoreId}.
     */
    public static DataStore getDataStore(String dataStoreId) {
        return INSTANCE.lookupDataStoreById(dataStoreId);
    }

    /**
     * Resolve the setup suffix for given data store identified by {@code dataStoreId}.
     *
     * @param dataStoreId the {@link org.failearly.dataz.datastore.DataStore#getId()}.
     * @return value of {@link DataStore#getSetupSuffix()}
     * @throws AssertionError no DataStore found with given {@code dataStoreId}.
     * @see DataStore#getSetupSuffix()
     * @see DataStores#getDataStore(String)
     */
    public static String getDataStoreSetupSuffix(String dataStoreId) {
        return getDataStore(dataStoreId).getSetupSuffix();
    }

    /**
     * Resolve the cleanup suffix for given data store identified by {@code dataStoreId}.
     *
     * @param dataStoreId the {@link org.failearly.dataz.datastore.DataStore#getId()}.
     * @return value of {@link DataStore#getCleanupSuffix()}
     * @throws AssertionError no DataStore found with given {@code dataStoreId}.
     * @see DataStore#getCleanupSuffix()
     * @see DataStores#getDataStore(String)
     */
    public static String getDataStoreCleanupSuffix(String dataStoreId) {
        return getDataStore(dataStoreId).getCleanupSuffix();
    }

    private static void shutdown() {
        LOGGER.info("Dispose and shutdown all data stores");
        dispose();
    }

    private void doDispose() {
        dataStoreById.values().forEach(DataStore::cleanupDataStore);
        dataStoreById.values().forEach(DataStore::dispose);
        dataStoreById.clear();
        dataStoreByTestClass.clear();
    }

    private DataStore doLoadDataStore(Class<?> testClass) {
        return dataStoreByTestClass.computeIfAbsent(testClass, this::doLoadDataStore0);
    }


    private DataStore doLoadDataStore0(Class<?> testClass) {
        final List<DataStoreSetupInstance> dataStoreSetupAnnotations = collectDataStoreSetupAnnotationsFromTestClass(testClass);
        final TemplateObjects templateObjects = resolveTemplateObjects(testClass);
        final DataStoreCollection dataStoreCollection = collectDataStoresFromTestClass(testClass, dataStoreSetupAnnotations, templateObjects);

        if (dataStoreCollection.isEmpty()) {
            dataStoreCollection.addDataStore(findOrCreateDataStoreFromAnnotation(DEFAULT_DATA_STORE_ANNOTATION, dataStoreSetupAnnotations, templateObjects));
        }

        return dataStoreCollection.getDataStore();
    }

    private List<DataStoreSetupInstance> collectDataStoreSetupAnnotationsFromTestClass(Class<?> testClass) {
        final List<DataStoreSetupInstance> dataStoreSetupAnnotations = new LinkedList<>();
        dataStoreSetupAnnotationTraverser.traverse(
                testClass,
                new AnnotationHandlerBase<DataStoreSetup>() {
                    @Override
                    public void handleClassAnnotation(Class<?> clazz, DataStoreSetup annotation) {
                        dataStoreSetupAnnotations.add(new DataStoreSetupInstance(clazz, annotation));
                    }
                }
        );
        return dataStoreSetupAnnotations;
    }

    private DataStoreCollection collectDataStoresFromTestClass(
            Class<?> testClass,
            List<DataStoreSetupInstance> dataStoreSetupAnnotations,
            TemplateObjects templateObjects) {
        final DataStoreCollection dataStoreCollection = new DataStoreCollection();
        metaDataStoreFactoryAnnotationTraverser.traverse(testClass, new MetaAnnotationHandlerBase<DataStoreFactory.Definition>() {
            @Override
            public void handleAnnotation(Annotation annotation) {
                dataStoreCollection.addDataStore(findOrCreateDataStoreFromAnnotation(annotation, dataStoreSetupAnnotations, templateObjects));
            }
        });

        return dataStoreCollection;
    }

    private DataStore findOrCreateDataStoreFromAnnotation(
            Annotation annotation,
            List<DataStoreSetupInstance> dataStoreSetupAnnotations,
            TemplateObjects templateObjects) {
        final String dataStoreId = resolveDataStoreId(annotation);
        return dataStoreById.computeIfAbsent(dataStoreId, (id) -> createDataStoreFromAnnotation(annotation, dataStoreSetupAnnotations, templateObjects));
    }

    private static final AnnotationElementResolver<String> idResolver = AnnotationElementResolvers.createResolver(String.class, "id");

    private static String resolveDataStoreId(Annotation annotation) {
        if (idResolver.hasElement(annotation))
            return idResolver.resolveElementValue(annotation);

        throw new MissingDataStoreIdError(annotation);
    }

    @SuppressWarnings("unchecked")
    private DataStore createDataStoreFromAnnotation(Annotation annotation, List<DataStoreSetupInstance> dataStoreSetupAnnotations, TemplateObjects templateObjects) {
        final DataStoreFactory.Definition dataStoreFactoryDefinition = AnnotationUtils.getMetaAnnotation(DataStoreFactory.Definition.class, annotation);
        final Class<? extends DataStoreFactory> dataStoreFactoryClass = dataStoreFactoryDefinition.value();
        final DataStore dataStore = ObjectCreator.createInstance(dataStoreFactoryClass).createDataStore(annotation, null);

        dataStore.initialize();
        dataStore.setupDataStore(dataStoreSetupAnnotations, templateObjects);

        return dataStore;
    }

    private DataStore lookupDataStoreById(String dataStoreId) {
        final DataStore dataStore = dataStoreById.get(dataStoreId);
        checkDataStore(dataStoreId, dataStore);
        return dataStore;
    }

    private static void checkDataStore(String dataStoreId, DataStore dataStore) {
        if (dataStore == null) {
            throw new DataStoreException("DataStore " + dataStoreId + " not found");
        }
    }

    private static void checkDataStore(Class<?> testClass, DataStore dataStore) {
        if (dataStore == null) {
            throw new DataStoreException("DataStore for " + testClass.getName() + " not found");
        }
    }


    private DataStore lookupDataStoreByTestClass(Class<?> testClass) {
        final DataStore dataStore = dataStoreByTestClass.get(testClass);
        checkDataStore(testClass, dataStore);
        return dataStore;
    }

    private DataStore lookupDataStoreByTestClassAndId(Class<?> testClass, String dataStoreId) {
        DataStore dataStore = lookupDataStoreByTestClass(testClass);
        if (dataStore instanceof DataStoreCollection) {
            dataStore = ((DataStoreCollection) dataStore).lookupDataStore(dataStoreId);
        }

        if (dataStore != null && !dataStoreId.equals(dataStore.getId())) {
            dataStore = null;
        }

        checkDataStore(dataStoreId, dataStore);

        return dataStore;
    }

    @AdhocDataStore
    private static class DefaultDataStoreDefinition {
    }
}
