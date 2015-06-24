dataSet
=======

_dataSet_ provides testing support for Datastores.

A typical datastore is a database, either SQL or NoSql.

If you have to test a module which is using a datastore(s), you have are facing some more or less typical issues:

* How should I provide test data (test fixture), so that the tests are repeatable and independent.
* A complex query to a database is worth it's own unit test (even this kind of test some people won't call this an unit test at all).
* The tests should be fast and so you decide to use a different database then the productive one (for example [h2](http://http://www.h2database.com/)). But ...
* ... the tests should also be executed on the productive database.
* You have more then one datastore. For example: A single master database and for each tenant a different database instance.
* You have different types of datastores. For example: MySql and memcache.
* You are making a product which is based on JPA, but every test should run also on any supported database.
* Performance tests with the productive database.

_dataSet_ is an attempt to provide solutions for these issues.


Documentation
=============

The documentation could be found [here](dataset-doc.readthedocs.org).

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


    @DataSet // <<<<<< Expects data (setup) resource /com/company/module/my/MyTest.setup >>>>>>
    public class MyTest {
        @Rule public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this); // <<<< This driver which applies the data resources on a datastores

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

Another possibility is to use the JUnit base class (_AbstractDataSetTest_) provided by dataSet.

	package com.company.module.my;

	import org.failearly.dataset.junit4.AbstractDataSetTest;
	import org.failearly.dataset.DataSet;

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

If you want to report any issue or feature request, please do it [here](https://github.com/loddar/dataset/issues). 


Social
======

You can found me on

[Twitter](https://twitter.com/failearly)  
[LinkedIn](https://www.linkedin.com/in/markoumek)


License
=======

![GPL v3](http://www.gnu.org/graphics/gplv3-127x51.png)

