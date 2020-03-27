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

/**
 * Base classes/interfaces and necessary classes for Template Objects. A <i>Template Object</i> is an object which could
 * be used in a template. The default template engine is Velocity.
 * <br><br>
 * Any full implementation of a template objects consists of <br><br>
 * <ul>
 *    <li>The implementation of {@link org.failearly.dataz.template.TemplateObject},</li>
 *    <li>The factory implementation of {@link org.failearly.dataz.template.TemplateObjectFactory} and </li>
 *    <li>the <i>template object impl</i>. The template object impl is the public interface of a template object.
 *          This impl must use {@link org.failearly.dataz.template.TemplateObjectFactory.Definition#value()} for
 *          assigning the factory to the template object impl.</li>
 * </ul>
 *
 * @see org.failearly.dataz.template.TemplateObject
 */
package org.failearly.dataz.template;