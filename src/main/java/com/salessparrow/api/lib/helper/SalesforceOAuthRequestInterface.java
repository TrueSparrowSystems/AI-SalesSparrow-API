package com.salessparrow.api.lib.helper;

@FunctionalInterface
public interface SalesforceOAuthRequestInterface<T> {
  T execute(String token);
}
