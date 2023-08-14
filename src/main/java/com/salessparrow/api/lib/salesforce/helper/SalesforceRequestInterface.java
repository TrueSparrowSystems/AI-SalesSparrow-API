package com.salessparrow.api.lib.salesforce.helper;

@FunctionalInterface
public interface SalesforceRequestInterface<T> {
  T execute(String token);
}
