/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.simplefile;

import org.failearly.dataset.exception.DataSetException;

/**
 * Thrown by DataSet in case of any exception while parsing.
 */
public class DataSetParseException extends DataSetException {
    public DataSetParseException(Exception ex) {
        super("DataSet parse exception", ex);
    }

    public DataSetParseException(String message, Exception ex) {
        super("DataSet parse exception: " + message, ex);
    }
}
