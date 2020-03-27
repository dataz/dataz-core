dataZ Core
==========

_dataZ_ provides testing support for Datastores. 

This module builds the **Core** of _dataZ_. It contains base implementation and interfaces for 

* customization for your project needs. 
* the datastore implementations (see below).
* and of course basic template object implementations. These are Java annotation which carries the actually data which will be used
in the setup/cleanup templates. You will be able to create your own template objects.

You have add a _Datastore_ implementation 

* [Neo4J](https://github.com/dataz/datastore-neo4j)
* [Relational DB](https://github.com/dataz/datastore-sql) 


and a test driver

* [JUnit4](https://github.com/dataz/dataz-junit4)
* _JUnit5 has not yet implemented_
* _Spock has not yet implemented_


Documentation has been moved to [dataZ](https://github.com/dataz/dataz).