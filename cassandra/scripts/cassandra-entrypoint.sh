#!/bin/sh

bootstrap() {
  until cqlsh -k system
    do
      >&2 echo "Cassandra is unavailable right now. Waiting..."
      sleep 7
    done

  for f in /docker-entrypoint-initdb.d/*;
  do
    echo "$f"
    case "$f" in
      *.sh) echo "$0: running $f"; . "$f" ;;
      *.cql) echo "$0: running $f"; cqlsh -f "$f" ;;
      *) echo "$0: ignoring $f" ;;
    esac
  done

  echo "done"
}

bootstrap &

exec /usr/local/bin/docker-entrypoint.sh "$@"
