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
