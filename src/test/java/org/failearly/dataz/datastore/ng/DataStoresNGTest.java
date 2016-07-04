package org.failearly.dataz.datastore.ng;

import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.common.test.ExceptionVerifier;
import org.failearly.common.test.annotations.Subject;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataStoresNGTest contains tests for ... .
 */
@Subject(DataStoresNG.class)
public class DataStoresNGTest {

    @Before
    public void setUp() throws Exception {
        DataStoresNG.dispose();
        TestDataStore.reset();
    }

    @Test
    public void what_happens_if_a_datastore_is_reserved() throws Exception {
        // arrange / given

        // act / when
        final MutableDataStores dataStores = DataStoresNG.reserve(AnyNamedDataStore.class);
        final DataStore dataStore = dataStores.getOriginDataStore(AnyNamedDataStore.class);
        dataStores.release();


        // assert / then
        assertThat(dataStore, is(instanceOf(TestDataStore.class)));
        assertThat(TestDataStore.initializedCalled(), is(1));
        assertThat(TestDataStore.disposedCalled(), is(0));
    }

    @Test
    public void what_happens_if_a_datastore_is_reserved_more_then_once() throws Exception {
        // arrange / given
        final MutableDataStores dataStores1 = DataStoresNG.reserve(AnyNamedDataStore.class);
        dataStores1.release();

        // act / when
        final MutableDataStores dataStores2 = DataStoresNG.reserve(AnyNamedDataStore.class);
        dataStores2.release();

        // assert / then
        assertThat("Still once initialized?", TestDataStore.initializedCalled(), is(1));
    }

    @Test
    public void what_happens_if_more_then_one_datastore_is_reserved() throws Exception {
        // arrange / given
        // act / when
        DataStoresNG.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);

