# Environment Variables Description

This document provides descriptions for the environment variables used in the configuration of the Sales Sparrow api. These variables are essential for configuring and customizing the behavior of the application.

## ENCRYPTION_KEY

- **Description:** A key used for encryption purposes. This is a random string with alphanumeric values.
- **Example Value:** `7Fh9E2jRwDpV6gYcBqXsL`

## API_COOKIE_SECRET

- **Description:** Secret key for encrypting API cookies. This is a random string with alphanumeric values.
- **Example Value:** `7Fh9E2jRwDpV6gYcBqXsL`

## AWS_IAM_REGION

- **Description:** The AWS region for IAM (Identity and Access Management) services.
- **Example Value:** `us-east-1`

## KMS_KEY_ID

- **Description:** Amazon Resource Name (ARN) of the KMS (Key Management Service) key used for encryption.
- **Example Value:** `arn:aws:kms:'us-east-1':'111122223333':key/bc436485-5092-42b8-92a3-0aa8b93536dc`

## SALESFORCE_CLIENT_ID

- **Description:** Client ID for authenticating with Salesforce APIs.
- **Example Value:** `12345asdf`

## SALESFORCE_CLIENT_SECRET

- **Description:** Client secret for authenticating with Salesforce APIs.
- **Example Value:** `12345asdf`

## SALESFORCE_AUTH_URL

- **Description:** URL for Salesforce authentication.
- **Example Value:** `https://test.salesforce.com`

## SALESFORCE_WHITELISTED_REDIRECT_URIS

- **Description:** Comma-separated list of URIs allowed as redirect URIs for Salesforce authentication.
- **Example Value:** `http://localhost:3000,example://oauth/success`

## MEMCACHED_CACHE_HOST

- **Description:** Hostname of the Memcached cache server.
- **Example Value:** `memcached`

## MEMCACHED_CACHE_PORT

- **Description:** Port number for connecting to the Memcached cache server.
- **Example Value:** `11211`

## DYNAMO_DB_URL

- **Description:** URL for connecting to the DynamoDB server.
- **Example Value:** `http://dynamodb:8000`

## LOCAL_KMS_ENDPOINT

- **Description:** Endpoint for the local KMS (Key Management Service) server.
- **Example Value:** `http://localkms:8080`

## ERROR_MAIL_FROM

- **Description:** Email address used as the sender for error notifications.
- **Example Value:** `fromtest@test.com`

## ERROR_MAIL_TO

- **Description:** Email address where error notifications are sent.
- **Example Value:** `totest@test.com`

## LOG_LEVEL

- **Description:** Logging level for the application.
- **Example Value:** `debug`
