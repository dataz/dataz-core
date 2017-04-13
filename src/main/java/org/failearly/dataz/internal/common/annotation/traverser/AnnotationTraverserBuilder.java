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

package org.failearly.dataz.internal.common.annotation.traverser;

import org.failearly.dataz.internal.common.annotation.filter.AnnotationFilters;
import org.failearly.dataz.internal.common.annotation.resolver.AnnotationResolver;
import org.failearly.dataz.internal.common.annotation.resolver.AnnotationResolvers;
import org.failearly.dataz.internal.common.builder.BuilderBase;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * AnnotationTraverserBuilder provides builders for {@link AnnotationTraverser} or {@link MetaAnnotationTraverser}.
 *
 * @see #metaAnnotationTraverser(Class)
 * @see #annotationTraverser(Class)
 */
@SuppressWarnings({"WeakerAccess"})
public abstract class AnnotationTraverserBuilder<T extends Annotation, AT> extends BuilderBase<AT> {

    final Class<T> annotationClass;
    TraverseStrategy traverseStrategy;
    TraverseDepth traverseDepth;

    private AnnotationTraverserBuilder(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Build a Meta Annotation traverser with meta impl {@code metaAnnotation}.
     *
     * @param metaAnnotation the meta impl class
     * @param <T>            the meta impl type
     * @return the {@link MetaAnnotationBuilder}
     */
    public static <T extends Annotation> MetaAnnotationBuilder<T> metaAnnotationTraverser(Class<T> metaAnnotation) {
        Objects.requireNonNull(metaAnnotation, "Please provide a meta impl NOT NULL.");
        return new MetaAnnotationBuilder<>(metaAnnotation);
    }


    /**
     * Build a Standard Annotation traverser with impl {@code impl}.
     *
     * @param annotation the impl class
     * @param <T>        the impl type
     * @return the {@link MetaAnnotationBuilder}
     */
    public static <T extends Annotation> StandardAnnotationBuilder<T> annotationTraverser(Class<T> annotation) {
        Objects.requireNonNull(annotation, "Please provide an impl NOT NULL.");
        return new StandardAnnotationBuilder<>(annotation);
    }

    public final AnnotationTraverserBuilder<T, AT> withTraverseStrategy(TraverseStrategy traverseStrategy) {
        this.traverseStrategy = traverseStrategy;
        return this;
    }

    public final AnnotationTraverserBuilder<T, AT> withTraverseDepth(TraverseDepth traverseDepth) {
        this.traverseDepth = traverseDepth;
        return this;
    }

    @Override
    protected final void checkMandatoryFields() {
        Objects.requireNonNull(this.traverseStrategy, "Please provide a traverse strategy.");
        Objects.requireNonNull(this.traverseDepth, "Please provide a traverse depth.");
    }

    /**
     * Check if any impl (of specified type) is available on {@code method} and so on depending of the current
     * {@link TraverseDepth}.
     *
     * @param method the method to be checked
     * @return  if the impl is available returns {@code true} otherwise {@code false}.
     */
    public abstract boolean anyAnnotationAvailable(Method method);

    /**
     * Check if any impl (of specified type) is available on {@code clazz} and so on depending of the current
     * {@link TraverseDepth}.
     *
     * @param clazz the class to be checked
     * @return  if the impl is available returns {@code true} otherwise {@code false}.
     */
    public abstract boolean anyAnnotationAvailable(Class<?> clazz);


    public final static class MetaAnnotationBuilder<T extends Annotation> extends AnnotationTraverserBuilder<T, MetaAnnotationTraverser<T>> {
        private MetaAnnotationBuilder(Class<T> metaAnnotationClass) {
            super(metaAnnotationClass);
        }

        @Override
        public boolean anyAnnotationAvailable(Class<?> clazz) {
            return build().anyAnnotationAvailable(clazz);
        }

        @Override
        public boolean anyAnnotationAvailable(Method method) {
            return  build().anyAnnotationAvailable(method);
        }

        @Override
        protected final MetaAnnotationTraverser<T> doBuild() {
            return AnnotationTraverserFactory.createMetaAnnotationTraverser(
                    traverseStrategy,
                    traverseDepth,
                    AnnotationResolvers.metaAnnotation(annotationClass)
            );
        }

    }

    public final static class StandardAnnotationBuilder<T extends Annotation>
            extends AnnotationTraverserBuilder<T, AnnotationTraverser<T>> {
        private StandardAnnotationBuilder(Class<T> annotationClass) {
            super(annotationClass);
        }

        @Override
        public boolean anyAnnotationAvailable(Class<?> clazz) {
            return build().anyAnnotationAvailable(clazz);
        }

        @Override
        public boolean anyAnnotationAvailable(Method method) {
            return  build().anyAnnotationAvailable(method);
        }

        @Override
        protected AnnotationTraverser<T> doBuild() {
            return AnnotationTraverserFactory.createAnnotationTraverser(
                    traverseStrategy,
                    traverseDepth, doCreateAnnotationResolver()
            );
        }

        private AnnotationResolver<T> doCreateAnnotationResolver() {
            if (isRepeatableAnnotation()) {
                return AnnotationResolvers.repeatable(annotationClass, AnnotationFilters.acceptAll());
            }

            return AnnotationResolvers.noneRepeatable(annotationClass, AnnotationFilters.isInstance(annotationClass));
        }

        private boolean isRepeatableAnnotation() {
            return annotationClass.isAnnotationPresent(Repeatable.class);
        }
    }
}
