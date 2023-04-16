# Config & Run Spring Boot App

## Building application

### Pre-requisites
- JDK 17
- maven 3+
- docker CLI or image build tools

### Building executable JAR
```jar
$ mvn clean package -DskipTests -s maven-settings.xml
```

### Set environment variables using jar mode
Linux | Windows 
--- | ---
export ENV=$DEV | set ENV=%DEV% 
export WRK_AIRCRAFT_DATASOURCE_URL=jdbc:h2:mem:wrk-aircraft | set WRK_AIRCRAFT_DATASOURCE_URL=jdbc:h2:mem:wrk-aircraft 
export WRK_AIRCRAFT_DATASOURCE_USR=sa | set WRK_AIRCRAFT_DATASOURCE_USR=sa
export WRK_AIRCRAFT_DATASOURCE_PWD=YWRtaW4wCg== | set WRK_AIRCRAFT_DATASOURCE_PWD=YWRtaW4wCg==

### Run the app
Using `Jar File`:
```java
$ java -jar target/wrk-aircraft-4.3.2.jar

$ java -Dspring.datasource.url=jdbc:h2:mem:wrk-aircraft \
    -Dspring.datasource.username=sa \
    -Dspring.datasource.password=YWRtaW4wCg== \
    -Dapp.env=DEV
    -jar target/wrk-aircraft-4.3.2.jar

$ java -jar wrk-aircraft-4.3.2.jar \
    --spring.datasource.url=jdbc:h2:mem:wrk-aircraft
    --spring.datasource.username=sa \
    --spring.datasource.password=YWRtaW4wCg== \
    --app.env=DEV
    
$ java -Dspring.datasource.url=jdbc:h2:mem:wrk-aircraft \
    -jar target/wrk-aircraft-4.3.2.jar \
    --spring.datasource.username=sa \
    --spring.datasource.password=YWRtaW4wCg== \
    --app.env=DEV   
```
### Test the app
```bash
$ curl localhost:8080/aircrafts
$ curl localhost:8080/aircrafts/1

$ browse to localhost:8080/swagger-ui/index.html with /api-docs
```

## Building the app in a Docker image to run it in a K8s cluster
Using this generic `Dockerfile`:
```Dockerfile
FROM openjdk:17-jdk-alpine3.14
WORKDIR /app
COPY target/*.jar /app/app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar ${0} ${@}"]

 (*) The container will run with a non-root user through kubernetes
```

Build container image:
```docker
$ docker build -t wrk-k8s/wrk-aircraft:4.3.2 .
```

Tag and Push this image to Container Registry:
```docker
$ docker tag wrk-k8s/wrk-aircraft:4.3.2 wrk-k8s/wrk-aircraft:4.3.2
$ docker push wrk-k8s/wrk-aircraft:4.3.2
```

### Run the image
```docker
$ docker run -d --name wrka -p 8080:8080 \
    wrk-k8s/wrk-aircraft:4.3.2

docker run -d --name wrka -p 8080:8080 -e JAVA_OPTS="-Dspring.datasource.url=jdbc:h2:mem:wrk-aircraft -Dspring.datasource.username=sa -Dspring.datasource.password=YWRtaW4wCg==" -e ENV="DEV" wrk-k8s/wrk-aircraft:4.3.2
$ docker run -d --name wrka -p 8080:8080 \
    -e JAVA_OPTS="-Dspring.datasource.url=jdbc:h2:mem:wrk-aircraft -Dspring.datasource.username=sa -Dspring.datasource.password=YWRtaW4wCg==" \
    -e ENV="DEV" \
    wrk-k8s/wrk-aircraft:4.3.2

$ docker run -d --name wrka -p 8080:8080 \
    -e JAVA_OPTS="-Dspring.datasource.url=jdbc:h2:mem:wrk-aircraft" \
    -e ENV="DEV" \
    wrk-k8s/wrk-aircraft:4.3.2 \
    --spring.datasource.username=sa \
    --spring.datasource.password=YWRtaW4wCg==    
```
### Launch a command into the container 
```docker
$ docker exec -it wrka /bin/sh

$ docker exec -it wrka ls -al
docker container logs %1
```
### See logs into the container 
```docker
$ docker container logs wrka
```
## Deploy to Kubernetes

### Deploy to Kubernetes cluster using K8S resources
```kubectl
$ kubectl apply -f wrk-k8s
```

### Get the pod
```kubectl
$ kubectl -n wrk-k8s get pod

NAME                            READY   STATUS    RESTARTS   AGE
wrk-aircraft-7c6bf579dd-xp77x   1/1     Running   0          15d
```

## Use the Workflow K8s Pipeline

### Using the generic `Jenkinsfile` pipeline
