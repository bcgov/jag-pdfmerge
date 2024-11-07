#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.8.3-openjdk-17 as build

ARG MVN_PROFILE
ARG SKIP_TESTS=false

ENV MVN_PROFILE=${MVN_PROFILE}

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn -B clean package \
        -P ${MVN_PROFILE} \
        -Dmaven.test.skip=${SKIP_TESTS}

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM eclipse-temurin:17-jre-alpine

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./target/*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
