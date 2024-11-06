# Koristi Maven image za izgradnju aplikacije
FROM maven:3.8.5-openjdk-17 AS build

# Postavi radni direktorijum unutar kontejnera
WORKDIR /app

# Kopiraj pom.xml i preuzmi zavisnosti (caching layer za brže build-ovanje)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiraj ostatak aplikacije i izgradi aplikaciju bez pokretanja testova
COPY . .
RUN mvn clean package -DskipTests

# Koristi manji JDK image za pokretanje aplikacije
FROM openjdk:17-jdk-slim

# Postavi radni direktorijum unutar kontejnera
WORKDIR /app

# Kopiraj izgrađeni jar fajl iz Maven build faze
COPY --from=build /app/target/*.jar /app/app.jar

# Izloži port 8080 za aplikaciju
EXPOSE 8080

# Pokreni Spring Boot aplikaciju
CMD ["java", "-jar", "/app/app.jar"]
