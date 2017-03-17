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

package org.failearly.dataz.common;

/**
 * Property for properties element in annotations (i.e. {@link org.failearly.dataz.template.simple.Adhoc}).
 *
 * @see org.failearly.dataz.template.simple.Adhoc
 * @see org.failearly.dataz.template.simple.support.AdhocTemplateObjectBase#getProperties()
 */
public @interface Property {
    /**
     * @return the key
     */
    String k();

    /**
     * @return the value
     */
    String v();
}
