# Etapa 1: build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Baixa dependências em cache
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -DskipTests

# Etapa 2: runtime (imagem enxuta)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Argumentos/variáveis com defaults
ENV JAVA_OPTS=""
ENV DB_URL="jdbc:postgresql://db:5432/biblioteca" \
    DB_USER="postgres" \
    DB_PASSWORD="postgres" \
    JWT_SECRET="my-secret-key"

# Copia o JAR (usa wildcard para versão)
COPY --from=build /app/target/biblioteca-api-*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
