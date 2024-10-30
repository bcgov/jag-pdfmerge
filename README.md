# jag-document-utils

[![Lifecycle:Stable](https://img.shields.io/badge/Lifecycle-Stable-97ca00)](https://img.shields.io/badge/Lifecycle-Stable-97ca00) [![Maintainability](https://api.codeclimate.com/v1/badges/a0b23562b87853f9824b/maintainability)](https://codeclimate.com/github/bcgov/jag-document-utils/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/a0b23562b87853f9824b/test_coverage)](https://codeclimate.com/github/bcgov/jag-document-utils/test_coverage)

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
