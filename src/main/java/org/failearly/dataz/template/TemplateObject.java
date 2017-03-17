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

package org.failearly.dataz.template;

import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.template.engine.TemplateEngine;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * TemplateObject is a named object which could be used in {@link DataResource} templates. The template object
 * could be used within any {@link DataResource} with the same dataz name or if the {@link #scope()} of the template object
 * is {@link Scope#GLOBAL}.
 * <br><br>
 * Remark: Please use {@link TemplateObjectBase} instead of implementing this interface.
 *
 *
 * @see TemplateEngine
 * @see DataResource
 * @see Scope
 */
public interface TemplateObject {
    /**
     * The annotation object, the template object has been created from.
     * @return the annotation.
     */
    Annotation getAnnotation();

    /**
     * The Template Object's annotation context hold the object where the actually annotation has been assigned to.
     *
     * Some template objects need access to the element has been applied to. For example to load resources.
     *
     * @return the context object.
     */
    TemplateObjectAnnotationContext getContext();

    /**
     * The name will be used within a (Velocity) template as {@code $name} or {@code ${name}}.
     * So be carefully with the name.
     *
     *
     * *Caution*: Not every name will be usable by a template engine.
     *          (Visit http://velocity.apache.org/engine/devel/user-guide.html)
     *
     * @return  the name of the template object.
     */
    String name();

    /**
     * The name of the (associated dataz).
     *
     * <b>Remarks</b>:<br><br>
     * <ul>
     *    <li>If there is no dataz with given (i.e.) {@link org.failearly.dataz.DataSet#name()},
     *        the template object will not be used, even when the name has been used within the Velocity template.</li>
     *    <li>Used only if the {@link #scope()} is {@link Scope#LOCAL}. Otherwise ignored.</li>
     * </ul>
     *
     *
     * @return the name of the (associated dataz).
     *
     * @see org.failearly.dataz.DataSet#name()
     * @see org.failearly.dataz.DataSetup#name()
     * @see org.failearly.dataz.DataCleanup#name()
     * @see Scope#LOCAL
     */
    Set<String> datasets();

    /**
     * @return The scope's value ({@link Scope#getScopeValue()}) of a template object.
     *
     * @see Scope
     * @see Scope#DEFAULT
     * @see Scope#getScopeValue()
     */
    Scope scope();

    @SuppressWarnings("unused")
    void __extend_TemplateObjectBase__instead_of_implementing_TemplateObject();

}
