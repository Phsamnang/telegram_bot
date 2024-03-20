FROM openjdk:lates
WORKDIR /app
COPY --from=build build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
