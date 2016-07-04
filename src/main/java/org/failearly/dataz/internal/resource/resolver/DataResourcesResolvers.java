/*
 * Copyright (c) 2009.
 *
 * Date: 31.05.16
 * 
 */
package org.failearly.dataz.internal.resource.resolver;

import org.failearly.common.annotation.traverser.TraverseDepth;
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
