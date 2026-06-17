# Estágio de build (builder)
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace/app

# Copia os arquivos essenciais do maven e o pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dá permissão de execução ao mvnw e instala o curl no contêiner
RUN chmod +x ./mvnw && apt-get update && apt-get install -y curl

# Cria a pasta libs temporária e baixa o JAR direto do seu GitHub Releases
RUN mkdir -p libs && \
    curl -L -o libs/jwt-package-1.0.4.jar https://github.com/betolara1/JWT-Package/releases/download/v1.0.4/jwt-package-1.0.4.jar

# Instala o JAR baixado no repositório Maven do contêiner
RUN ./mvnw install:install-file \
    -Dfile=libs/jwt-package-1.0.4.jar \
    -DgroupId=com.betolara1 \
    -DartifactId=JWT-Package \
    -Dversion=1.0.4 \
    -Dpackaging=jar

# Baixa as dependências (faz cache na camada Docker)
RUN ./mvnw dependency:go-offline

# Copia o restante do código-fonte
COPY src src

# Faz o build do pacote jar
RUN ./mvnw clean package -DskipTests

# Estágio de runtime (produção)
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

ENV PORT=8081

COPY --from=builder /workspace/app/target/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
