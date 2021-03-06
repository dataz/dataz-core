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

package org.failearly.dataz.template.simple.support;

import org.failearly.dataz.internal.common.proputils.PropertiesAccessor;
import org.failearly.dataz.common.PropertyUtility;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.simple.Adhoc;

import java.util.*;

/**
 * AdhocTemplateObjectBase is the base class for all {@link org.failearly.dataz.template.simple.Adhoc.AdhocTemplateObject} implementations.
 */
public abstract class AdhocTemplateObjectBase extends TemplateObjectBase implements Adhoc.AdhocTemplateObject {
    private List<String> arguments = Collections.emptyList();
    private PropertiesAccessor propertiesAccessor;

    protected AdhocTemplateObjectBase() {}

    protected AdhocTemplateObjectBase(Adhoc annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
        this.arguments = Arrays.asList(annotation.args());
        this.propertiesAccessor = PropertyUtility.toPropertyAccessor(annotation.properties());
    }

    /**
     * @return the entire {@link Adhoc#args()}.
     */
    public final List<String> getArguments() {
        return new ArrayList<>(arguments);
    }

    /**
     * @return the entire {@link Adhoc#properties()} as {@link PropertiesAccessor} instance.
     */
    public final PropertiesAccessor getProperties() {
        return propertiesAccessor;
    }

    @Override
    public final void ___extend_AdhocTemplateObjectBase__instead_of_implementing_AdhocTemplateObject() {
        throw new UnsupportedOperationException("___extend_AdhocTemplateObjectBase__instead_of_implementing_AdhocTemplateObject must not be called");
    }
}
