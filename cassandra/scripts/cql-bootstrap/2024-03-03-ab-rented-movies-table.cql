CREATE TABLE IF NOT EXISTS test_cassandra.rented_movies (
   client_ID UUID,
   movie_ID UUID,
   times_rented int,
   date_rented timestamp,
   PRIMARY KEY (client_ID, movie_ID));
