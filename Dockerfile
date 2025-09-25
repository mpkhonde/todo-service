# ---- Bygg-steg (Maven + JDK) ----
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiera pom.xml först (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiera resten av koden och bygg
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Kör-steg (endast JRE) ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Kopiera den färdiga jar-filen från byggsteget
COPY --from=build /app/target/*.jar app.jar

# Exponera HTTP-porten
EXPOSE 8080

# Starta applikationen
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
