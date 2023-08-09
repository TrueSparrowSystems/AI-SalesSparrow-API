package com.salessparrow.api.lib;

import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.salessparrow.api.config.CoreConstants;

/**
 * AWS Key Management Service (KMS) component for encryption and decryption
 * operations.
 */
@Component
public class AwsKms {

  @Autowired
  private AWSKMS kmsClient;

  /**
   * Creates a new instance of AwsKms.
   */
  public AwsKms() {
  }

  /**
   * Encrypts a token using AWS KMS.
   * 
   * @param token
   * 
   * @return The encrypted token.
   */
  public String encryptToken(String token) {
    if (token == null) {
      return null;
    }
    EncryptRequest request = new EncryptRequest()
        .withKeyId(CoreConstants.kmsKeyId())
        .withPlaintext(ByteBuffer.wrap(token.getBytes()));

    EncryptResult result = kmsClient.encrypt(request);
    return Base64.getEncoder().encodeToString(result.getCiphertextBlob().array());
  }

  /**
   * Decrypts a token using AWS KMS.
   * 
   * @param encryptedToken
   * 
   * @return The decrypted token.
   */
  public String decryptToken(String encryptedToken) {
    byte[] decodedToken = Base64.getDecoder().decode(encryptedToken);

    DecryptRequest request = new DecryptRequest()
        .withCiphertextBlob(ByteBuffer.wrap(decodedToken))
        .withKeyId(CoreConstants.kmsKeyId());

    DecryptResult result = kmsClient.decrypt(request);

    if (result == null || result.getPlaintext() == null) {
      return null;
    }

    return new String(result.getPlaintext().array());
  }
}