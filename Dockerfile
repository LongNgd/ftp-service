FROM eclipse-temurin:21
RUN adduser --disabled-password --home /home/ate ate && mkdir /home/ate/logs && chown ate:ate /home/ate/logs
USER ate
WORKDIR /home/ate
ADD target/ftp-service-0.0.1-SNAPSHOT.jar /home/ate/application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","application.jar"]
