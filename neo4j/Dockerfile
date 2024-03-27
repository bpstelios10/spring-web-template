FROM neo4j:5.18.1 as neo4j-base

MAINTAINER Stelios <bpstelios10@hotmail.com>

# exposing port here is mostly for documentation purposes. not really doing much
EXPOSE 7474 7687

# create a group and user (no uid) to run as non-root. create home dir for this user
RUN groupadd neo4j_executors_group && useradd -m -g neo4j_executors_group neo4j_executor
# Give any priviledges needed. with neo4j nothing is needed right now
# RUN chown -R neo4j_executor:neo4j_executors_group /var/lib/neo4j/bin/neo4j ???

# set workdir of the container to the created user's home folder
WORKDIR /home/neo4j_executor
# create entry script for neo4j. This will execute all cypher files in docker-entrypoint-initdb.d
COPY scripts/neo4j-entrypoint.sh /home/neo4j_executor/neo4j-entrypoint.sh

# Set new password for neo4j at startup
ENV NEO4J_AUTH=neo4j/neo4jpass

# Copy configuration file
# ADD conf /var/lib/neo4j/conf

# base image : ENTRYPOINT ["tini", "-g", "--", "/startup/docker-entrypoint.sh"]
ENTRYPOINT ["/home/neo4j_executor/neo4j-entrypoint.sh"]
# --- end base image here --- #
FROM neo4j-base

COPY scripts/cypher-neo4j-bootstrap/*.cypher /docker-entrypoint-initdb.d/

# set user of the container
USER neo4j_executor

CMD ["neo4j", "console"]
