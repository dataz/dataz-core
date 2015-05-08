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

package org.failearly.dataset.internal.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Top down implementation of AnnotationTraverser.
 */
final class ReverseAnnotationTraverser<T extends Annotation> implements AnnotationTraverser<T> {
    private final AnnotationTraverser<T> annotationTraverser;

    ReverseAnnotationTraverser(AnnotationTraverser<T> annotationTraverser) {
        this.annotationTraverser = annotationTraverser;
    }


    @Override
    public void traverse(Method method, AnnotationHandler<T> annotationHandler) {
        final List<Pair<T>> annotations=new LinkedList<>();
        annotationTraverser.traverse(method, createCollectingHandler(annotations));
        doApplyCollectedAnnotations(annotations, annotationHandler);
    }

    @Override
    public void traverse(Class<?> clazz, AnnotationHandler<T> annotationHandler) {
        final List<Pair<T>> annotations=new LinkedList<>();
        annotationTraverser.traverse(clazz, createCollectingHandler(annotations));
        doApplyCollectedAnnotations(annotations, annotationHandler);
    }

    private void doApplyCollectedAnnotations(List<Pair<T>> annotations, AnnotationHandler<T> annotationHandler) {
        Collections.reverse(annotations);
        annotations.forEach((pair)->pair.applyHandler(annotationHandler));
    }

    private static <T extends Annotation> AnnotationHandler<T> createCollectingHandler(final List<Pair<T>> annotations) {
        return new AnnotationHandlerBase<T>() {
            @Override
            public void handleClassAnnotation(Class<?> clazz, T annotation) {
                annotations.add(new Pair<>(clazz, annotation));
            }

            @Override
            public void handleMethodAnnotation(Method method, T annotation) {
                annotations.add(new Pair<>(method, annotation));
            }
        };
    }

    private static class Pair<T> {
        private final AnnotatedElement annotatedElement;
        private final T annotation;

        private Pair(AnnotatedElement annotatedElement, T annotation) {
            this.annotatedElement = annotatedElement;
            this.annotation = annotation;
        }

        void applyHandler(AnnotationHandler<T> annotationHandler) {
            Objects.requireNonNull(annotatedElement, "Should be never null");
            if( annotatedElement instanceof Method ) {
                annotationHandler.handleMethodAnnotation((Method) annotatedElement, annotation);
            }
            else {
                annotationHandler.handleClassAnnotation((Class<?>) annotatedElement, annotation);
            }
        }
    }
}
