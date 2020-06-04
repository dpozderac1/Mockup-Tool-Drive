FROM openjdk:8
ADD target/project.jar project.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "project.jar"]