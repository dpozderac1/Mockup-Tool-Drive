FROM openjdk:8-alpine
ADD target/eureka.jar eureka.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka.jar"]