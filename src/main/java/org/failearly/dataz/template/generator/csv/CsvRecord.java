/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2017 'Marko Umek' (http://fail-early.com)
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
package org.failearly.dataz.template.generator.csv;

/**
 * CsvRecord is responsible for ...
 */
public interface CsvRecord {
    /**
     * The record's number
     * @return the record number.
     */
    long getRecordNumber();

    /**
     * Access Record's value by index.
     * @param index column index
     * @return the value
     */
    String getValue(int index);

    /**
     * Access Record's column by name.
     * @param columnName column name
     * @return the value (as String)
     */
    String getValue(String columnName);
}
