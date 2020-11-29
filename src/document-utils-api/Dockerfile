#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.6.3-jdk-8 as build

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
FROM openjdk:8-jdk-slim

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./target/*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
