package com.salessparrow.api.domain;

/**
 * User interface.
 */
public interface User {

  String getExternalUserId();

  String getEmail();

  String getName();

  String getUserType();

  String getCookieToken();

  String getEncryptionSalt();

}
