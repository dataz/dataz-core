/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.model;

import org.failearly.dataset.SuppressCleanup;
import org.failearly.dataset.internal.annotation.*;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.generator.resolver.GeneratorResolver;
import org.failearly.dataset.internal.resource.DataResourceHandler;
import org.failearly.dataset.annotations.DataCleanupResourceFactoryDefinition;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.annotations.DataSetupResourceFactoryDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * TestMethodImpl is responsible for ...
 */
final class TestMethodImpl implements TestMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestMethodImpl.class);

    private final String name;

    private final List<DataResource> setupResources = new LinkedList<>();
    private final List<DataResource> cleanupResources = new LinkedList<>();
    private final boolean suppressCleanup;


    private TestMethodImpl(String testMethodName, boolean suppressCleanup) {
        this.name = testMethodName;
        this.suppressCleanup = suppressCleanup;
    }

    /**
     * Create a test method implementation.
     *
     * @param testMethod  the test method instance
     * @param testClass  the enclosing (test) class
     *
     * @return the test method instance
     */
    public static TestMethod createTestMethod(Method testMethod, Class<?> testClass) {
        final TestMethodImpl newInstance = new TestMethodImpl(testMethod.getName(), isSuppressCleanupAvailable(testMethod, testClass));
        newInstance.resolveDataResources(GeneratorResolver.resolveFromTestMethod(testMethod), testMethod);
        return newInstance;
    }

    private void resolveDataResources(List<GeneratorCreator> generatorCreators, Method testMethod) {
        resolveSetupDataResources(testMethod, generatorCreators);
        resolveCleanupDataResources(testMethod, generatorCreators);
    }

    private void resolveSetupDataResources(Method testMethod, List<GeneratorCreator> generatorCreators) {
        final AnnotationTraverser<Annotation> resourcesTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
                DataSetupResourceFactoryDefinition.class,
                TraverseStrategy.BOTTOM_UP,
                Order.UNCHANGED);
        resourcesTraverser.traverse(testMethod, new DataSetupResourceAnnotationHandler(setupResources, generatorCreators));
    }

    private void resolveCleanupDataResources(Method testMethod, List<GeneratorCreator> generatorCreators) {
        final AnnotationTraverser<Annotation> resourcesTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
                DataCleanupResourceFactoryDefinition.class,
                TraverseStrategy.BOTTOM_UP,
                Order.UNCHANGED);
        resourcesTraverser.traverse(testMethod, new DataCleanupResourceAnnotationHandler(cleanupResources, generatorCreators));
        Collections.reverse(cleanupResources);
    }

    private static boolean isSuppressCleanupAvailable(Method testMethod, Class<?> testClass) {
        return testMethod.isAnnotationPresent(SuppressCleanup.class) ||
                testClass.isAnnotationPresent(SuppressCleanup.class);
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
    public void handleSetupResource(String dataStoreId, DataResourceHandler dataResourceHandler) {
        handleResource(setupResources, dataStoreId, dataResourceHandler);
    }

    @Override
    public void handleCleanupResource(String dataStoreId, DataResourceHandler dataResourceHandler) {
        assert ! suppressCleanup : "handleCleanupResource() must not be called if @SuppressCleanup is active.";
        handleResource(cleanupResources, dataStoreId, dataResourceHandler);
    }

    @Override
    public boolean isSuppressCleanup() {
        return suppressCleanup;
    }

    private void handleResource(List<DataResource> dataSetResources, String dataStoreId, DataResourceHandler dataResourceHandler) {
        Objects.requireNonNull(dataStoreId, "DataStore ID must not be null");
        Objects.requireNonNull(dataResourceHandler, "dataResourceHandler must not be null");
        dataSetResources.stream()
                .filter(resource -> dataStoreId.equals(resource.getDataStoreId()))
                .forEach(resource -> dataResourceHandler.handleResource(this.name, resource.getDataSetName(), resource));
    }
}
