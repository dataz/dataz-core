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

package org.failearly.dataz.internal.resource.factory.use;

import org.failearly.common.annotation.traverser.*;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.Use;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.TypedDataResourcesFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.failearly.dataz.internal.template.TemplateObjectsResolver.resolveFromTestClass;

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
    private final Class<T> metaAnnotationClass;

    ResourcesFactoryBase(Class<T> metaAnnotationClass) {
        super(Use.class);
        this.metaAnnotationClass = metaAnnotationClass;
    }

    @Override
    protected List<DataResource> doCreateDataResources(Use annotation, TemplateObjects templateObjects) {
        final List<DataResource> dataResources = new LinkedList<>();
        final List<Class<? extends Use.ReusableDataSet>> reusableDataSetClasses = filterDuplicatedReusableDataSetClasses(annotation);
        doResolveDataResourcesFromAllReusableDataSets(reusableDataSetClasses, dataResources, templateObjects.filterGlobalScope());
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

    private void doResolveDataResourcesFromAllReusableDataSets(
            List<Class<? extends Use.ReusableDataSet>> reusableDataSetClasses,
            List<DataResource> dataResources,
            TemplateObjects globalTemplateObjects) {
        try {
            nestedUse(1);
            for (Class<? extends Use.ReusableDataSet> reusableDataSetClass : reusableDataSetClasses) {
                doCreateDataResourcesFromSingleReusableDataSet(reusableDataSetClass, dataResources, globalTemplateObjects);
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


    private void doCreateDataResourcesFromSingleReusableDataSet(Class<?> reusableDataSetClass, List<DataResource> dataResources, TemplateObjects globalTemplateObjects) {
        final MetaAnnotationTraverser<T> resourcesTraverser = AnnotationTraverserBuilder.metaAnnotationTraverser(metaAnnotationClass)
                .withTraverseStrategy(TraverseStrategy.BOTTOM_UP)
                .withTraverseDepth(TraverseDepth.CLASS_HIERARCHY)
                .build();
        resourcesTraverser.traverse(                                                               //
                reusableDataSetClass,                                                              //
                metaAnnotationHandler(dataResources, globalTemplateObjects.merge(resolveFromTestClass(reusableDataSetClass)))       //
        );
    }

    protected abstract MetaAnnotationHandler<T> metaAnnotationHandler(List<DataResource> dataResources, TemplateObjects templateObjects);

    private List<Class<? extends Use.ReusableDataSet>> filterDuplicatedReusableDataSetClasses(Use annotation) {
        final List<Class<? extends Use.ReusableDataSet>> filteredDataSetClasses=new LinkedList<>();
        final Set<Class<? extends Use.ReusableDataSet>> uniqueReusableDataSets=new HashSet<>();
        for (Class<? extends Use.ReusableDataSet> reusableDataSet : annotation.value()) {
            if( uniqueReusableDataSets.add(reusableDataSet) ) {
                filteredDataSetClasses.add(reusableDataSet);
            }
        }
        return filteredDataSetClasses;
    }


}
