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
package org.failearly.dataz.internal.datastore.state;

import org.failearly.dataz.internal.datastore.state.DataStoreState.OnRelease;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * OnReleaseList is responsible for ...
 */
final class OnReleaseList implements OnRelease {
    private final List<OnRelease> onReleases=new LinkedList<>();

    void add(OnRelease onRelease) {
        Objects.requireNonNull(onRelease, "onRelease callback instance must not be null");
        onReleases.add(0, onRelease);
    }
    @Override
    public void onRelease(DataStoreState idle) {
        onReleases.forEach((or)->or.onRelease(idle));
    }
}
