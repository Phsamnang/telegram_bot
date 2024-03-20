FROM openjdk:21-jre-alpine
WORKDIR /app
COPY --from=build build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
