# Sales Sparrow APIs: Getting Started Guide

## Prerequisites and System Requirements

Before using the *Sales Sparrow* APIs, make sure your development environment meets the following prerequisites and system requirements:

### Docker

- Docker version 4.19.0 or newer

## Getting Started

To clone the project and install dependencies, follow these steps:

### Clone the Project

```sh
$ git clone git@github.com:TrueSparrowSystems/AI-SalesSparrow-API.git

$ cd AI-SalesSparrow-API
```

### Clone the AI-SalesSparrow-Docs Submodule

```sh
$ git submodule update --init
```

### Set Environment Variables

1. Copy the contents of `sample.secrets.json` to `secrets.json`.
2. Update the values of the environment variables in secrets.json:
    - Copy Salesforce credentials from the Salesforce connected app and update `SALESFORCE_CLIENT_ID`, `SALESFORCE_CLIENT_SECRET`, `SALESFORCE_AUTH_URL`.
    - Update `KMS_KEY_ID` with value `arn:aws:kms:'us-east-1':'111122223333':key/bc436485-5092-42b8-92a3-0aa8b93536dc`. This local Docker setup uses the [local-kms](https://hub.docker.com/r/nsmithuk/local-kms) Docker image, and the key is already configured using [seed](init/seed.yaml).

### Start the API Server with Docker

```sh
$ docker-compose up api
```

### Run Test Cases with Docker

#### Set Test-Related Environment Variables

1. Create a `test.secrets.json` file:
```sh
$ touch test.secrets.json
```

2. Add the following environment variables to `test.secrets.json`:
```json
{
    "ENCRYPTION_KEY": "1234567890",
    "API_COOKIE_SECRET": "1234567890",
    "AWS_IAM_REGION": "us-east-1",
    "KMS_KEY_ID": "arn:aws:kms:'us-east-1':'111122223333':key/bc436485-5092-42b8-92a3-0aa8b93536dc",
    "SALESFORCE_CLIENT_ID": "12345",
    "SALESFORCE_CLIENT_SECRET": "12345",
    "SALESFORCE_AUTH_URL": "https://test.salesforce.com",
    "SALESFORCE_WHITELISTED_REDIRECT_URIS": "http://localhost:3000",
    "MEMCACHED_CACHE_HOST": "memcached",
    "MEMCACHED_CACHE_PORT": "11211",
    "LOG_LEVEL": "debug",
    "DYNAMO_DB_URL": "http://dynamodb:8000",
    "LOCAL_KMS_ENDPOINT": "http://localkms:8080",
    "ERROR_MAIL_FROM": "",
    "ERROR_MAIL_TO": "",
    "COOKIE_DOMAIN":""
}
```

#### Run Test Cases

```sh
$ docker-compose up test
```
To view the test coverage, simply open the target/site/index.html file in a web browser.