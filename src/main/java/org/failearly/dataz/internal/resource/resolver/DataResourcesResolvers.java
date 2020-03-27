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
package org.failearly.dataz.internal.resource.resolver;

import org.failearly.dataz.internal.common.annotation.traverser.TraverseDepth;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.resource.DataResource;

/**
 * DataResourcesResolvers provides {@link DataResourcesResolver} implementations.
 */
public abstract class DataResourcesResolvers {
    private DataResourcesResolvers() {
    }

    /**
     * Create a {@link DataResource} resolver for setup resources.
     * @param traverseDepth the traverseDepth
     * @return the setup {@link DataResource} resolver
     *
     * @see DataSet#setup()
     */
    public static DataResourcesResolver setupDataResourcesResolver(TraverseDepth traverseDepth) {
        return new SetupDataResourcesResolver(traverseDepth);
    }

    /**
     * Create a {@link DataResource} resolver for cleanup resources.
     * @param traverseDepth the traverseDepth
     * @return the cleanup {@link DataResource} resolver
     *
     * @see DataSet#cleanup()
     */
    public static DataResourcesResolver cleanupDataResourcesResolver(TraverseDepth traverseDepth) {
        return new CleanupDataResourcesResolver(traverseDepth);
    }
}
