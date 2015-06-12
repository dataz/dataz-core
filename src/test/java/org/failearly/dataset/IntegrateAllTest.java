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

package org.failearly.dataset;

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.config.DataSetProperties;
import org.failearly.dataset.datastore.*;
import org.failearly.dataset.datastore.support.SimpleFileTransactionalSupportDataStoreBase;
import org.failearly.dataset.template.generator.ConstantGenerator;
import org.failearly.dataset.template.generator.ListGenerator;
import org.failearly.dataset.template.generator.RandomRangeGenerator;
import org.failearly.dataset.internal.model.TestMethod;
import org.failearly.dataset.junit4.DataSetDriver;
import org.failearly.dataset.resource.DataResource;
import org.failearly.dataset.simplefile.SimpleFileStatement;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

/**
 * IntegrateAllTest tests the collobaration of the entire DataSet core module.
 */
@SuppressWarnings("SpellCheckingInspection")
// The DataStoreDefinition should be ussually in a base class.
@DataStoreDefinition(id = "h2", type = IntegrateAllTest.TestDataStoreType.class)
@DataStoreDefinition(id = "oracle", type = IntegrateAllTest.TestDataStoreType.class)
@DataStoreDefinition(id = "db2", type = IntegrateAllTest.TestDataStoreType.class)
@DataStoreDefinition(id = "db2" /* ignored */)

// The default data set
@DataSet(datastore = "db2", name = "DS1") // This associates DS1 to db2
@ConstantGenerator(dataset = "DS1", name = "dbtype", constant = "DB2")
@ListGenerator(dataset = "DS1", name = "firstnames", values = {"Marko", "Jana", "Emma", "Femke"})
@ListGenerator(dataset = "DS1", name = "lastnames", values = {"U.", "S."})
@RandomRangeGenerator(dataset = "DS1", name = "ids", start = 1, end = 1000, seed = 1)

@FixMethodOrder(MethodSorters.JVM)
public class IntegrateAllTest {

