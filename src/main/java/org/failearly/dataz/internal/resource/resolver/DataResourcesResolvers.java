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
