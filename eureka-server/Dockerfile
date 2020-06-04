FROM openjdk:8
ADD target/eureka.jar eureka.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka.jar"]