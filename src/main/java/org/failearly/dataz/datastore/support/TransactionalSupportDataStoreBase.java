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

package org.failearly.dataz.datastore.support;


import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.internal.util.With;

import java.lang.annotation.Annotation;

/**
 * Base class for providing usual transactional behaviour for a DataStore.
 *
 * @see org.failearly.dataz.datastore.DataStore#hasTransactionalSupport()
 * @see org.failearly.dataz.DataSet#transactional()
 *
 * @param <T> the transaction type
 */
@SuppressWarnings("UnusedParameters")
public abstract class TransactionalSupportDataStoreBase<T> extends DataStoreBase {

    protected static final boolean USE_TRANSACTION = true;
    protected static final boolean NO_TRANSACTION = false;

    protected TransactionalSupportDataStoreBase(Class<? extends NamedDataStore> namedDataStore, Annotation dataStoreAnnotation) {
        super(namedDataStore, dataStoreAnnotation);
    }

    @Override
    public boolean hasTransactionalSupport() {
        return true;
    }

    /**
     * {@code true} if the DataStore implementation should use always a transaction. If {@code true} overrides {@link #hasTransactionalSupport()}.
     * Default is {@code false}.
     *
     * @return {@code true} if the DataStore implementation should use always a transaction. Otherwise {@code false}.
     */
    protected boolean forceTransaction() {
        return false;
    }

    protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        if( forceTransaction() ) {
            doApplyTransactionalResource(USE_TRANSACTION, dataResource);
        } else if (hasTransactionalSupport() && dataResource.isTransactional()) {
            doApplyTransactionalResource(USE_TRANSACTION, dataResource);
        } else {
            doApplyTransactionalResource(NO_TRANSACTION, dataResource);
        }
    }

    private void doApplyTransactionalResource(boolean useTransaction, DataResource dataResource) {
        final T transaction=with.producer("Start transaction", ()->startTransaction(dataResource, useTransaction));
        try {
            with.action("Apply resource " + dataResource.getResource(), ()-> applyResourceOnTransaction(transaction, dataResource));
            if( useTransaction ) {
                with.action("Commit transaction",()->commitTransaction(transaction));
            }
        }
        catch(RuntimeException ex) {
            if( transaction!=null && useTransaction ) {
                With.ignore().action("Rollback transaction", ()->rollbackTransaction(transaction));
            }
            throw ex;
        }
        finally {
            With.ignore().action("Close transaction", ()->closeTransaction(transaction));
        }
    }

    /**
     * Do from a transaction on dataResource. The returned object could be any object. If {@code useTransaction==false} neither
     * {@link #commitTransaction(Object)} nor {@link #rollbackTransaction(Object)} will be called. But {@link #closeTransaction(Object)} will always called
     * on the returned object ({@code null}).
     * <br><br>
     * Caution: {@code startTransaction} will always called, either transactional or not.
     *
     * @param dataResource the {@link org.failearly.dataz.DataSet} resource (setup or cleanup).
     * @param useTransaction  use transaction.
     *
     * @return the transaction/context object or {@code null}.
     *
     * @throws java.lang.Exception any exception while starting transaction
     *
     * @see #commitTransaction(Object)
     * @see #rollbackTransaction(Object)
     * @see #closeTransaction(Object)
     */
    protected T startTransaction(DataResource dataResource, boolean useTransaction) throws Exception {
        return null;
    }

    /**
     * The implementation should apply the {@link DataResource} within the transaction (or) if not used,
     * ignore the transaction object.
     *
     * @param transaction the transaction/context object (could be {@code null}).
     * @param dataResource the data resource.
     *
     * @throws Exception any exception while processing the {@code dataSetResource}.
     *
     * @see #startTransaction(DataResource, boolean)
     */
    protected abstract void applyResourceOnTransaction(T transaction, DataResource dataResource) throws Exception;

    /**
     * Do commit the previous created transaction. Only called if there is an open transaction.
     *
     * @param transaction the transaction/context object (could be {@code null}).
     *
     * @throws Exception any Exception while commit the transaction.
     */
    protected void commitTransaction(T transaction) throws Exception {
        /* default implementation does nothing */
    }

    /**
     * Do rollback the previous created transaction. Only called if there is an open transaction.
     *
     * @param transaction the transaction/context object (could be {@code null}).
     *
     * @throws Exception any Exception while rollback the transaction.
     */
    @SuppressWarnings({"WeakerAccess", "EmptyMethod"})
    protected void rollbackTransaction(T transaction) throws Exception {
        /* default implementation does nothing */
    }

    /**
     * Called to close the transaction or context object. This method will be always called, except there was an exception while starting the transaction.
     *
     * @param transaction the transaction/context object (could be {@code null}).
     *
     * @throws Exception any Exception while closing the transaction.
     */
    @SuppressWarnings({"WeakerAccess", "EmptyMethod"})
    protected void closeTransaction(T transaction) throws Exception {
        /* default implementation does nothing */
    }

}
