CREATE KEYSPACE IF NOT EXISTS test_cassandra
  WITH REPLICATION = {
   'class' : 'SimpleStrategy',
   'replication_factor' : 1
  };
