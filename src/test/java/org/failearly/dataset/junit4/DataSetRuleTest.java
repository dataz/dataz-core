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

package org.failearly.dataset.junit4;

import org.failearly.dataset.DataSet;
import org.failearly.dataset.SuppressDataSet;
import org.failearly.dataset.SuppressCleanup;
import org.failearly.dataset.datastore.DataStore;
import org.failearly.dataset.datastore.DataStoreException;
import org.failearly.dataset.datastore.DataStores;
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.test.DefaultFakeDataStore;
import org.failearly.dataset.util.ExceptionVerifier;
import org.failearly.dataset.test.FakeDataStoreRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * DataSetRuleTest contains tests for {@link DataSetRule} and annotation {@link DataSet}.
 */
public class DataSetRuleTest {

    private static final String DATA_STORE_ID = "datastore";

    private static final DataStore fakeDataStore = spy(new FakeDataStore(DATA_STORE_ID));

    @ClassRule
    public static final TestRule fakeDataStoreRule = FakeDataStoreRule.createFakeDataStoreRule(DataSetRuleTest.class).addDataStore(fakeDataStore);

    @Rule
    public final Junit4DescriptionHolder descriptionHolder = new Junit4DescriptionHolder();


    private DataSetRule dataSetRule;
    private Statement fakeStatement;

    @Before
    public void setup() {
        fakeStatement = createStatement();
        Mockito.reset(fakeDataStore);
        DataStores.addDataStore(this.getClass(), fakeDataStore);
        dataSetRule = DataSetRule.createDataSetRule(this.getClass());
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID)
    public void with_DataSet_annotation__should_return_a_new_junit_statement() throws Throwable {
        // act / when
        final Statement newStatement = applyDataSetRule();

        // assert / then
        assertThat("Got new statement?", newStatement, not(sameInstance(fakeStatement)));
        verify(fakeDataStore, never()).setup(any(TestMethod.class));
        verify(fakeDataStore, never()).cleanup(any(TestMethod.class));
    }

    @Test
    public void without_DataSet_annotation__should_return_the_origin_junit_statement() throws Throwable {
        // act / when
        final Statement newStatement = applyDataSetRule();

        // assert / then
        assertThat("Got new statement?", newStatement, sameInstance(fakeStatement));
        verify(fakeDataStore, never()).setup(any(TestMethod.class));
        verify(fakeDataStore, never()).cleanup(any(TestMethod.class));
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID)
    @SuppressDataSet
    public void without_DataSet_and_NoDataSet_annotation__should_return_the_origin_junit_statement() throws Throwable {
        // act / when
        final Statement originStatement = applyDataSetRule();

        // assert / then
        assertThat("Got origin statement?", originStatement, sameInstance(fakeStatement));
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID)
    @SuppressDataSet
    public void without_DataSet_and_NoDataSet_annotation__should_never_apply_setup_nor_cleanup() throws Throwable {
        // arrange / given
        final Statement originStatement = applyDataSetRule();

        // act / when
        originStatement.evaluate();

        // assert / then
        verify(fakeDataStore, never()).setup(any(TestMethod.class));
        verify(fakeDataStore, never()).cleanup(any(TestMethod.class));
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID, setup = "existing.setup", cleanup = "existing.cleanup")
    public void dataSet_junit_statement_evaluate__should_call_setup_and_cleanup_on_data_store() throws Throwable {
        // arrange / given
        final Statement newStatement = applyDataSetRule();

        // act / when
        newStatement.evaluate();

        // assert / then
        verify(fakeDataStore).setup(any(TestMethod.class));
        verify(fakeDataStore).cleanup(any(TestMethod.class));
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID, setup = "existing.setup", cleanup = "existing.cleanup")
    @SuppressCleanup
    public void annotation_SuppressCleanup__should_suppress_cleanup() throws Throwable {
        // arrange / given
        final Statement newStatement = applyDataSetRule();

        // act / when
        newStatement.evaluate();

        // assert / then
        verify(fakeDataStore).setup(any(TestMethod.class));
        verify(fakeDataStore, never()).cleanup(any(TestMethod.class));
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID, setup = "unknown.setup")
    public void missing_setup_resource__should_result_in_error() throws Throwable {
        // arrange / given
        final Statement newStatement = applyDataSetRule();

        // assert / then
        ExceptionVerifier.on(newStatement::evaluate).expect(DataStoreException.class).expect("Can't open resource '/org/failearly/dataset/junit4/unknown.setup'. Did you create the resource within classpath?").verify();
    }

    @Test
    @DataSet(datastore = DATA_STORE_ID, setup = "existing.setup", cleanup = "unknown.cleanup")
    public void missing_cleanup_resource_in_annotation_DataSet__should_be_ignored() throws Throwable {
        // arrange / given
        final Statement newStatement = applyDataSetRule();

        // act / when
        newStatement.evaluate();

        // assert / then
        verify(fakeDataStore).setup(any(TestMethod.class));
        verify(fakeDataStore).cleanup(any(TestMethod.class));
    }

    private static Statement createStatement() {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
    }

    private Statement applyDataSetRule() {
        return dataSetRule.apply(fakeStatement, descriptionHolder.lastDescription);
    }


    private static class FakeDataStore extends DefaultFakeDataStore {
        FakeDataStore(String dataStoreId) {
            super(dataStoreId);
        }

        @Override
        protected void doApplyResource(DataResource dataResource) throws DataStoreException {
            with.action("Open resource", () -> dataResource.open().close());
        }
    }

    private static class Junit4DescriptionHolder implements TestRule {
        Description lastDescription = null;

        @Override
        public Statement apply(Statement base, Description description) {
            lastDescription = description;
            return base;
        }
    }
}