docker run --rm --network=sombra_default \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://sombra_demo_db:3306/task_server_schema \
  -e SPRING_DATASOURCE_USERNAME=batch_user \
  -e SPRING_DATASOURCE_PASSWORD=batchuser \
  sombra/task_with_docker:latest
