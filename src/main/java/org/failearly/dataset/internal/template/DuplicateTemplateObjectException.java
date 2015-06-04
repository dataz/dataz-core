/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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

package org.failearly.dataset.internal.template;

import org.failearly.dataset.exception.DataSetException;
import org.failearly.dataset.template.TemplateObject;

/**
 * DuplicateTemplateObjectException used by {@link TemplateObjectDuplicateStrategy#STRICT}.
 */
public class DuplicateTemplateObjectException extends DataSetException {
    public DuplicateTemplateObjectException(TemplateObject templateObject) {
        super("Duplicate template object found: " + templateObject);
    }
}
