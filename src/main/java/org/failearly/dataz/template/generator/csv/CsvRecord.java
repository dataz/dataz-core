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
