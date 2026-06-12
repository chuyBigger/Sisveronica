# Etapa 1: Build con Maven y Java 17 (Amazon Corretto ARM64)
FROM maven:3.9.9-amazoncorretto-17-alpine AS build
WORKDIR /app

# Copiar el pom y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
# Usamos package para generar el jar
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución ligera
FROM amazoncorretto:17-alpine
WORKDIR /app

# Crear un usuario no root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el jar desde la etapa de compilación
# Nota: Asegúrate que el nombre del archivo coincida con lo que genera Maven
COPY --from=build /app/target/SisVeronica-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]