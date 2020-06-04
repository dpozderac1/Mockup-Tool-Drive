FROM openjdk:8-alpine
ADD target/zuul.jar zuul.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "zuul.jar"]