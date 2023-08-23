#!/usr/bin/env bash

export ENVIRONMENT='local-test'
export AWS_ACCESS_KEY_ID='local'
export AWS_SECRET_ACCESS_KEY='local'
export AWS_REGION='us-east-1'

./mvnw test