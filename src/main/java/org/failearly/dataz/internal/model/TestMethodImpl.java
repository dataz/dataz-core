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

import org.failearly.common.annotation.traverser.*;
import org.failearly.dataz.SuppressCleanup;
import org.failearly.dataz.internal.resource.DataResourceHandler;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * TestMethodImpl is responsible for ...
 */
final class TestMethodImpl implements TestMethod {
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
     * @param testMethod the test method instance
     * @param testClass  the enclosing (test) class
     * @return the test method instance
     */
    public static TestMethod createTestMethod(Method testMethod, Class<?> testClass) {
        final TestMethodImpl newInstance = new TestMethodImpl(testMethod.getName(), isSuppressCleanupAvailable(testMethod, testClass));
        newInstance.resolveDataResources(TemplateObjectsResolver.resolveFromTestMethod(testMethod), testMethod);
        return newInstance;
    }

    private void resolveDataResources(TemplateObjects templateObjects, Method testMethod) {
        resolveSetupDataResources(testMethod, templateObjects);
        resolveCleanupDataResources(testMethod, templateObjects);
    }

    private void resolveSetupDataResources(Method testMethod, TemplateObjects templateObjects) {
        final MetaAnnotationTraverser<DataResourcesFactory.SetupDefinition> resourcesTraverser = metaAnnotationTraverser(DataResourcesFactory.SetupDefinition.class)
                .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
                .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
                .build();
        resourcesTraverser.traverse(testMethod, new DataSetupResourceAnnotationHandler(setupResources, templateObjects));
    }

    private void resolveCleanupDataResources(Method testMethod, TemplateObjects templateObjects) {
        final MetaAnnotationTraverser<DataResourcesFactory.CleanupDefinition> resourcesTraverser = metaAnnotationTraverser(DataResourcesFactory.CleanupDefinition.class)
                .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
                .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
                .build();
        resourcesTraverser.traverse(testMethod, new DataCleanupResourceAnnotationHandler(cleanupResources, templateObjects));
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
        assert !suppressCleanup : "handleCleanupResource() must not be called if @SuppressCleanup is active.";
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
