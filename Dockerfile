FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn package


FROM openjdk:17-slim-buster
WORKDIR /app
COPY --from=build /app/target/*.jar ./npc-bot.jar
CMD ["java", "-jar", "npc-bot.jar"]
