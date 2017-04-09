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


package org.failearly.dataz.internal.common.annotation.traverser

import spock.lang.Specification

import java.lang.annotation.Annotation
/**
 * AnnotationTraverserSpecBase contains specification for ... .
 */
abstract class AnnotationTraverserSpecBase<T extends Annotation> extends Specification {
    protected static final NO_ANNOTATIONS = Collections.emptyList()

    protected static AnnotationHandler<Annotation> createAnnotationHandler(List annotationList) {
        new AnnotationHandlerBase<Annotation>() {
            @Override
            void handleAnnotation(Annotation annotation) {
                annotationList.add new AnnotationValue(annotation)
            }
        }
    }

    protected static <T extends Annotation> MetaAnnotationHandler<T> createMetaAnnotationHandler(Class<T> metaAnnotationClass, List annotationList, Collection metaAnnotationList) {
        new MetaAnnotationHandlerBase<T>() {
            @Override
            void handleMetaAnnotation(Annotation annotation, T metaAnnotation) {
                annotationList.add new AnnotationValue(annotation)
                metaAnnotationList.add new AnnotationValue(metaAnnotation)
            }
        }
    }

    protected static List<Annotation> expected(Class<? extends Annotation> annotationClass, def expectedAnnotationIds) {
        expectedAnnotationIds.inject([]) { List list, def annotationId ->
            list.add new AnnotationValue(annotationClass, annotationId)
            return list
        }
    }

    protected static boolean haveExpectedAnnotationTypes(List<AnnotationValue> collectedAnnotations, Class<?>... annotations) {
        collectedAnnotations.every {
            it.isExpectedAnnotation(annotations)
        }
    }

    protected static List<String> extractIds(List<AnnotationValue> collectedAnnotations) {
        collectedAnnotations.collect {
            it.id
        }
    }

    static class AnnotationValue {
        private final Class<? extends Annotation> annotationClass

        private final String id

        /**
         * Constructor for expected annotations.
         */
        AnnotationValue(Class<? extends Annotation> annotationClass, def id) {
            this.annotationClass = annotationClass
            this.id = ""+id
        }

        /**
         * Constructor for the actually impl
         */
        AnnotationValue(Annotation annotation) {
            this.annotationClass = annotation.annotationType()
            this.id = annotation.id()
        }

        String getId() {
            return id
        }

        boolean isExpectedAnnotation(Class... expected) {
            expected.any { it == annotationClass }
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (!(o instanceof AnnotationValue)) return false

            AnnotationValue that = (AnnotationValue) o

            if (annotationClass != that.annotationClass) return false
            if (id != that.id) return false

            return true
        }

        int hashCode() {
            int result = annotationClass.hashCode()
            result = 31 * result + id.hashCode()
            return result
        }


        @Override
        public String toString() {
            return "@"+annotationClass.getSimpleName() + "(id=" + id +")";
        }


    }
}