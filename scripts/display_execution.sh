#!/bin/sh

FILE_LOCATION="$1"

if [ -z "$FILE_LOCATION" ]; then
  echo "Error: FILE_LOCATION not provided."
  exit 1
fi

if [ ! -f "$FILE_LOCATION/stdout.log" ]; then
  echo "Error: stdout.log not found at $FILE_LOCATION"
  exit 1
fi

cat "$FILE_LOCATION/stdout.log"