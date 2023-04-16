FROM bellsoft/liberica-openjdk-alpine:17.0.6-10 as build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM bellsoft/liberica-openjdk-alpine:17.0.6-10
VOLUME /tmp
RUN adduser -D aircraft && addgroup wrk && addgroup aircraft wrk
USER aircraft:wrk
WORKDIR /app
ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.da.wrk.aircraft.AircraftApplication"]
