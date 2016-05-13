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

/**
 * Base classes/interfaces and necessary classes for Template Objects. A <i>Template Object</i> is an object which could
 * be used in a template. The default template engine is Velocity.
 * <br><br>
 * Any full implementation of a template objects consists of <br><br>
 * <ul>
 *    <li>The implementation of {@link org.failearly.dataz.template.TemplateObject},</li>
 *    <li>The factory implementation of {@link org.failearly.dataz.template.TemplateObjectFactory} and </li>
 *    <li>the <i>template object annotation</i>. The template object annotation is the public interface of a template object.
 *          This annotation must use {@link org.failearly.dataz.template.TemplateObjectFactory.Definition#value()} for
 *          assigning the factory to the template object annotation.</li>
 * </ul>
 *
 * @see org.failearly.dataz.template.TemplateObject
 */
package org.failearly.dataz.template;