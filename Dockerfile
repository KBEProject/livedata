FROM openjdk:11
EXPOSE 8085
ADD target/spring-service-livedata.jar livedata.jar
ENTRYPOINT ["java","-jar","livedata.jar"]