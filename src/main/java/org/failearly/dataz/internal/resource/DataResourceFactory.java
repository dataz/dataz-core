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

package org.failearly.dataz.internal.resource;

import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.resource.DataResourceValues;

/**
 * DataResourceFactory provides utility methods for creating {@link DataResource}.
 */
public final class DataResourceFactory {
    private DataResourceFactory() {
    }

    public static DataResource createStandardInstance(DataResourceValues dataResourceValues) {
        return new StandardDataResource(dataResourceValues);
    }

    public static DataResource createTemplateInstance(DataResourceValues dataResourceValues) {
        return new TemplateDataResource(dataResourceValues);
    }

    public static DataResource createMissingResourceInstance(DataResourceValues dataResourceValues) {
        return new MissingDataResource(dataResourceValues);
    }

    public static DataResource createIgnoringInstance(DataResourceValues dataResourceValues) {
        return new IgnoringDataResource(dataResourceValues);
    }

}
