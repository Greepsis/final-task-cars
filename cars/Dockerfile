FROM openjdk:15

ADD ServiceAPI/target/ServiceAPI-1.0-SNAPSHOT.jar ServiceAPI.jar
ENTRYPOINT ["java", "-jar", "ServiceAPI.jar"]
ADD ServiceImpl/target/ServiceImpl-1.0-SNAPSHOT.jar ServiceImpl.jar
ENTRYPOINT ["java", "-jar", "ServiceImpl.jar"]
ADD RestService/target/RestService-1.0-SNAPSHOT.jar RestService.jar
ENTRYPOINT ["java", "-jar", "RestService.jar"]