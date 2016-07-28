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

package org.failearly.dataz.internal.model;

import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.annotation.traverser.TraverseStrategy;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.SuppressCleanup;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStores;
import org.failearly.dataz.datastore.MutableDataStores;
import org.failearly.dataz.internal.resource.resolver.DataResourcesResolver;
import org.failearly.dataz.internal.resource.resolver.DataResourcesResolvers;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.failearly.dataz.resource.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * AtomicTestInstance is responsible for ...
 */
@SuppressWarnings("WeakerAccess")
final class AtomicTestInstance implements AtomicTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicTestInstance.class);

    private static final MetaAnnotationTraverser<SuppressCleanup> SUPPRESS_CLEANUP_TRAVERSER =  //
            metaAnnotationTraverser(SuppressCleanup.class)                                      //
                    .withTraverseStrategy(TraverseStrategy.TOP_DOWN)                            //
                    .withTraverseDepth(TraverseDepth.HIERARCHY)                                 //
                    .build();

    private static final DataResourcesResolver setupResolver= DataResourcesResolvers.setupDataResourcesResolver(TraverseDepth.HIERARCHY);
    private static final DataResourcesResolver cleanupResolver= DataResourcesResolvers.cleanupDataResourcesResolver(TraverseDepth.HIERARCHY);
    private static final TemplateObjectsResolver templateObjectResolver= TemplateObjectsResolver.withStandardSettings();

    private final String name;
    private List<DataResource> setupResources = new LinkedList<>();
    private List<DataResource> cleanupResources = new LinkedList<>();
    private final boolean suppressCleanup;
    private MutableDataStores datastores;


    private AtomicTestInstance(String testMethodName, boolean suppressCleanup) {
        this.name = testMethodName;
        this.suppressCleanup = suppressCleanup;
    }

    /**
     * Create a {@link AtomicTest} from a {@link Method} object.
     *
     * @param testMethod the test method instance
     * @param testClass  the enclosing (test) class
     * @return the test instance
     */
    public static AtomicTest createTestInstance(Method testMethod, Class<?> testClass) {
        final AtomicTestInstance newInstance = new AtomicTestInstance(
                testMethod.getName(),
                isSuppressCleanupAvailable(testMethod, testClass)
        );
        newInstance.resolveDataResources(templateObjectResolver.resolveFromMethod(testMethod), testMethod);
        return newInstance;
    }

    private void resolveDataResources(TemplateObjects templateObjects, Method testMethod) {
        resolveSetupDataResources(testMethod, templateObjects);
        resolveCleanupDataResources(testMethod, templateObjects);
    }

    private MutableDataStores reserveDataStores() {
        return DataStores.reserve(collectAllDataStoresFromDataResources());
    }

    private List<Class<? extends NamedDataStore>> collectAllDataStoresFromDataResources() {
        final List<Class<? extends NamedDataStore>> dataStoreNames = new LinkedList<>();
        dataStoreNames.addAll(collectDataStores(setupResources));
        dataStoreNames.addAll(collectDataStores(cleanupResources));
        return dataStoreNames;
    }

    private static List<Class<? extends NamedDataStore>> collectDataStores(List<DataResource> resources) {
        return resources.stream()
                .map(DataResource::getNamedDataStore)
                .collect(Collectors.toList());
    }

    private void resolveSetupDataResources(Method testMethod, TemplateObjects templateObjects) {
        this.setupResources = setupResolver.resolveFromMethod(testMethod, templateObjects);
    }

    private void resolveCleanupDataResources(Method testMethod, TemplateObjects templateObjects) {
        this.cleanupResources = cleanupResolver.resolveFromMethod(testMethod, templateObjects);
    }

    private static boolean isSuppressCleanupAvailable(Method testMethod, Class<?> testClass) {
        boolean isSuppressCleanupAvailable = SUPPRESS_CLEANUP_TRAVERSER.anyAnnotationAvailable(testMethod);

        if (!isSuppressCleanupAvailable && testMethod.getDeclaringClass() != testClass) {
            isSuppressCleanupAvailable = SUPPRESS_CLEANUP_TRAVERSER.anyAnnotationAvailable(testClass);
        }

        return isSuppressCleanupAvailable;
    }

    @Override
    public boolean isValid() {
        return true;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setup() {
        this.datastores = reserveDataStores();
        this.datastores.apply(dataStore -> applyDataResources(dataStore, setupResources));
    }

    @Override
    public void cleanup() {
        this.datastores.apply(dataStore -> {
            if( ! isSuppressCleanup() ) {
                applyDataResources(dataStore, cleanupResources);
            }
            else {
                LOGGER.warn("@SuppressCleanup: cleanup() not executed on datastore={}", dataStore.getId());
            }
        });

        this.datastores.release();
        this.datastores = null;
    }

    private void applyDataResources(DataStore dataStore, List<DataResource> resources) {
        resources.stream()
                .filter(dataResource -> dataResource.shouldAppliedOn(dataStore))
                .forEach(dataStore::applyDataResource);
    }

    @Override
    public boolean isSuppressCleanup() {
        return suppressCleanup;
    }

}
