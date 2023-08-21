package com.salessparrow.api.lib.salesforce.helper;

/*
 * SalesforceRequestInterface is a functional interface for making a request to the Salesforce API.
 */
@FunctionalInterface
public interface SalesforceRequestInterface<T> {
  T execute(String token, String instanceUrl);
}
