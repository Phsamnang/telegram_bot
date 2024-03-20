FROM openjdk:19-jdk

#COPY target/VCR-0.0.1-SNAPSHOT.jar .

COPY  /build/libs/telegram-bot-0.0.1-SNAPSHOT.jar .


CMD ["java", "-jar", "telegram-bot-0.0.1-SNAPSHOT.jar"]
