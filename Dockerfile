
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=runtime dependencies/ ./
COPY --from=runtime spring-boot-loader/ ./
COPY --from=runtime snapshot-dependencies/ ./
COPY --from=runtime application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
