# Etapa 1: Descargar dependencias (offline) para cachear
FROM maven:3.9-eclipse-temurin-21-alpine AS dependencies
WORKDIR /app
# Se copia el pom para descargar todas las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Etapa 2: Construcción del artefacto (compilación y empaquetado)
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Se reutilizan las dependencias cacheadas
COPY --from=dependencies /root/.m2 /root/.m2
# Se copia nuevamente el pom y el código fuente
COPY pom.xml .
COPY src ./src
# Empaqueta la aplicación sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa 3: Imagen final (solo runtime)
FROM openjdk:21-slim
WORKDIR /app
# Copia el JAR generado en la etapa build
COPY --from=build /app/target/*.jar app.jar
# Expone el puerto que se usará en ejecución
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
