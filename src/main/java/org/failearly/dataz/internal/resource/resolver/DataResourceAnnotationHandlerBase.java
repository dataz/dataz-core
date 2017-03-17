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

package org.failearly.dataz.internal.resource.resolver;

import org.failearly.common.annotation.traverser.MetaAnnotationHandlerBase;
import org.failearly.dataz.internal.template.TemplateObjects;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourcesFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * DataResourceAnnotationHandlerBase is the base class for handling DataResource Annotations.
 */
abstract class DataResourceAnnotationHandlerBase<T extends Annotation> extends MetaAnnotationHandlerBase<T> {
    private final List<DataResource> dataResourceList;
    private final TemplateObjects templateObjects;

    protected DataResourceAnnotationHandlerBase(TemplateObjects templateObjects, List<DataResource> dataResourceList) {
        this.templateObjects = templateObjects;
        this.dataResourceList = dataResourceList;
    }

    protected DataResourceAnnotationHandlerBase(TemplateObjects templateObjects) {
        this(templateObjects, new LinkedList<>());
    }

    public final List<DataResource> getDataResourceList() {
        return dataResourceList;
    }

    @Override
    public void handleMetaClassAnnotation(Class<?> clazz, Annotation annotation, T metaAnnotation) {
        final DataResourcesFactory dataResourcesFactory = createDataResourceFactory(metaAnnotation);
        dataResourceList.addAll(dataResourcesFactory.createDataResources(clazz, annotation, templateObjects));
    }

    @Override
    public void handleMetaMethodAnnotation(Method method, Annotation annotation, T metaAnnotation) {
        final DataResourcesFactory dataResourcesFactory = createDataResourceFactory(metaAnnotation);
        dataResourceList.addAll(dataResourcesFactory.createDataResources(method, annotation, templateObjects));
    }

    protected abstract DataResourcesFactory createDataResourceFactory(T metaAnnotation);
}
