/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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
package org.failearly.dataz.internal.datastore;

import org.failearly.common.annotation.traverser.MetaAnnotationHandlerBase;
import org.failearly.common.annotation.traverser.MetaAnnotationTraverser;
import org.failearly.common.annotation.traverser.TraverseDepth;
import org.failearly.common.annotation.traverser.TraverseStrategy;
import org.failearly.common.classutils.ObjectCreatorUtil;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.*;
import org.failearly.dataz.internal.datastore.state.DataStoreState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static org.failearly.common.annotation.traverser.AnnotationTraverserBuilder.metaAnnotationTraverser;

/**
 * DataStoresImplementation contains the actually implementation of {@link DataStores}.
 */
public final class DataStoresImplementation implements DataStores.Instance, DataStoreState.OnRelease {

    private final ThreadLocal<Optional<MutableDataStores>> currentDataStore = new ThreadLocal<Optional<MutableDataStores>>() {
        @Override
        protected Optional<MutableDataStores> initialValue() {
            return Optional.empty();
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStoresImplementation.class);

    private static final MetaAnnotationTraverser<DataStoreFactory.Definition> DATASTORE_ANNOTATION_TRAVERSER = metaAnnotationTraverser(DataStoreFactory.Definition.class)
            .withTraverseDepth(TraverseDepth.DECLARED_CLASS)
            .withTraverseStrategy(TraverseStrategy.TOP_DOWN)
            .build();

    private final ConcurrentMap<Class<? extends NamedDataStore>, DataStoresHolder> dataStores = new ConcurrentHashMap<>();

    private static final DataStoresShutdown shutdown=new DataStoresShutdown();

    private DataStoresImplementation() {
        shutdown.setImplementation(this);
    }

    /**
     * The factory method for {@link DataStores.Instance}.
     * @return a new datastore instance.
     */
    public static DataStores.Instance createDataStoresInstance() {
        return new DataStoresImplementation();
    }

    @Override
    public MutableDataStores reserve(Set<Class<? extends NamedDataStore>> namedDataStores) {
        final MutableDataStoresImpl mutableDataStores = new MutableDataStoresImpl(reserveDataStores(namedDataStores));
        currentDataStore.set(Optional.of(mutableDataStores));
        return mutableDataStores;
    }

    @Override
    public ImmutableDataStores access() {
        return currentDataStore.get()
                    .map(MutableDataStores::access)
                    .orElseThrow(()-> new IllegalStateException("access(): You could only access already reserved datastores."));
    }

    @Override
    public void dispose() {
        disposeAllDataStores();
    }

    private void disposeAllDataStores() {
        this.dataStores.values().forEach(DataStoresHolder::dispose);
        this.dataStores.clear();
    }

    private List<DataStoreState> reserveDataStores(Set<Class<? extends NamedDataStore>> namedDataStores) {
        createDataStoreInstances(namedDataStores);
        return doReserveDataStores(namedDataStores);
    }

    private void createDataStoreInstances(Set<Class<? extends NamedDataStore>> namedDataStores) {
        for (Class<? extends NamedDataStore> namedDataStore : namedDataStores) {
            dataStores.computeIfAbsent(namedDataStore, this::doCreateDataStores);
        }
    }

    private List<DataStoreState> doReserveDataStores(Set<Class<? extends NamedDataStore>> namedDataStores) {
        return namedDataStores.stream()
                .map(dataStores::get)
                .map(DataStoresHolder::reserve)
                .map(dss ->dss.register(this))
                .collect(Collectors.toList());
    }

    @Override
    public void onRelease(DataStoreState idle) {
        currentDataStore.set(Optional.empty());
    }


    private DataStoresHolder doCreateDataStores(Class<? extends NamedDataStore> namedDataStore) {
        final DataStoreCreator dataStoreCreator = new DataStoreCreator(namedDataStore);
        DATASTORE_ANNOTATION_TRAVERSER.traverse(namedDataStore, dataStoreCreator);
        return DataStoresHolder.createDataStoresHolder(namedDataStore, dataStoreCreator.getDataStores());
    }

    void shutdown() {
        LOGGER.info("Shutdown: dispose and reset {} data stores.", this.dataStores.size());
        disposeAllDataStores();
    }

    private static class DataStoreCreator extends MetaAnnotationHandlerBase<DataStoreFactory.Definition> {
        private final List<DataStore> collectedDataStores = new LinkedList<>();
        private Class<? extends NamedDataStore> namedDataStore;

        private DataStoreCreator(Class<? extends NamedDataStore> namedDataStore) {
            this.namedDataStore = namedDataStore;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleMetaAnnotation(Annotation annotation, DataStoreFactory.Definition dataStoreDefinition) {
            final Class<? extends DataStoreFactory> dataStoreFactoryClass = dataStoreDefinition.factory();
            final DataStore dataStore = ObjectCreatorUtil.createInstance(dataStoreFactoryClass).createDataStore(this.namedDataStore, annotation, dataStoreDefinition);
            collectedDataStores.add(dataStore);
        }

        List<DataStore> getDataStores() {
            if (collectedDataStores.isEmpty()) {
                throw new DataStoreInitializationException("Missing a DataStore annotation on " + this.namedDataStore.getSimpleName() + "!");
            }
            return collectedDataStores;
        }
    }
}
