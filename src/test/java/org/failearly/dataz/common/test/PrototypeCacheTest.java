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

package org.failearly.dataz.common.test;

import org.failearly.dataz.internal.common.classutils.PrototypeCache;
import org.failearly.dataz.common.test.annotations.TestsFor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PrototypeCacheTest contains tests for PrototypeCache
 */
@TestsFor(PrototypeCache.class)
public class PrototypeCacheTest {

    @Test
    public void prototype_cache__should_call_prototype_creator_once() throws Exception {
        // arrange / given
        final PrototypeCache.ObjectCreator creator=Mockito.mock(PrototypeCache.ObjectCreator.class);
        Mockito.when(creator.createPrototype(Prototype.class)).thenReturn(Prototype.INSTANCE);

        // act / when
        PrototypeCache.fetchPrototype(Prototype.class, creator);
        PrototypeCache.fetchPrototype(Prototype.class, creator);
        PrototypeCache.fetchPrototype(Prototype.class, creator);

        // assert / then
        Mockito.verify(creator).createPrototype(Prototype.class);

    }

    @Test
    public void prototype_cache__should_return_the_cached_prototype() throws Exception {
        // arrange / given
        final Prototype prototype=PrototypeCache.fetchPrototype(Prototype.class);

        // act / when
        final Prototype secondInstance=PrototypeCache.fetchPrototype(Prototype.class);

        // assert / then
        assertThat(prototype)
            .isNotNull()
            .isSameAs(secondInstance);
    }


    // A class which uses the prototype pattern.
    private static class Prototype {
        static final Prototype INSTANCE=new Prototype();

        private Prototype() {
        }
    }
}
