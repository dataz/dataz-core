/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.failearly.dataz.internal.resource.factory.use;

import org.failearly.common.annotation.traverser.*;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.Use;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.internal.template.TemplateObjectsResolver;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DelegateDataResource;
import org.failearly.dataz.resource.TypedDataResourcesFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * DataSetResourcesFactoryBase is the base class for {@link DataSet} based {@link DataResource}s factory classes.
 */
abstract class UseResourcesFactoryBase<T extends Annotation> extends TypedDataResourcesFactory<Use> {

    private static final TemplateObjectsResolver templateObjectsResolver= TemplateObjectsResolver.withStandardSettings();
    private static final ThreadLocal<Integer> nestedUseCounter = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    private final Class<T> metaAnnotationClass;


    UseResourcesFactoryBase(Class<T> metaAnnotationClass) {
        super(Use.class);
        this.metaAnnotationClass = metaAnnotationClass;
    }

    @Override
    protected List<DataResource> doCreateDataResources(Use annotation, TemplateObjects templateObjects) {
        final List<DataResource> dataResources = new LinkedList<>();
        final List<Class<? extends Use.ReusableDataSet>> reusableDataSetClasses = filterDuplicatedReusableDataSetClasses(annotation);
        doResolveDataResourcesFromAllReusableDataSets(reusableDataSetClasses, dataResources, templateObjects.filterGlobalScope());
        return overwriteDataStores(changeOrder(dataResources), annotation);
    }

    private List<DataResource> overwriteDataStores(List<DataResource> dataResources, Use annotation) {
        final Class<? extends NamedDataStore>[] datastores = annotation.datastores();
        if( datastores.length==0 || isNestedUse() )
            return dataResources;

        final List<Class<? extends NamedDataStore>> namedDataStores=distinctNamedDataStores(datastores);

        return dataResources.stream()
                .flatMap((ds)-> generateDataResourcePerNamedDataStore(ds, namedDataStores))
                .collect(Collectors.toList());
    }

    private static List<Class<? extends NamedDataStore>> distinctNamedDataStores(Class<? extends NamedDataStore>[] namedDataStores) {
        return Arrays.stream(namedDataStores).distinct().collect(Collectors.toList());
    }

    private Stream<DataResource> generateDataResourcePerNamedDataStore(DataResource dataResource, List<Class<? extends NamedDataStore>> namedDataStores) {
        return namedDataStores.stream().map((nds)->new OverwriteAssignedDataStores(dataResource, nds));
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
            reusableDataSetClasses.forEach(
                    reusableDataSetClass -> doCreateDataResourcesFromSingleReusableDataSet(reusableDataSetClass, dataResources, globalTemplateObjects)
            );
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
                metaAnnotationHandler(dataResources, globalTemplateObjects.merge(                  //
                        templateObjectsResolver.resolveFromClass(reusableDataSetClass)             //
                    )                //
                )                    //
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

    private static class OverwriteAssignedDataStores extends DelegateDataResource {

        private final Class<? extends NamedDataStore> namedDataStore;


        private OverwriteAssignedDataStores(DataResource dataResource, Class<? extends NamedDataStore> namedDataStore) {
            super(dataResource);
            this.namedDataStore = namedDataStore;
        }

        @Override
        public Class<? extends NamedDataStore> getNamedDataStore() {
            return namedDataStore;
        }
    }

}
