# NEO4J

## Database for watched movies

This table has labels:
- `Movie`
- `Person` (either actors/directors/producers or audience) 

and relationships like:
- `ACTED_IN` with property `roles` of type list
- `DIRECTED`
- `PRODUCED`
- `WATCHED` with property `rating` of type int

## Dockerfile

Startup:

```shell
# manually 
# neo4j folder (if volume is needed, add '-v /my/own/datadir:/neo4j/data')
docker build . -t neo4j-test
docker run -p 7474:7474 -p 7687:7687 --env NEO4J_AUTH=neo4j/neo4jpass neo4j-test
# using docker compose from project root folder
docker-compose up --build
```

### Conf
There is a copy of the default neo4j.conf file but not used. if needed, you have to uncomment the relevant line in Dockerfile

## Accessing the Neo4j Browser
From a local browser use this URL (unless you ve changed the 7474 port): http://localhost:7474/browser/.

## Helpful Queries
```roomsql
MATCH (m:Movie) RETURN m;
MATCH (tom:Person {name: "Tom Hanks"}) RETURN tom;
MATCH (nineties:Movie) WHERE nineties.released >= 1990 AND nineties.released < 2000 RETURN nineties.title;
MATCH (people:Person) RETURN people.name LIMIT 10;

MATCH (tom:Person {name: "Tom Hanks"})-[:ACTED_IN]->(tomHanksMovies) RETURN tom,tomHanksMovies;
MATCH (people:Person)-[relatedTo]-(:Movie {title: "Cloud Atlas"}) RETURN people.name, Type(relatedTo), relatedTo.roles;

// Up to 4 hops
MATCH (bacon:Person {name:"Kevin Bacon"})-[*1..4]-(hollywood)
RETURN DISTINCT hollywood;
// shortest path between 2 people
MATCH p=shortestPath(
(bacon:Person {name:"Kevin Bacon"})-[*]-(meg:Person {name:"Meg Ryan"})
)
RETURN p;

MATCH (a)-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN a,m,d LIMIT 10;

MATCH (a:Person)-[:WATCHED]->(m:Movie) RETURN a,m LIMIT 10;
MATCH (a:Person)-[rel:WATCHED]->(m:Movie) WHERE rel.rating > 0 RETURN a,m LIMIT 10;
MATCH (audience3:Person {name:'Kevin Pollak'})-[:WATCHED]->(m:Movie) RETURN audience3, m;
MATCH (a:Person)-[rel:WATCHED]->(m:Movie) WHERE rel.rating>0 RETURN a,m;

// recommendation queries:
// find movies that other users have watched (matrix) and rated the same as the one you just rated by user (client2)
MATCH (client:Person {name:'client2'})-[r:WATCHED]->(movie:Movie {title:'The Matrix'})
WITH r.rating as rating1, client
MATCH (rec:Movie)<-[:WATCHED {rating:(rating1)}]-(a:Person)-[:WATCHED {rating:(rating1)}]->(m:Movie {title:'The Matrix'}) WHERE a<>client RETURN rec;
```
