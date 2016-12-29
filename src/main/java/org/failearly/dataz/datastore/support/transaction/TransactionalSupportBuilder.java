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

package org.failearly.dataz.datastore.support.transaction;

import org.failearly.common.builder.BuilderBase;

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