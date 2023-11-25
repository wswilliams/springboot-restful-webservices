# Etapa 1: Use uma imagem Maven para compilar o projeto
FROM maven:3.8.1-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean install

# Etapa 2: Use uma imagem OpenJDK para a execução da aplicação
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copiar o JAR gerado da etapa de compilação
COPY --from=build /app/target/springboot-restful-webservices-0.0.1-SNAPSHOT.jar /app/springboot-restful-webservices.jar

# Comando de entrada para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "springboot-restful-webservices.jar"]