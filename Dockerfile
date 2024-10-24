FROM maven:3.8.3-openjdk-17 as build

COPY pom.xml /usr/src/app/pom.xml
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.main.skip -Dspring-boot.repackage.skip=true -Dmaven.test.skip

COPY src /usr/src/app/src
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:17-oracle
COPY --from=build /usr/src/app/target/minibridge-0.0.1-SNAPSHOT.jar /usr/app/minibridge-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-Dspring.profiles.active=prod", "-jar", "/usr/app/minibridge-0.0.1-SNAPSHOT.jar" ]