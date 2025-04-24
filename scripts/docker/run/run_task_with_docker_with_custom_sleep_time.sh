#!/bin/bash

# Get message from first argument or set default
MESSAGE="${1:-Hello Everyone}"

# Get sleep duration from second argument or set default to 15 seconds
SLEEP_DURATION="${2:-15}"

# Get task name from third argument or set default
TASK_NAME="${3:-task_with_docker_from_terminal}"

docker run --rm --network=sombra_default \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://sombra_demo_db:3306/task_server_schema \
  -e SPRING_DATASOURCE_USERNAME=batch_user \
  -e SPRING_DATASOURCE_PASSWORD=batchuser \
  -e SPRING_APPLICATION_NAME="$TASK_NAME" \
  sombra/task_with_docker:latest "$MESSAGE" "$SLEEP_DURATION"
