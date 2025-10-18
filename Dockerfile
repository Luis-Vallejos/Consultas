# Usar una imagen base de Java 17
FROM eclipse-temurin:17-jdk-jammy

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar el archivo JAR compilado al contenedor
COPY target/consultas-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
