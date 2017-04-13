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
package org.failearly.dataz.datastore.support.transaction

import org.failearly.dataz.datastore.DataStoreException
import org.failearly.dataz.resource.DataResource
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.sql.SQLException

import static org.failearly.dataz.datastore.support.transaction.TransactionalSupportBuilder.Provider.USE_STATEMENT_PROVIDER
/**
 * PerStatementTransactionalSupportSpec contains specification for ... .
 */
@Subject([TransactionalSupportBuilder, TransactionalSupport])
class PerStatementTransactionalSupportSpec extends Specification {
    private DataResource dataResource
    private TestTransactionalContext transactionalContext
    private PerStatementProvider<TestTransactionalContext> provider
    private TransactionalSupport<TestTransactionalContext> transactionalSupport

    void setup() {
        dataResource = Stub(DataResource) {
            getResource() >> "AnyResourceName"
        }

        transactionalContext = new TestTransactionalContext()

        provider = Mock(PerStatementProvider)

        transactionalSupport = TransactionalSupportBuilder.createBuilder(TestTransactionalContext)
                .withPerStatement(provider)
                .withProvider(USE_STATEMENT_PROVIDER)
                .build()
    }

    def "How to process a data resource?"() {
        when: "process a data resource"
        transactionalSupport.process(dataResource)

        then: "all methods of the happy path should be called"
        with(provider) {
            1 * createTransactionContext() >> transactionalContext
            1 * process(transactionalContext, dataResource)
            1 * close(transactionalContext, ProcessingState.OK)
        }
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