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

package org.failearly.dataz.datastore.support.transaction;

import org.failearly.dataz.internal.common.builder.BuilderBase;

/**
 * Provides support for processing a data resource either in a single transaction (batch mode) or
 * per statement mode.
 *
 * **Remark**: This builder could be reused, but is not thread safe. So use it per data resource instance, not per class.
 *
 * @param <T> the transactional context
 */
public final class TransactionalSupportBuilder<T> extends BuilderBase<TransactionalSupport<T>> {
    private PerDataResourceProvider<T> perDataResourceProvider;
    private PerStatementProvider<T> perStatementProvider;
    private Provider provider;


    /**
     * Create a TransactionalSupport builder.
     *
     * @param transactionalContextType the transactional context class.
     * @param <T> the transactional context class
     * @return an empty instance.
     */
    @SuppressWarnings("UnusedParameters")
    public static <T> TransactionalSupportBuilder<T> createBuilder(Class<T> transactionalContextType) {
        return new TransactionalSupportBuilder<>();
    }


    /**
     * Set the data resource provider.
     * @param perDataResourceProvider the per data resource provider.
     * @return self
     */
    public TransactionalSupportBuilder<T> withPerDataResource(PerDataResourceProvider<T> perDataResourceProvider) {
        this.perDataResourceProvider = perDataResourceProvider;
        return this;
    }

    /**
     * Set the per statement provider.
     * @param perStatementProvider the per statement provider.
     * @return self
     */
    public TransactionalSupportBuilder<T> withPerStatement(PerStatementProvider<T> perStatementProvider) {
        this.perStatementProvider = perStatementProvider;
        return this;
    }

    public enum Provider {
        USE_DATA_RESOURCE_PROVIDER,
        USE_STATEMENT_PROVIDER
    }

    /**
     * Select the provider to be used.
     *
     * @param provider the provider type
     * @return self
     */
    public TransactionalSupportBuilder<T> withProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    @Override
    protected void checkMandatoryFields() {
        super.checkMandatoryField(provider, "provider");

        if(provider==Provider.USE_DATA_RESOURCE_PROVIDER)
            super.checkMandatoryField(perDataResourceProvider, "perDataResourceProvider");
        else
            super.checkMandatoryField( perStatementProvider, "perStatementProvider");
    }

    @Override
    protected TransactionalSupport<T> doBuild() {
        if(provider==Provider.USE_DATA_RESOURCE_PROVIDER) {
            return TransactionalSupport.create(perDataResourceProvider);
        }
        return TransactionalSupport.create(perStatementProvider);
    }
}