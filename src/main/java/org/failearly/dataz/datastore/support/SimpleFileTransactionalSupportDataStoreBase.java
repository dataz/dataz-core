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

package org.failearly.dataz.datastore.support;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.simplefile.SimpleFileParser;
import org.failearly.dataz.simplefile.SimpleFileStatement;

import java.lang.annotation.Annotation;

/**
 * Base class for {@link org.failearly.dataz.simplefile.SimpleFileParser} based data store implementations with transactional behaviour.
 */
public abstract class SimpleFileTransactionalSupportDataStoreBase<T> extends TransactionalSupportDataStoreBase<T> {

    private final SimpleFileParser simpleFileParser=new SimpleFileParser();

    protected SimpleFileTransactionalSupportDataStoreBase(Class<? extends NamedDataStore> namedDataStore, Annotation dataStoreAnnotation) {
        super(namedDataStore, dataStoreAnnotation);
    }

    @Override
    protected final void applyResourceOnTransaction(T transaction, DataResource dataResource) throws Exception {
        simpleFileParser.parseAndHandleInputStream(dataResource,
                transaction,
                this::doExecuteStatement);
    }

    protected void doExecuteStatement(T transaction, SimpleFileStatement statement) throws Exception {
        throw new UnsupportedOperationException("Please implement doExecuteStatement()");
    }

}
