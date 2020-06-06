FROM openjdk:8-alpine
ADD target/user.jar user.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user.jar"]