    @Rule
    public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);

    @BeforeClass
    public static void setDefaultSuffix() throws Exception {
        DataSetProperties.setProperty(Constants.DATASET_PROPERTY_DEFAULT_SETUP_SUFFIX, "setup.vm");
        DataSetProperties.setProperty(Constants.DATASET_TEMPLATE_OBJECT_DUPLICATE_STRATEGY, "IGNORE");
    }

    @AfterClass
    public static void resetDataSetProperties() throws Exception {
        DataSetProperties.reload();
    }

    @After
    public void dataStoresReset() throws Exception {
        DataSetDriver.reset();
        DataStores.reset();
    }

    @Test
    public void multipleInstancesOfDataStores() throws Exception {
        // act / when
        final TestDataStore h2 = (TestDataStore) DataStores.getDataStore("h2", this);
        final TestDataStore oracle = (TestDataStore) DataStores.getDataStore("oracle", this);
        final TestDataStore db2 = (TestDataStore) DataStores.getDataStore("db2", this);


        // assert / then
        assertThat("H2!=Oracle?", h2, not(sameInstance(oracle)));
        assertThat("H2!=DB2?", h2, not(sameInstance(db2)));
        assertThat("Oracle!=DB2?", oracle, not(sameInstance(db2)));
    }

    @Test
    @SuppressDataSet
    public void testNoDataSet() throws Exception {
        verifySetupCalls(0, "db2", "h2", "oracle");
        verifyCleanupCalls(0, "db2", "h2", "oracle");
    }

    @Test
    @DataSet(datastore = "oracle", name = "DS1", setup = "IntegrateAllTest.setup.vm")
    @ConstantGenerator(dataset = "DS1", name = "dbtype", constant = "ORACLE")
    @ListGenerator(dataset = "DS1", name = "firstnames", values = {"Kai", "Tanja", "Lorenz"})
    @ListGenerator(dataset = "DS1", name = "lastnames", values = {"S.", "U."})
    public void testOnOracle() throws Exception {
        assertAppliedStatements("oracle",
                "INSERT INTO DBTYPES (DBTYPE) VALUES ('ORACLE')"
                , "INSERT INTO USERS (ID,NAME) VALUES (986,'Kai S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (589,'Kai U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (848,'Tanja S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (314,'Tanja U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (255,'Lorenz S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (905,'Lorenz U.')"
        );
        assertSameAppliedStatements("db2", "oracle");
        assertAppliedStatements("h2");

        verifySetupCalls(1, "db2", "h2", "oracle");
        verifyCleanupCalls(0, "db2", "h2", "oracle");
    }

    @Test
    @DataSet(datastore = "h2", name = "DS1", setup = "IntegrateAllTest.setup.vm")
    @ConstantGenerator(dataset = "DS1", name = "dbtype", constant = "H2")
    public void testOnH2() throws Exception {
        assertAppliedStatements("h2",
                "INSERT INTO DBTYPES (DBTYPE) VALUES ('H2')"
                , "INSERT INTO USERS (ID,NAME) VALUES (986,'Marko U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (589,'Marko S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (848,'Jana U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (314,'Jana S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (255,'Emma U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (905,'Emma S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (435,'Femke U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (607,'Femke S.')"
        );
        assertSameAppliedStatements("db2", "h2");
        assertAppliedStatements("oracle");

        verifySetupCalls(1, "db2", "h2", "oracle");
        verifyCleanupCalls(0, "db2", "h2", "oracle");
    }

    @Test
    public void testClassDataSets() throws Exception {
        assertAppliedStatements("db2",
                "INSERT INTO DBTYPES (DBTYPE) VALUES ('DB2')"
                , "INSERT INTO USERS (ID,NAME) VALUES (986,'Marko U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (589,'Marko S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (848,'Jana U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (314,'Jana S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (255,'Emma U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (905,'Emma S.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (435,'Femke U.')"
                , "INSERT INTO USERS (ID,NAME) VALUES (607,'Femke S.')"
        );
        assertAppliedStatements("h2");
        assertAppliedStatements("oracle");
        verifySetupCalls(1, "db2", "h2", "oracle");
        verifyCleanupCalls(0, "db2", "h2", "oracle");
    }

    private static void assertSameAppliedStatements(String dataStoreId, String expectedDataStoreId) {
        final TestDataStore expectedDataStore = getDataStore(expectedDataStoreId);
        assertAppliedStatements(dataStoreId, expectedDataStore.statements.toArray(new String[expectedDataStore.statements.size()]));
    }

    private static TestDataStore getDataStore(String dataStoreId) {
        return (TestDataStore) DataStores.getDataStore(IntegrateAllTest.class, dataStoreId);
    }

    private static void assertAppliedStatements(String dataStoreId, String... expectedStatements) {
        final TestDataStore dataStore = getDataStore(dataStoreId);

        if (expectedStatements.length > 0) {
            assertThat("Statements on " + dataStore.getId() + "?", dataStore.statements, contains(expectedStatements));
        } else {
            assertThat("No statements on " + dataStore.getId() + "?", dataStore.statements, hasSize(0));
        }
    }

    private static void verifySetupCalls(int numCalls, String... dataStoreIds) {
        verifyDataStoreExecution((d) -> d.setup(any(TestMethod.class)), numCalls, dataStoreIds);
    }

    private static void verifyCleanupCalls(int numCalls, String... dataStoreIds) {
        verifyDataStoreExecution((d) -> d.cleanup(any(TestMethod.class)), numCalls, dataStoreIds);
    }

    private static void verifyDataStoreExecution(Consumer<DataStore> doExecuteMethod, int times, String... dataStoreIds) {
        for (String dataStoreId : dataStoreIds) {
            doExecuteMethod.accept(Mockito.verify(DataStores.getDataStore(IntegrateAllTest.class, dataStoreId), times(times)));
        }
    }


    public static class TestDataStoreType implements DataStoreType {
        @Override
        public DataStore createDataStore(DataStoreDefinition annotation, Object context) {
            return Mockito.spy(new TestDataStore(annotation.id(), annotation.config()));
        }
    }

    private static class TestDataStore extends SimpleFileTransactionalSupportDataStoreBase<List<String>> {
        List<String> statements = Collections.emptyList();

        TestDataStore(String dataStoreId, String dataStoreConfig) {
            super(dataStoreId, dataStoreConfig);
        }

        @Override
        protected List<String> startTransaction(DataResource dataResource, boolean useTransaction) {
            return new LinkedList<>();
        }

        @Override
        protected void doExecuteStatement(List<String> transaction, SimpleFileStatement statement) {
            transaction.add(statement.getContent());
        }

        @Override
        protected void commitTransaction(List<String> transaction) {
            this.statements = transaction;
        }

        @Override
        public void initialize() throws DataStoreException {
        }
    }
}
