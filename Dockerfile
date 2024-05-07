FROM openjdk:8-jdk-alpine
COPY Docker/datadis-analyzer/entrypoint.sh /
RUN chmod +x /entrypoint.sh
COPY target/datadis-analyzer-*-jar-with-dependencies.jar /app/datadis-analyzer.jar
ENTRYPOINT /entrypoint.sh