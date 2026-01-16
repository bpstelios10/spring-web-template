FROM cassandra:5.0 as cassandra-base

MAINTAINER Stelios <bpstelios10@hotmail.com>

# exposing port here is mostly for documentation purposes. not really doing much
EXPOSE 9042

# create entry script for cassandra. This will execute all cql files in docker-entrypoint-initdb.d
COPY scripts/cassandra-entrypoint.sh /home/cassandra/cassandra-entrypoint.sh

ENTRYPOINT ["/home/cassandra/cassandra-entrypoint.sh"]
# --- end base image here --- #
FROM cassandra-base

COPY scripts/cql-bootstrap/*.cql /docker-entrypoint-initdb.d/

# set user of the container
USER cassandra

CMD ["cassandra", "-f"]
