FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/spam_ai_detection-0.0.1-SNAPSHOT.jar spam_ai_detection.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spam_ai_detection.jar"]