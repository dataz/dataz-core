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

package org.failearly.dataz.datastore.support.transaction;

import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.exception.DataSetException;
import org.failearly.dataz.internal.util.With;
import org.failearly.dataz.resource.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TransactionalSupport is responsible for ...
 */
public abstract class TransactionalSupport<T>  {
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionalSupport.class);

    private static final With ignore = With.ignore();
    private static final With handle = With.create((description, exception) -> {
        throw new DataStoreException(description, exception);
    }, "transactional-support");

    private TransactionalSupport() {
    }

    static <T> TransactionalSupport<T> create(final PerDataResourceProvider<T> perDataResourceProvider) {
        return new TransactionalSupport<T>() {
            @Override
            public void process(DataResource dataResource) throws DataStoreException {
                ProcessingState processingState=ProcessingState.ERROR;
                final T transactionalContext= createTransactionalContext(perDataResourceProvider);
                try {
                    handle.action("Process data resource " + dataResource.getResource(), ()->perDataResourceProvider.process(transactionalContext, dataResource));
                    handle.action("Commit transaction on data resource " + dataResource.getResource(),()->perDataResourceProvider.commit(transactionalContext));
                    processingState=ProcessingState.OK;
                }
                catch(DataSetException ex) {
                    ignore.action("Rollback transaction", ()->perDataResourceProvider.rollback(transactionalContext));
                    throw ex;
                }
                finally {
                    final ProcessingState fProcessingState=processingState;
                    ignore.action("Close connection", ()->perDataResourceProvider.close(transactionalContext, fProcessingState));
                }
            }

            private T createTransactionalContext(final PerDataResourceProvider<T> perDataResourceProvider) {
                final T transactionalContext=handle.producer("Create transactional context", perDataResourceProvider::createTransactionContext);
                if( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug("Created transactional context '{}'", transactionalContext);
                }

                return transactionalContext;
            }
        };
    }

    static <T>  TransactionalSupport<T> create(PerStatementProvider<T> perStatementProvider) {
        return new TransactionalSupport<T>() {
            @Override
            public void process(DataResource dataResource) throws DataStoreException {
                ProcessingState processingState=ProcessingState.ERROR;
                final T transactionalContext=createTransactionalContext(perStatementProvider);
                try {
                    handle.action("Process data resource " + dataResource.getResource(), ()->perStatementProvider.process(transactionalContext, dataResource));
                    processingState=ProcessingState.OK;
                }
                finally {
                    final ProcessingState fProcessingState=processingState;
                    ignore.action("Close connection", ()->perStatementProvider.close(transactionalContext, fProcessingState));
                }
            }

            private T createTransactionalContext(final PerStatementProvider<T> perStatementProvider) {
                final T transactionalContext=handle.producer("Create transactional context", perStatementProvider::createTransactionContext);
                if( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug("Created transactional context '{}'", transactionalContext);
                }

                return transactionalContext;
            }
        };
    }



    public abstract void process(DataResource dataResource) throws DataStoreException;
}
