FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY app/build/libs/*.jar app.jar
COPY config/application.properties config/application.properties
ENTRYPOINT ["java","-jar","/app.jar"]