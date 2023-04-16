FROM c2aharbor.c2.dav.fr/docker-hub/bellsoft/liberica-openjdk-alpine:17.0.5
WORKDIR /app
COPY target/*.jar /app/app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar ${0} ${@}"]
