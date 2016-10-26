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


package org.failearly.dataz.datastore.support.transaction

import org.failearly.dataz.datastore.DataStoreException
import org.failearly.dataz.resource.DataResource
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.sql.SQLException

import static org.failearly.dataz.datastore.support.transaction.TransactionalSupportBuilder.Provider.*

@Subject([TransactionalSupport, TransactionalSupportBuilder])
class PerDataResourceTransactionalSupportSpec extends Specification {
    private DataResource dataResource
    private TestTransactionalContext transactionalContext
    private PerDataResourceProvider<TestTransactionalContext> provider
    private TransactionalSupport<TestTransactionalContext> transactionalSupport

    void setup() {
        dataResource = Stub(DataResource) {
            getResource() >> "AnyResourceName"
        }

        transactionalContext = new TestTransactionalContext()

        provider = Mock(PerDataResourceProvider)

        transactionalSupport = TransactionalSupportBuilder.createBuilder(TestTransactionalContext)
                .withPerDataResource(provider)
                .withProvider(USE_DATA_RESOURCE_PROVIDER)
                .build()
    }


    def "How to process a data resource?"() {
        when: "process a data resource"
        transactionalSupport.process(dataResource)

        then: "all methods of the happy path should be called"
        with(provider) {
            1 * createTransactionContext() >> transactionalContext
            1 * process(transactionalContext, dataResource)
            1 * commit(transactionalContext)
            1 * close(transactionalContext, ProcessingState.OK)
        }

        and: "rollback() must not be executed, which does not belongs to the happy path!"
        0 * provider.rollback(_)
    }

    @Unroll
    def "How to handle process errors? - #exception"() {
        given: "provider throws an exception"
        with(provider) {
            createTransactionContext() >> transactionalContext
            process(_, _) >> { throw exception }
        }

        when: "process data resource (throwing the #exception)"
        transactionalSupport.process(dataResource)

        then: "a DataStoreException should be thrown and caused by #exception"
        def ex = thrown(DataStoreException)
        ex.message == "Process data resource AnyResourceName"
        ex.cause == exception

        and: "rollback() should have been called"
        1 * provider.rollback(transactionalContext)

        and: "commit() must not be executed"
        0 * provider.commit(_)

        and: "close() should be called anyway, but with ERROR state"
        1 * provider.close(transactionalContext, ProcessingState.ERROR)

        where:
        exception                                     || makeSpockHappy
        new Exception("any exception")                || _
        new RuntimeException("any runtime exception") || _
        new IOException("any IO exception")           || _
        new SQLException("any SQL exception")         || _
    }

    @Unroll
    def "What happens, if commit() throws an exception? - #exception"() {
        given: "provider throws an exception"
        with(provider) {
            createTransactionContext() >> transactionalContext
            commit(_) >> { throw exception }
        }

        when: "process data resource (throwing the #exception)"
        transactionalSupport.process(dataResource)

        then: "a DataStoreException should be thrown and caused by #exception"
        def ex = thrown(DataStoreException)
        ex.message == "Commit transaction on data resource AnyResourceName"
        ex.cause == exception

        and: "rollback() should have been called"
        1 * provider.rollback(transactionalContext)

        and: "close() should be called anyway, but with ERROR state"
        1 * provider.close(transactionalContext, ProcessingState.ERROR)

        where:
        exception                                     || makeSpockHappy
        new Exception("any exception")                || _
        new RuntimeException("any runtime exception") || _
        new IOException("any IO exception")           || _
        new SQLException("any SQL exception")         || _
    }

    @Unroll
    def "What happens, if rollback() throws an exception? - #processException"() {
        given: "a provider mock"
        with(provider) {
            createTransactionContext() >> transactionalContext
            process(_, _) >> { throw processException }
            rollback(_) >> { throw new IllegalArgumentException("any exception while rollback") }
        }

        when: "process the data resource (which causes an exception)"
        transactionalSupport.process(dataResource)

        then: "rollback's exception will be ignored"
        def ex = thrown(DataStoreException)
        ex.cause == processException

        and: "rollback must be called"
        1 * provider.rollback(transactionalContext)

        and: "commit() still must not be executed"
        0 * provider.commit(_)

        and: "close() should be called anyway but with ERROR state"
        1 * provider.close(transactionalContext, ProcessingState.ERROR)

        where:
        processException                              || makeSpockHappy
        new Exception("any exception")                || _
        new RuntimeException("any runtime exception") || _
        new IOException("any IO exception")           || _
        new SQLException("any SQL exception")         || _
    }

    @Unroll
    def "What happens, if createTransactionException() throws an exception? - #exception"() {
        given: "createTransactionException() throwing a exception"
        provider.createTransactionContext() >> { throw exception }

        when: "process the data resource (which causes an exception)"
        transactionalSupport.process(dataResource)

        then: "a DataStoreException should be thrown and caused by #exception"
        def ex = thrown(DataStoreException)
        ex.message == "Create transactional context"
        ex.cause == exception

        where:
        exception                                     || makeSpockHappy
        new Exception("any exception")                || _
        new RuntimeException("any runtime exception") || _
        new IOException("any IO exception")           || _
        new SQLException("any SQL exception")         || _
    }

    @Unroll
    def "What happens, if close() throws an exception? - #exception"() {
        given: "close() throwing a exception"
        with(provider) {
            createTransactionContext() >> transactionalContext
            close(_, _) >> { throw exception }
        }

        when: "process the data resource (which causes an exception)"
        transactionalSupport.process(dataResource)

        then: "the exception must be ignored"
        noExceptionThrown()

        where:
        exception                                     || makeSpockHappy
        new Exception("any exception")                || _
        new RuntimeException("any runtime exception") || _
        new IOException("any IO exception")           || _
        new SQLException("any SQL exception")         || _
    }
}