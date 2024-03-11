#!/bin/bash

for f in /docker-entrypoint-initdb.d/*;
do
  echo "$f"
  case "$f" in
    *.sh) echo "$0: running $f"; . "$f" ;;
    *.cql) echo "$0: running $f" && until cqlsh -f "$f"; do >&2 echo "Cassandra is unavailable - sleeping"; sleep 7; done & ;;
    *) echo "$0: ignoring $f" ;;
  esac
done

echo "done"

exec /usr/local/bin/docker-entrypoint.sh "$@"
