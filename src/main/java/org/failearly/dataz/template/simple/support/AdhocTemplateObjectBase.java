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

package org.failearly.dataz.template.simple.support;

import org.failearly.dataz.template.support.PropertiesHelperFactory;
import org.failearly.dataz.template.TemplateObjectBase;
import org.failearly.dataz.template.simple.Adhoc;
import org.failearly.common.test.PropertiesHelper;

import java.util.*;

/**
 * AdhocTemplateObjectBase is the base class for all {@link org.failearly.dataz.template.simple.Adhoc.AdhocTemplateObject} implementations.
 */
public abstract class AdhocTemplateObjectBase extends TemplateObjectBase implements Adhoc.AdhocTemplateObject {
    private List<String> arguments = Collections.emptyList();
    private PropertiesHelper propertiesHelper;

    protected AdhocTemplateObjectBase() {}

    protected AdhocTemplateObjectBase(Adhoc annotation) {
        super(annotation);
        this.arguments = Arrays.asList(annotation.args());
        this.propertiesHelper = PropertiesHelperFactory.createFromPropertyList(annotation.properties());
    }

    /**
     * @return the entire {@link Adhoc#args()}.
     */
    public final List<String> getArguments() {
        return new ArrayList<>(arguments);
    }

    /**
     * @return the entire {@link Adhoc#properties()} as {@link PropertiesHelper} instance.
     */
    public final PropertiesHelper getProperties() {
        return propertiesHelper;
    }

    @Override
    public final void ___extend_AdhocTemplateObjectBase__instead_of_implementing_AdhocTemplateObject() {
        throw new UnsupportedOperationException("___extend_AdhocTemplateObjectBase__instead_of_implementing_AdhocTemplateObject must not be called");
    }
}
