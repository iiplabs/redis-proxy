FROM maven:3.8.6-amazoncorretto-19 AS buildOne
WORKDIR /app
COPY . /app
RUN mvn package

FROM amazoncorretto:19-al2-jdk as buildTwo
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=buildOne ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:19-al2-jdk
WORKDIR /app
COPY --from=buildTwo /app/dependencies/ ./
COPY --from=buildTwo /app/snapshot-dependencies/ ./
COPY --from=buildTwo /app/spring-boot-loader/ ./
COPY --from=buildTwo /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]