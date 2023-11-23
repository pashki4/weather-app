FROM maven:3.9.3-eclipse-temurin-17-focal AS build

WORKDIR /app

COPY . .

RUN mvn clean install

FROM tomcat:10.1.6

EXPOSE 8080

COPY --from=build /app/target/weather-app.war /usr/local/tomcat/webapps/