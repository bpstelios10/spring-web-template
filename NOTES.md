# RUNNING LOCAL TESTS WITH CASSANDRA
A million issues with embedded cassandras and stubbed cassandras interacting with Java 21 (and Windows)

## Option 1 
(like https://github.com/nosan/embedded-cassandra) is to download a cassandra and try to start it up with code.
But cassandra 4+ doesnt provide any runner for windows any more. So this makes solution complex.
https://downloads.apache.org/cassandra/

## Option 2 
(like https://github.com/MeteoGroup/cassandra-embedded-for-test) use `org.apache.cassandra:cassandra-all` and
possibly `com.datastax.oss:java-driver-core`.
But Java21 fails some reflection code (at least in cassandra 4+).

## Option 3 
(like this project) use docker-java-client to spin up cassandra docker container before tests and/or local
execution. (in this case we dont use the docker-java-client for local executions to avoid have it in the jar that is
supposed to go to production. instead we can manually start cassandra and keep it running, so that we can test changes
fast. docker-compose can be used too, but testing service changes might be slower).
PS: This solution of course takes ages, for starting the cassandra image each time.

# CASSANDRA MIGRATION TOOLS
(if needed in the future)
Migration tool for cassandra: https://github.com/patka/cassandra-migration / https://github.com/sky-uk/cqlmigrate.
This might be a solution for linux: https://github.com/nosan/embedded-cassandra.
