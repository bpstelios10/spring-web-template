# CASSANDRA

## Table rented-movies

This table has a partition key (client_id) and a clustering key (movie_id). This way we can achieve:

1) Both queries needed for the service ('find all movies for a user' and 'check if a movie is rented by user')
2) Data will be properly spread across nodes, since client_id is a UUID and we expect many clients (with different ids)
   renting movies

## Dockerfile

The dockerfile handles the creation of a keyspace `test_cassandra` and a table `rented_movies`. Any cql script
under [cql-bootstrap folder](scripts/cql-bootstrap) will be executed at the startup of the image.
Startup:

```shell
# manually cassandra folder (if volume is needed, add '-v /my/own/datadir:/var/lib/cassandra')
docker build . -t cassandra-test
docker run -p 9042:9042 cassandra-test
# or using docker compose from project root folder
docker-compose up --build
```

## Helpful Queries

```roomsql
INSERT INTO test_cassandra.rented_movies(client_id,movie_id,times_rented,date_rented) VALUES (f9734631-6833-4885-93c5-dd41679fc908, c37d661d-7e61-49ea-96a5-68c34e83db3a, 1, toTimestamp(now()));
SELECT * FROM test_cassandra.rented_movies WHERE client_id=f9734631-6833-4885-93c5-dd41679fc908;
SELECT * FROM test_cassandra.rented_movies WHERE client_id=f9734631-6833-4885-93c5-dd41679fc908 AND movie_id=c37d661d-7e61-49ea-96a5-68c34e83db3a;
```