       // assert / then
        assertThat("More then once initialized?", TestDataStore.initializedCalled(), is(2));
    }

    @Test
    public void what_happens_if_released_datastores_are_disposed() throws Exception {
        // arrange / given
        final MutableDataStores dataStores = DataStoresNG.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);
        dataStores.release();

        // act / when
        DataStoresNG.dispose();

        // assert / then
        assertThat("Disposed?",TestDataStore.disposedCalled(), is(2));
    }

    @Test
    public void what_happens_while_disposing_one_is_not_released() throws Exception {
        // arrange / given
        DataStoresNG.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class).release();
        DataStoresNG.reserve(OtherNamedDataStore.class); // NOT RELEASED

        // act / when
        DataStoresNG.dispose();

        // assert / then
        assertThat("Not all are disposed?",TestDataStore.disposedCalled(), is(1));
    }

    @Test
    public void what_happens_while_disposing_all_datastores_a_last_one_will_released_after_dispose() throws Exception {
        // arrange / given
        DataStoresNG.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class).release();
        MutableDataStores notYetReleased =DataStoresNG.reserve(AnyNamedDataStore.class);

        // act / when
        DataStoresNG.dispose();
        notYetReleased.release();

        // assert / then
        assertThat("Even the last will be disposed?",TestDataStore.disposedCalled(), is(2));
    }

    @Test
    public void what_happens_if_disposing_twice() throws Exception {
        // arrange / given
        MutableDataStores dataStores = DataStoresNG.reserve(AnyNamedDataStore.class, OtherNamedDataStore.class);
        dataStores.release();
        DataStoresNG.dispose();
        assertThat("Only the first will be executed?", TestDataStore.disposedCalled(), is(2));
        TestDataStore.reset();

        // act / when
        DataStoresNG.dispose();

        // assert / then
        assertThat("The second dispose() has nothing to do?", TestDataStore.disposedCalled(), is(0));
    }

    @Test
    public void what_happens_a_named_datastore_has_multiple_DataStore_annotations_and_not_all_has_been_used() throws Exception {
        // arrange / given
        MutableDataStores first = DataStoresNG.reserve(MultipleDataStoreAnnotations.class);
        MutableDataStores second = DataStoresNG.reserve(MultipleDataStoreAnnotations.class);
        first.release();
        second.release();

        // act / when
        DataStoresNG.dispose();

        // assert / then
        assertThat("Created DataStore instances?", TestDataStore.created(), is(3));
        assertThat("Initialized called for each reserved instance?", TestDataStore.initializedCalled(), is(2));
        assertThat("Only the reserved will be disposed?", TestDataStore.disposedCalled(), is(2));
    }

    @Test
    public void how_many_data_store_will_be_applied_per_named_data_store() throws Exception {
        // arrange / given
        final MutableDataStores dataStores = DataStoresNG.reserve(MultipleDataStoreAnnotations.class);

        // act / when
        dataStores.apply((dataStore -> dataStore.applyDataResource(null)));

        // assert / then
        assertThat("How many applications of doApplyResource()?", TestDataStore.applyCalled(), is(1));
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_while_a_mutable_is_already_reserved() throws Exception {
        // arrange / given
        final DataResource DONT_CARE=null;
        DataStoresNG.reserve(AnyNamedDataStore.class);

        // act / when
        final ImmutableDataStores immutableDataStores = DataStoresNG.access();

        // assert / then
        ExceptionVerifier.on(()-> immutableDataStores.access(dataStore -> dataStore.applyDataResource(DONT_CARE)))
                .expect(IllegalStateException.class)
                .expect("Illegal State: DO NOT USE method 'applyDataResource' in state 'Accessed'.")
                .verify();
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_which_on_reserved_datastore() throws Exception {
        // arrange / given
        DataStoresNG.reserve(AnyNamedDataStore.class);

        // act / when
        final ImmutableDataStores immutableDataStores = DataStoresNG.access();

        // assert / then
        assertThat("Access to reserved datastores?",
                immutableDataStores.getDataStore(AnyNamedDataStore.class).getNamedDataStore(),
                is(AnyNamedDataStore.class)
        );
    }

    @Test
    public void what_happens_if_you_try_to_access_and_there_was_no_previous_reserve() throws Exception {
        // assert / then
        ExceptionVerifier.on(DataStoresNG::access)
                .expect(IllegalStateException.class)
                .expect("access(): You could only access already reserved datastores.")
                .verify();
    }

    @Test
    public void what_happens_if_you_try_to_create_an_immutable_which_on_an_already_release_datastore() throws Exception {
        // arrange / given
        DataStoresNG.reserve(AnyNamedDataStore.class).release();

        // act / when
        // assert / then
        ExceptionVerifier.on(DataStoresNG::access)
                .expect(IllegalStateException.class)
                .expect("access(): You could only access already reserved datastores.")
                .verify();
    }



    @AdhocDataStore(name ="any", implementation = TestDataStore.class)
    private static class AnyNamedDataStore extends NamedDataStore {}

    @AdhocDataStore(name ="other", implementation = TestDataStore.class)
    private static class OtherNamedDataStore extends NamedDataStore {}

    @AdhocDataStore(name ="1", implementation = TestDataStore.class)
    @AdhocDataStore(name ="2", implementation = TestDataStore.class)
    @AdhocDataStore(name ="3", implementation = TestDataStore.class)
    private static class MultipleDataStoreAnnotations extends NamedDataStore {}

    public static class TestDataStore extends DataStoreBase {
        private final static AtomicInteger created=new AtomicInteger(0);
        private final static AtomicInteger initializedCalled=new AtomicInteger(0);
        private final static AtomicInteger disposedCalled=new AtomicInteger(0);

        private final static AtomicInteger applyCalled=new AtomicInteger(0);

        @SuppressWarnings("unused")
        static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            return new TestDataStore(namedDataStore, annotation);
        }

        private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
            super(namedDataStore, annotation);
            created.incrementAndGet();
        }

        @Override
        public void initialize() throws DataStoreException {
            initializedCalled.incrementAndGet();
            super.initialize();
        }

        @Override
        protected void doEstablishConnection(PropertiesAccessor properties) throws Exception {
            // nothing to do
        }

        @Override
        public void dispose() {
            disposedCalled.incrementAndGet();
        }

        static void reset() {
            initializedCalled.set(0);
            disposedCalled.set(0);
            created.set(0);
            applyCalled.set(0);
        }

        @Override
        protected void doApplyResource(DataResource dataResource) {
            applyCalled.incrementAndGet();
        }

        static int created() {
            return created.get();
        }
        static int disposedCalled() {
            return disposedCalled.get();
        }

        static int initializedCalled() {
            return initializedCalled.get();
        }

        static int applyCalled() {
            return applyCalled.get();
        }
    }
}