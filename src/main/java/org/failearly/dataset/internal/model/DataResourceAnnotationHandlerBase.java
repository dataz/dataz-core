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

package org.failearly.dataset.internal.model;

import org.failearly.dataset.internal.annotation.AnnotationHandlerBase;
import org.failearly.dataset.internal.annotation.AnnotationUtils;
import org.failearly.dataset.internal.template.TemplateObjects;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.resource.DataResourcesFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * DataResourceAnnotationHandlerBase is the base class for handling DataResource Annotations.
 */
public abstract class DataResourceAnnotationHandlerBase<T extends Annotation> extends AnnotationHandlerBase<Annotation> {
    private final List<DataResource> dataResourceList;
    private final TemplateObjects generatorCreators;
    private final Class<T> metaAnnotationClass;

    protected DataResourceAnnotationHandlerBase(Class<T> metaAnnotationClass, TemplateObjects generatorCreators, List<DataResource> dataResourceList) {
        this.metaAnnotationClass = metaAnnotationClass;
        this.generatorCreators = generatorCreators;
        this.dataResourceList = dataResourceList;
    }

    @Override
    public final void handleClassAnnotation(Class<?> clazz, Annotation annotation) {
        final DataResourcesFactory dataResourcesFactory = getDataResourceFactory(annotation);
        dataResourceList.addAll(dataResourcesFactory.createDataResources(clazz, annotation, generatorCreators));
    }

    @Override
    public final void handleMethodAnnotation(Method method, Annotation annotation) {
        final DataResourcesFactory dataResourcesFactory = getDataResourceFactory(annotation);
        dataResourceList.addAll(dataResourcesFactory.createDataResources(method, annotation, generatorCreators));
    }

    private DataResourcesFactory getDataResourceFactory(Annotation annotation) {
        final T metaAnnotation = AnnotationUtils.getMetaAnnotation(metaAnnotationClass, annotation);
        return createDataResourceFactory(metaAnnotation);
    }

    protected abstract DataResourcesFactory createDataResourceFactory(T metaAnnotation);
}
