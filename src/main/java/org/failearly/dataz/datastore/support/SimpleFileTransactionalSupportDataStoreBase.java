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

package org.failearly.dataz.datastore.support;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.datastore.support.simplefile.SimpleFileParser;
import org.failearly.dataz.datastore.support.simplefile.SimpleFileStatement;

import java.lang.annotation.Annotation;

/**
 * Base class for {@link org.failearly.dataz.datastore.support.simplefile.SimpleFileParser} based data store implementations with transactional behaviour.
 */
public abstract class SimpleFileTransactionalSupportDataStoreBase<T> extends TransactionalSupportDataStoreBase<T> {

    private final SimpleFileParser simpleFileParser=new SimpleFileParser();

    protected SimpleFileTransactionalSupportDataStoreBase(Class<? extends NamedDataStore> namedDataStore, Annotation dataStoreAnnotation) {
        super(namedDataStore, dataStoreAnnotation);
    }

    @Override
    protected final void applyResourceOnTransaction(T transaction, DataResource dataResource) throws Exception {
        simpleFileParser.processStatements(
                dataResource,
                transaction,
                (statement, transaction1) -> doExecuteStatement(transaction1, statement)
        );
    }

    protected void doExecuteStatement(T transaction, SimpleFileStatement statement) throws Exception {
        throw new UnsupportedOperationException("Please implement doExecuteStatement()");
    }

}
