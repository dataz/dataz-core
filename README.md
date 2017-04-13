dataZ
=====

_dataZ_ provides testing support for Datastores.

A typical _Datastore_ is a database, either SQL or NoSql. But actually, any kind of persistence storage could be a _Datastore_.

If you have to test a module which is using a _Datastore_, you have are facing some of these *typical issues*:

* Tests are slow.
* Tests are brittle.
* Tests are _integration tests_.
* Tests could not executed concurrent.
* How should I provide test data (_test fixture_), so that the tests are repeatable and independent.
* _Test fixtures_ are hard to maintain: Just add a column to a table ... 
* A complex query to a database is worth it's own unit test (even this kind of test some people won't call this an unit test at all).
* Mocks are ... (your choice :-))

More issues are

* The tests should be fast and so you decide to use a different database then the productive one (for example [h2](http://http://www.h2database.com/)). But ...
* ... the tests should also be executed on the productive database.
* You have more then one datastore. For example: A single master database and for each tenant a different database instance.
* You have different types of datastores. For example: [MySql](https://www.mysql.com/) and [neo4j](https://neo4j.com//).
* You are making a product which is based on JPA, but every test should run also on any supported database.
* Performance tests with the productive database.
* _and even more trouble..._

**Brief: Writing tests are a pain in the ass.**

Everything which makes trouble, people try to avoid it. Another point is that is hard to apply TDD if a _Datastore_ is
part of your test.

Design Goals
============

So _dataZ_ makes testing with _Datastores_ a pleasure (again?). 

1. The general _design goal_ is to provide a framework which works for any kind of _Datastore_. 
1. It should close the gap between unit and integration tests. 
1. Having a open and extendable framework which is adaptable to any specific needs of your project.


Documentation
=============

*Under construction*

**[dataZ - Github Pages](https://dataz.github.io/)**

Build
=====

TODO: Deploy to maven central.

Datastore implementations
=========================

Currently are two Datastore implementations available:

* [Neo4J](https://github.com/dataz/datastore-neo4j)
* [Relational DB](https://github.com/dataz/datastore-sql)


Example
=======

TODO: Example repository


    package com.company.module.my;

    import org.failearly.dataz.junit4.DataSetDriver;
    import org.failearly.dataz.DataSet;

    // JUnit imports omitted for brevity


    @DataSet // <<<<<< Expects data (setup) resource /com/company/module/my/MyTest.setup >>>>>>
    public class MyTest {
        @Rule public final TestRule _dataZ_Driver = DataSetDriver.createDataSetDriver(this); // <<<< This driver which applies the data resources on a datastores

        @Test
        public void testUsingClassDataSet() {
            // Running your test against data resource /com/company/module/my/MyTest.setup
        }

        @Test @DataSet // <<<<<< Expects data (setup) resource /com/company/module/my/MyTest-testUsingMethodDataSet.setup >>>>>>
        public void testUsingMethodDataSet() {
            // Running your test against data resources 
			//            /com/company/module/my/MyTest.setup 
			//    and(!!) /com/company/module/my/MyTest-testUsingMethodDataSet.setup
        }
    }


Another possibility is to use a JUnit base class (_AbstractDataSetTest_) provided by _dataZ_.


	package com.company.module.my;

	import org.failearly.dataz.junit4.AbstractDataSetTest;
	import org.failearly.dataz.DataSet;

	// JUnit imports omitted for brevity

	@DataSet
	public class MyTest extends AbstractDataSetTest /* <<< Use this instead of TestRule >>>> */ {
	    @Test
	    public void testUsingClassDataSet() {
	        // ...
	    }

	    @Test @DataSet
	    public void testUsingMethodDataSet() {
	        // ...
	    }
	}


Issues
======

If you want to report any issue or feature request, please do it [here](https://github.com/dataz/dataz-core/issues). 


How to contribute?
==================

See [dataZ parent](https://github.com/dataz/dataz-install).

Social
======

You can found me on

[Twitter](https://twitter.com/failearly)  
[LinkedIn](https://www.linkedin.com/in/markoumek)


License
=======

[Eclipse Public License - v1.0](http://www.eclipse.org/org/documents/epl-v10.html)

