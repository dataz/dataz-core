/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.resource.factory.use;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.ReusableDataSet;
import org.failearly.dataset.Use;
import org.failearly.dataset.internal.annotation.*;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.TypedDataResourcesFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.failearly.dataset.internal.template.TemplateObjectsResolver.resolveFromTestClass;

/**
 * DataSetResourcesFactoryBase is the base class for {@link DataSet} based {@link DataResource}s factory classes.
 */
abstract class ResourcesFactoryBase<T extends Annotation> extends TypedDataResourcesFactory<Use> {

    private static final ThreadLocal<Integer> nestedUseCounter = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    protected ResourcesFactoryBase() {
        super(Use.class);
    }

    @Override
    protected List<DataResource> doCreateDataResources(Use annotation, TemplateObjects ignored) {
        final List<DataResource> dataResources = new LinkedList<>();
        final List<Class<? extends ReusableDataSet>> reusableDataSetClasses = filterDuplicatedReusableDataSetClasses(annotation);
        doResolveDataResourcesFromAllReusableDataSets(reusableDataSetClasses, dataResources);
        return changeOrder(dataResources);
    }

    private List<DataResource> changeOrder(List<DataResource> dataResources) {
        if (isNestedUse())
            return dataResources;

        return doChangeOrder(dataResources);
    }

    private static boolean isNestedUse() {
        return nestedUseCounter.get() > 0;
    }

    private void doResolveDataResourcesFromAllReusableDataSets(List<Class<? extends ReusableDataSet>> reusableDataSetClasses, List<DataResource> dataResources) {
        try {
            nestedUse(1);
            for (Class<? extends ReusableDataSet> reusableDataSetClass : reusableDataSetClasses) {
                doCreateDataResourcesFromSingleReusableDataSet(reusableDataSetClass, dataResources);
            }
        }
        finally {
            nestedUse(-1);
        }
    }

    private static void nestedUse(int value) {
        nestedUseCounter.set(nestedUseCounter.get() + value);
    }

    protected List<DataResource> doChangeOrder(List<DataResource> dataResources) {
        return dataResources;
    }


    private void doCreateDataResourcesFromSingleReusableDataSet(Class<?> reusableDataSetClass, List<DataResource> dataResources) {
        final AnnotationTraverser<Annotation> resourcesTraverser = AnnotationTraversers.createMetaAnnotationTraverser(
                annotationClass(),
                TraverseStrategy.BOTTOM_UP,
                TraverseDepth.CLASS_HIERARCHY
        );
        resourcesTraverser.traverse(                                                               //
                reusableDataSetClass,                                                              //
                annotationHandler(dataResources, resolveFromTestClass(reusableDataSetClass))       //
        );
    }

    protected abstract Class<? extends Annotation> annotationClass();

    protected abstract AnnotationHandler<Annotation> annotationHandler(List<DataResource> dataResources, TemplateObjects templateObjects);

    private List<Class<? extends ReusableDataSet>> filterDuplicatedReusableDataSetClasses(Use annotation) {
        final List<Class<? extends ReusableDataSet>> filteredDataSetClasses=new LinkedList<>();
        final Set<Class<? extends ReusableDataSet>> uniqueReusableDataSets=new HashSet<>();
        for (Class<? extends ReusableDataSet> reusableDataSet : annotation.value()) {
            if( uniqueReusableDataSets.add(reusableDataSet) ) {
                filteredDataSetClasses.add(reusableDataSet);
            }
        }
        return filteredDataSetClasses;
    }


}
