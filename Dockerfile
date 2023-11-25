# Use a imagem do Maven para compilar o projeto
FROM maven:3.6.3-openjdk-8 AS build

# Configurar o diretório de trabalho
WORKDIR /app

# Copiar apenas o arquivo POM para obter dependências
COPY ./pom.xml .

# Baixar as dependências do Maven
RUN mvn dependency:go-offline

# Copiar o código-fonte
COPY ./src ./src

# Compilar o projeto
RUN mvn package

# Use a imagem do OpenJDK para a execução da aplicação
FROM openjdk:8-jdk-alpine

# Configurar o diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado da etapa de compilação
COPY --from=build /app/target/springboot-restful-webservices-0.0.1-SNAPSHOT.jar /app/springboot-restful-webservices.jar

# Comando de entrada para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "springboot-restful-webservices.jar"]
