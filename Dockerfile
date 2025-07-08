# Etapa 1: Construcción con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiamos solo lo necesario
COPY pom.xml ./
COPY src ./src

# Compilamos la aplicación, omitiendo tests
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con solo JRE de Java 21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos el JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
