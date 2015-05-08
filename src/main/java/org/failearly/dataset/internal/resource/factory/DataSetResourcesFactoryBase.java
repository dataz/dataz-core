/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 marko (http://fail-early.com/contact)
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
package org.failearly.dataset.internal.resource.factory;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.internal.generator.resolver.GeneratorCreator;
import org.failearly.dataset.internal.resource.ResourceType;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourcesFactoryBase;
import org.failearly.dataset.resource.ResourcePathUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * DataSetResourcesFactoryBase is responsible for ...
 */
public abstract class DataSetResourcesFactoryBase extends DataResourcesFactoryBase<DataSet> {

    public DataSetResourcesFactoryBase(ResourceType resourceType) {
        super(DataSet.class, resourceType);
    }

    @Override
    protected final List<DataResource> resolveAllDataResourcesFromTestClass(Class<?> testClass, DataSet annotation, List<GeneratorCreator> generatorCreators) {
        final LinkedList<DataResource> dataResources = new LinkedList<>();
        final String[] resourceNames= resolveResourceNamesFromAnnotation(annotation);
        if(resourceNames.length>0) {
            for (String resourceName : resourceNames) {
                dataResources.add(createDataResourceFromAnnotation(annotation, testClass, resourceName, generatorCreators));
            }
        }
        else {
            dataResources.add(
                    createDataResourceFromAnnotation(
                            annotation, testClass, ResourcePathUtils.createDefaultResourceNameFromTestClass(testClass, annotation.datastore(), this.resourceType),
                            generatorCreators)
                );
        }
        return dataResources;
    }

    @Override
    protected final List<DataResource> resolveAllDataResourcesFromTestMethod(Method testMethod, DataSet annotation, List<GeneratorCreator> generatorCreators) {
        final LinkedList<DataResource> dataResources = new LinkedList<>();
        final String[] resourceNames= resolveResourceNamesFromAnnotation(annotation);
        if(resourceNames.length>0) {
            for (String resourceName : resourceNames) {
                dataResources.add(createDataResourceFromAnnotation(annotation, testMethod, resourceName, generatorCreators));
            }
        }
        else {
            dataResources.add(
                    createDataResourceFromAnnotation(
                            annotation, testMethod, ResourcePathUtils.createDefaultResourceNameFromTestMethod(testMethod, annotation.datastore(), this.resourceType),
                            generatorCreators)
            );
        }
        return dataResources;
    }

}