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

package org.failearly.dataz.test;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.function.Function;

/**
 * ClosureMatcher is responsible for ...
 */
public class ClosureMatcher<T, U> extends FeatureMatcher<T, U> {
    private final Function<T, U> function;

    private ClosureMatcher(Function<T, U> function, Matcher<? super U> subMatcher, String featureDescription, String featureName) {
        super(subMatcher, featureDescription, featureName);
        this.function = function;
    }

    public static <T, U> Matcher<T> closureMatcher(Function<T, U> function, Matcher<? super U> subMatcher, String featureName) {
        return closureMatcher(function, subMatcher, featureName, featureName);
    }


    public static <T, U> Matcher<T> closureMatcher(Function<T, U> function, Matcher<? super U> subMatcher, String featureDescription, String featureName) {
        return new ClosureMatcher<T, U>(function, subMatcher, featureDescription, featureName);
    }

    @Override
    protected U featureValueOf(T actual) {
        return function.apply(actual);
    }
}
