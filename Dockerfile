# Stage 1: Build the Angular frontend
FROM node:20 AS frontend
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .

RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /app

COPY backend/pom.xml .
COPY backend/.mvn ./.mvn
COPY backend/src ./src

COPY --from=frontend /app/frontend/dist/frontend/browser ./src/main/resources/static/

RUN mvn package -DskipTests


FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=backend /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
