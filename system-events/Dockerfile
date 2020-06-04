FROM openjdk:8
ADD target/system-events.jar system-events.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "system-events.jar"]