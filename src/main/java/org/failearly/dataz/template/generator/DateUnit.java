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

package org.failearly.dataz.template.generator;

/**
 * DateUnit defines the date/time units. Be carefully with MONTHS and YEARS, these are not correct.
 */
public enum DateUnit {
    MILLISECONDS,
    SECONDS(1000, MILLISECONDS),
    MINUTES(60,SECONDS),
    HOURS(60,MINUTES),
    DAYS(24,HOURS),
    WEEKS(7,DAYS),
    MONTHS(30,DAYS),
    YEARS(365, DAYS);

    private final long milliseconds;

    DateUnit() {
        this.milliseconds = 1;
    }

    DateUnit(long factor, DateUnit previous) {
        this.milliseconds = factor * previous.milliseconds;
    }

    public long next(int numberOf) {
        return numberOf * milliseconds;
    }
}
