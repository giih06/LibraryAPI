# build
FROM maven:3.8.8-amazoncorretto-21-al2023 as build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

# run
FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build ./build/target/*.jar ./libraryapi.jar

EXPOSE 8080
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USERNAME=''
ENV DATASOURCE_PASSWORD=''
ENV GOOGLE_CLIENT_ID='client_id'
ENV GOOGLE_CLIENT_SECRET='client_secret'

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar libraryapi.jar

# docker run --name libraryapi-production -e DATASOURCE_URL=jdbc:postgresql://librarydb-prod:5432/library -e DATASOURCE_USERNAME=postgresprod -e DATASOURCE_PASSWORD=postgresprod --network library-network -d -p 8080:8080 -p 9090:9090 giih06/libraryapi