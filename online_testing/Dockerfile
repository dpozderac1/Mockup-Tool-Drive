FROM openjdk:8-alpine
ADD target/online-testing.jar online-testing.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "online-testing.jar"]