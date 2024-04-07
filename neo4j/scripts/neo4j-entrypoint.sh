#!/bin/sh

bootstrap() {
  until cypher-shell -u neo4j -p neo4jpass "match (n) return count(n)"
    do
      >&2 echo "Neo4j is unavailable right now. Waiting..."
      sleep 3
    done

  for f in /docker-entrypoint-initdb.d/*;
  do
    echo "$f"
    case "$f" in
      *.sh) echo "$0: running $f"; . "$f" ;;
      *.cypher) echo "$0: running $f"; cypher-shell -u neo4j -p neo4jpass -f "$f" ;;
      *) echo "$0: ignoring $f" ;;
    esac
  done

  echo "done"
}

bootstrap &

exec tini -g -- "/startup/docker-entrypoint.sh" "$@"
