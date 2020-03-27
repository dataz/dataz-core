/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.common.annotation.traverser;

import org.failearly.dataz.internal.common.annotation.resolver.AnnotationResolver;

import java.lang.annotation.Annotation;

/**
 * AnnotationTraverserFactory is responsible for creating the actually {@link AnnotationTraverser} instance.
 */
public final class AnnotationTraverserFactory {
    private AnnotationTraverserFactory() {
    }

    /**
     * Creates an {@link AnnotationTraverser} instance appropriate to the {@code traverseStrategy}.
     * @param <T> the impl type
     * @param traverseStrategy the traverse strategy
     * @param traverseDepth the traverse depth
     * @param resolver the resolver instance
     * @return the {@link AnnotationTraverser}
     */
    public static <T extends Annotation> AnnotationTraverser<T> createAnnotationTraverser(
            TraverseStrategy traverseStrategy,
            TraverseDepth traverseDepth,
            AnnotationResolver<T> resolver
    ) {
        return doCreateAnnotationTraverser(TraverserType.STANDARD, traverseStrategy, traverseDepth, resolver);

    }

    /**
     * Creates a {@link MetaAnnotationTraverser} instance appropriate to the {@code traverseStrategy}.
     * @param <T> the impl type
     * @param traverseStrategy the traverse strategy
     * @param traverseDepth the traverse depth
     * @param resolver the resolver instance
     * @return the {@link AnnotationTraverser}
     */
    public static <T extends Annotation> MetaAnnotationTraverser<T> createMetaAnnotationTraverser(
            TraverseStrategy traverseStrategy,
            TraverseDepth traverseDepth,
            AnnotationResolver<T> resolver
    ) {
        return doCreateAnnotationTraverser(TraverserType.META, traverseStrategy, traverseDepth, resolver);
    }

    private static <T extends Annotation> AnnotationTraverserBase<T> doCreateAnnotationTraverser(
            TraverserType traverserType,
            TraverseStrategy traverseStrategy,
            TraverseDepth traverseDepth,
            AnnotationResolver<T> resolver) {
        if (traverseStrategy == TraverseStrategy.BOTTOM_UP) {
            return new BottomUpAnnotationTraverser<>(resolver, traverseDepth, traverserType);
        }
        return new TopDownAnnotationTraverser<>(resolver, traverseDepth, traverserType);
    }
}
