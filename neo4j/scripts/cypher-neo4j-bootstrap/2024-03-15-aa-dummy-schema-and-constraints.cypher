CREATE CONSTRAINT uniqueMovieTitleConstraint IF NOT EXISTS FOR (n:Movie) REQUIRE (n.title) IS UNIQUE;
CREATE CONSTRAINT uniquePersonNameConstraint IF NOT EXISTS FOR (n:Person) REQUIRE (n.name) IS UNIQUE;
