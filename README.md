dataSet
=======

_dataSet_ provides testing support for Datastores.

A typical datastore is a database, either SQL or NoSql. But a datastore could be more then a database. It could be any (remote) synchronous system.

>A __Datastore__ is any remote synchronous system, which responses to a request.

If you have to test a module which is using a datastore(s), you have are facing some more or less typical issues:

* How should I provide test data (test fixture), so that the tests are repeatable and independent.
* A complex query to a database is worth it's own unit test (even this kind of test some people won't call this an unit test at all).
* The tests should be fast and so you decide to use a different database then the productive one (for example [h2](http://http://www.h2database.com/)). But ...
* ... the tests should also be executed on the productive database.
* You have more then one datastore. For example: A single master database and for each tenant a different database instance.
* You have different types of datastores. For example: MySql and memcache.
* You are making a product which is based on JPA, but every test should run also on any supported database.
* Performance tests with the productive database.
* A custom remote system.
* ...

_dataSet_ is an attempt to provide solutions for these issues.


Why not DbUnit?
===============

But there is [DbUnit](http://dbunit.sourceforge.net/). We are using DbUnit. Why should we switch to _dataSet_?

There is no need, because the sql module is build on top of DbUnit. But you will also be able to use native DDL and DML (SQL). So _dataSet_ provides
a better way to use DbUnit.

So the question is not: Use DbUnit or _dataSet_?

**The actually question is: Why not using DbUnit with dataStore?**

How to install/clone all projects?
==================================

You can download each repository or you can use [dataSet Installer](https://github.com/loddar/dataset-install).

Datastore implementations
=========================

Currently are two Datastore implementations available:

* [Neo4J](https://github.com/loddar/datasore-neo4j)
* [Relational DB](https://github.com/loddar/datasore-sql)


Example
=======

    package com.company.module.my;

    import org.failearly.dataset.junit4.DataSetDriver;
    import org.failearly.dataset.DataSet;

    // JUnit imports omitted for brevity


    @DataSet
    public class MyTest {
        @Rule
        public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);

        @Test
        public void testUsingClassDataSet() {
            // 1. Before the test: dataSetDriver will look for a setup resource /com/company/module/my/MyTest.setup and apply it on the default DataStore.
            // If you don't define any datastore, the default will be used.

            .... your test ...

            // 2. After the test: dataSetDriver will look for a cleanup resource /com/company/module/my/MyTest.cleanup and if exists, then it will be applied.
        }

        @Test @DataSet
        public void testUsingAdditionalDataSet() {
            // 1. Before: dataSetDriver will look for a setup resource /com/company/module/my/MyTest.setup and apply it on the default DataStore
            // 2. And it will look for a setup resource /com/company/module/my/MyTest-testUsingAdditionalDataSet.setup
            //    and apply it on the default DataStore

            .... your test ...

            // 3. After: dataSetDriver will look for a cleanup resource /com/company/module/my/MyTest.cleanup and if exists, then it will be applied.
            // 4. And dataSetDriver will look for a cleanup resource /com/company/module/my/MyTesttestUsingAdditionalDataSet.cleanup and if exists,
            //    then it will be applied.
        }
    }

So the actually test class look like this:

    package com.company.module.my;

    import org.failearly.dataset.junit4.DataSetDriver;
    import org.failearly.dataset.DataSet;

    // JUnit imports omitted for brevity

    @DataSet
    public class MyTest {
        @Rule
        public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);

        @Test
        public void testUsingClassDataSet() {
            .... your test ...
        }

        @Test @DataSet
        public void testUsingAdditionalDataSet() {
            .... your test ...
        }
    }

Issues
======

If you want to report any issue or feature request, please do it [here](https://github.com/loddar/dataset/issues). 


Social
======

You can found me on

[Twitter](https://twitter.com/failearly)  
[LinkedIn](https://www.linkedin.com/in/markoumek)


License
=======

![GPL v3](http://www.gnu.org/graphics/gplv3-127x51.png)

