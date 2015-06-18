/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2015 Marko Umek (http://fail-early.com/contact)
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
package org.failearly.dataset.datastore.support;

import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.simplefile.SimpleFileParser;
import org.failearly.dataset.simplefile.SimpleFileStatement;

/**
 * Base class for {@link org.failearly.dataset.simplefile.SimpleFileParser} based data store implementations with transactional behaviour.
 */
public abstract class SimpleFileTransactionalSupportDataStoreBase<T> extends TransactionalSupportDataStoreBase<T> {

    private final SimpleFileParser simpleFileParser=new SimpleFileParser();

    protected SimpleFileTransactionalSupportDataStoreBase() {
    }

    protected SimpleFileTransactionalSupportDataStoreBase(String dataStoreId, String dataStoreConfig) {
        super(dataStoreId, dataStoreConfig);
    }

    @Override
    protected final void applyEntireResource(T transaction, DataResource dataResource) throws Exception {
        simpleFileParser.parseAndHandleInputStream(dataResource,
                transaction,
                this::doExecuteStatement);
    }

    protected void doExecuteStatement(T transaction, SimpleFileStatement statement) throws Exception {
        throw new UnsupportedOperationException("Please implement doExecuteStatement()");
    }

}
