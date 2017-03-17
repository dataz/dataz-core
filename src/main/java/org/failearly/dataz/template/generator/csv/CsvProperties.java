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

import org.failearly.common.annotations.utils.OptBoolean;
import org.failearly.common.annotations.utils.OptCharacter;
import org.failearly.common.annotations.utils.OptEnum;
import org.failearly.common.annotations.utils.OptString;
import org.failearly.dataz.internal.template.generator.csv.NoCustomCsvFormat;

import java.lang.annotation.*;

/**
 * CsvProperties is responsible for ...
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CsvProperties {
    /**
     * Use one of the predefined Formats.
     *
     * @return a predefined formats.
     */
    PredefinedCsvFormat predefined() default PredefinedCsvFormat.DEFAULT;

    /**
     * Adapt the predefined format.
     *
     * @return a custom format.
     */
    Class<? extends CustomCsvFormat> custom() default NoCustomCsvFormat.class;

    /**
     * Set the (optional) column separator.
     * 
     * @return the column separator
     */
    OptCharacter columnSeparator() default @OptCharacter;

    /**
     * @return user defined column names (default none).
     */
    String[] columnNames() default {};

    /**
     * Use a enumeration class for setting the {@link #columnNames()} - default none.
     * @return (optional) user defined {@link Enum} class.
     */
    OptEnum columnEnum() default @OptEnum;

    /**
     * Should be set to true, if the _column names_ should be read from the CSV file.
     *
     * Ignore if {@link #columnNames()} has been used.
     *
     * @return {@code true} first record will be used as columnNames.
     */
    OptBoolean firstRecordAsHeader() default OptBoolean.DEFAULT;

    /**
     * Ignore the column names case (default {@code false}).
     *
     * @return {@code true}: ignore lower or upper case, otherwise {@code false}.
     */
    OptBoolean ignoreColumnNamesCase() default OptBoolean.DEFAULT;

    /**
     * Handling of the first record as a header or as a record.
     *
     * @return {@code true} if the first line is a header and should be ignored, otherwise {@code false}.
     */
    OptBoolean skipHeader() default OptBoolean.DEFAULT;

    /**
     * If empty lines are possible, set this to {@code true} - default is {@code false}.
     *
     * @return {@code true}, if empty lines should be ignored.
     */
    OptBoolean ignoreEmptyLines() default OptBoolean.DEFAULT;

    /**
     * Set the (optional) comment marker (default is no comment marker)
     *
     * @return the default or a comment character
     */
    OptCharacter commentMarker() default @OptCharacter;

    /**
     * Trim the content of column's value.
     *
     * @return current {@link OptBoolean}
     */
    OptBoolean trim() default OptBoolean.DEFAULT;

    /**
     * Set a ignorable quote character (for example ' or ").
     * 
     * @return the quote character
     */
    OptCharacter quoteCharacter() default @OptCharacter;

    /**
     * Set the null code string, which should be convert to {@code null}.
     * 
     * @return the null code
     * 
     * @see #nullString() 
     */
    OptString nullCode() default @OptString;

    /**
     * The null string to be returned, in case of {@code null} - default is {@code NULL}.
     *
     * This must be used in conjunction with {@link #nullCode()}, otherwise there will be never null.
     *
     * @return the {@code null} replacement string
     */
    String nullString() default "NULL";


    /**
     * Set the escape character - default is none.
     * 
     * @return the escape character.
     */
    OptCharacter escapeCharacter() default @OptCharacter;
}
