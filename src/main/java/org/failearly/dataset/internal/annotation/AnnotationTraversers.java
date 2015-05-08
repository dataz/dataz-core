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

import org.failearly.dataset.internal.annotation.filter.AnnotationFilters;
import org.failearly.dataset.internal.annotation.resolver.AnnotationResolver;
import org.failearly.dataset.internal.annotation.resolver.AnnotationResolvers;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;

/**
 * AnnotationTraversers provides {@link org.failearly.dataset.internal.annotation.AnnotationTraverser} implementations.
 */
public final class AnnotationTraversers {

    private AnnotationTraversers() {
    }

    /**
     * Creates a {@link org.failearly.dataset.internal.annotation.AnnotationTraverser} for specified {@code annotationClass}.
     *
     * @param annotationClass the annotationClass
     * @param traverseStrategy the traversing direction.
     * @param order the order (unchanged or reverted)
     * @param <T> an annotation type
     * @return the annotation traverser
     */
    public static <T extends Annotation> AnnotationTraverser<T> createAnnotationTraverser(Class<T> annotationClass, TraverseStrategy traverseStrategy, Order order) {
        final AnnotationResolver<T> resolver = doCreateAnnotationResolver(annotationClass);
        return doCreateAnnotationTraverser(traverseStrategy, order, resolver);
    }

    /**
     * Creates a {@link org.failearly.dataset.internal.annotation.AnnotationTraverser} for specified {@code metaAnnotationClass}. A meta annotation is a annotation
     * which could be applied only on an annotation. So it's not possible to specify the type of the actually annotation.
     *
     * @param metaAnnotationClass the meta annotation.
     * @param traverseStrategy the traversing direction.
     * @param order the order (unchanged or reverted)
     *
     * @return the meta annotation traverser
     *
     * @see org.failearly.dataset.generator.support.GeneratorFactoryDefinition
     * @see org.failearly.dataset.datastore.DataStoreFactoryDefinition
     */
    public static AnnotationTraverser<Annotation> createMetaAnnotationTraverser(Class<? extends Annotation> metaAnnotationClass, TraverseStrategy traverseStrategy, Order order) {
        final AnnotationResolver<Annotation> resolver = doCreateMetaAnnotationResolver(metaAnnotationClass);
        return doCreateAnnotationTraverser(traverseStrategy, order, resolver);
    }

    private static <T extends Annotation> AnnotationTraverser<T> doCreateAnnotationTraverser(TraverseStrategy traverseStrategy, Order order, AnnotationResolver<T> resolver) {
        final AnnotationTraverser<T> annotationTraverser = doCreateAnnotationTraverserByTraverseDirection(resolver, traverseStrategy);
        return decorateByOrder(order, annotationTraverser);
    }

    private static AnnotationResolver<Annotation> doCreateMetaAnnotationResolver(Class<? extends Annotation> metaAnnotationClass) {
        return AnnotationResolvers.metaAnnotation(metaAnnotationClass);
    }

    private static <T extends Annotation> AnnotationResolver<T> doCreateAnnotationResolver(Class<T> annotationClass) {
        if (isRepeatableAnnotation(annotationClass)) {
            return AnnotationResolvers.repeatable(annotationClass, AnnotationFilters.acceptAll());
        }

        return AnnotationResolvers.noneRepeatable(annotationClass, AnnotationFilters.isInstance(annotationClass));
    }

    private static <T extends Annotation> boolean isRepeatableAnnotation(Class<T> annotationClass) {
        return annotationClass.isAnnotationPresent(Repeatable.class);
    }

    private static <T extends Annotation> AnnotationTraverser<T> decorateByOrder(Order order, AnnotationTraverser<T> annotationTraverser) {
        if( order==Order.UNCHANGED ) {
            return annotationTraverser;
        }

        return new ReverseAnnotationTraverser<>(annotationTraverser);
    }

    private static <T extends Annotation> AnnotationTraverser<T> doCreateAnnotationTraverserByTraverseDirection(
            AnnotationResolver<T> resolver,
            TraverseStrategy traverseStrategy
        ) {
        if( traverseStrategy == TraverseStrategy.BOTTOM_UP ) {
            return new BottomUpAnnotationTraverser<>(resolver);
        }
        return new TopDownAnnotationTraverser<>(resolver);
    }
}