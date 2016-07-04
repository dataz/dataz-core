/*
 * Copyright (c) 2009.
 *
 * Date: 23.05.16
 * 
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
