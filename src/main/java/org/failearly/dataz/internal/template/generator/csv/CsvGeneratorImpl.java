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
package org.failearly.dataz.internal.template.generator.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.failearly.common.annotations.Tests;
import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.template.TemplateObjectAnnotationContext;
import org.failearly.dataz.template.generator.csv.CsvGenerator;
import org.failearly.dataz.template.generator.csv.CsvProperties;
import org.failearly.dataz.template.generator.csv.CsvRecord;
import org.failearly.dataz.template.generator.support.LimitedGeneratorBase;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CsvGeneratorImpl implements {@link CsvGenerator}.
 */
@Tests("CsvGeneratorTest")
public class CsvGeneratorImpl extends LimitedGeneratorBase<CsvRecord> {
    
    private List<CsvRecord> records;

    CsvGeneratorImpl(CsvGenerator annotation, TemplateObjectAnnotationContext context) {
        super(annotation, context);
    }

    @Override
    protected void doInit() {
        super.doInit();
        try {
            final CSVFormat format = new CsvFormatResolver(csvProperties()).resolve().getCsvFormat();
            final CSVParser parser = new CSVParser(loadResourceAsReader(getFileAttribute()), format);
            this.records = toCsvRecord(parser.getRecords());
        } catch (IOException ex) {
            throw new DataSetException("Can't load resource", ex);
        }
    }

    private CsvProperties csvProperties() {
        return getAnnotation(CsvGenerator.class).properties();
    }

    private List<CsvRecord> toCsvRecord(List<CSVRecord> records) {
        final CsvProperties csvProperties = csvProperties();
        final boolean ignoreHeaderCase = csvProperties.ignoreColumnNamesCase().getBoolean().orElse(false);
        final String nullString = csvProperties.nullString();
        return records.stream().map((record) -> new CsvRecordImpl(record, ignoreHeaderCase, nullString)).collect(Collectors.toList());
    }

    private Reader loadResourceAsReader(String resource) {
        return getContext() //
            .loadResourceAsReader(getFileAttribute()) //
            .orElseThrow(() -> new DataSetException("Could not load resource '" + resource + "'!"));
    }


    private String getFileAttribute() {
        return getAnnotation(CsvGenerator.class).file();
    }

    @Override
    public Iterator<CsvRecord> createIterator() {
        return records.iterator();
    }
}
