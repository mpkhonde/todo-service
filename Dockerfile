# ---------------------------------------------------
# Dockerfile – todo-service (Spring Boot 3, Java 17)
# Multi-stage build: bygger JAR i steg 1, kör smal JRE i steg 2
# ---------------------------------------------------

# ---- Bygg-steg (Maven + JDK) ----------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiera först pom.xml för bättre cache vid ändringar i koden
COPY pom.xml .
# Kopiera källkod och bygg JAR (utan tester för snabbare build)
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Kör-steg (endast JRE) ------------------------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Kopiera byggd JAR från föregående steg och döp till app.jar
COPY --from=build /app/target/*.jar app.jar

# (Valfritt) JVM-flaggor vid runtime: sätt t.ex. -Xms256m -Xmx512m i compose/run
ENV JAVA_OPTS=""

# Exponera HTTP-porten
EXPOSE 8080

# Starta applikationen (shell-form så JAVA_OPTS expanderas)
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
