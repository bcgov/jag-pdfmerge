# jag-document-utils

[![Maintainability](https://api.codeclimate.com/v1/badges/a0b23562b87853f9824b/maintainability)](https://codeclimate.com/github/bcgov/jag-document-utils/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/a0b23562b87853f9824b/test_coverage)](https://codeclimate.com/github/bcgov/jag-document-utils/test_coverage)

Justice Sector PDF Merge Microservice integration with Adobe Experience Manager

## Run locally

Create a .ENV file and set the following values to an AEM server instance:

```env
DOCMERGE_AEM_ENDPOINT=
DOCMERGE_AEM_USER=
DOCMERGE_AEM_PASSWORD=
```

Run

```bash
docker-compose up
```

## Deployment Instructions 

As of 2022-01-04 the pipelines for this are not functioning due to the adobe library being moved to a different repository 
that is not publicly accessible. Below will detail the workaround to deploy critical fixes.

1) Get access to the jar files. The jars you need are:
   1) adobe-assembler-client-11.0.0.jar
   2) adobe-docconverter-client-11.0.0.jar
   3) adobe-livecycle-client-11.0.0.jar.
   4) adobe-usermanager-client-11.0.0.jar
2) Copy these jars into the resources folder.
3) Running ```mvn package``` should not work. You may need to skip the tests.
4) Run ```docker login <Openshift Tools Image repo> -u <Username>```. The password is your token from the cluster.
5) Build the local image. ```docker build -t <Image Repo>/<Image Name>:<Tag> -f Dockerfile.local .```
6) Push the image and it will auto deploy. The tag should be dev to deploy to dev ```docker push <Image Repo>/<Image Name>:<Tag>```
7) Tag the image to other tags to trigger other deployments eg. test ```oc tag <Image Repo>/<Image Name>:<Source Tag> <Image Name>:<Destination Tag>```
