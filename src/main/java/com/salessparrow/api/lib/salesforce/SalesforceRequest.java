package com.salessparrow.api.lib.salesforce;

@FunctionalInterface
public interface SalesforceRequest<T> {
  T execute(String token) throws Exception;
}
