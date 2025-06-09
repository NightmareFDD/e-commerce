FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /workspace/product-service/target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]