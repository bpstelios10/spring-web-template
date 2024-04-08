# RUNNING APPLICATION WITH NEO4J
For local executions, there is an embedded neo4j server, provided by the library : https://github.com/neo4j/neo4j-java-driver-spring-boot-starter
Although it is archived, it is still usable, even in latest `"org.springframework.boot:spring-boot-starter-data-neo4j:3.2.4"`

## Unit Tests
Running unit tests with spring runner is easy with neo4j. In cases where there is no interaction with the db, like in PrivateEndpointTests.java,
there is nothing needed for you to do. Spring wont fail to execute such tests.
In cases where there is interaction with db, like in FunctionalEndpointTests.java, you can either just use mockBean on the repositories
(or other places where you interact with neo4j), or use `org.neo4j.test:neo4j-harness` from the library mentioned before.
For the later (more complex), there is the implementation provided in this test class.

NOTE: for tests using neo4j-harness, memory requirements are higher, so we need to increase heap-size for the task (see build.gradle).

## Local Executions


## MIGRATION TOOLS?
