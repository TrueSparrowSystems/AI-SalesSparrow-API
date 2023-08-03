package com.salessparrow.api.lib;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.salessparrow.api.config.CoreConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.ByteBuffer;
import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class AwsKmsTest {

  @Mock
  private AWSKMS kmsClient;

  @Mock
  private CoreConstants coreConstants;

  @InjectMocks
  private AwsKms awsKms;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidEncryptToken() {
    // Arrange
    String token = "myToken";
    ByteBuffer encryptedData = ByteBuffer.wrap("encryptedData".getBytes());
    EncryptResult encryptResult = new EncryptResult().withCiphertextBlob(encryptedData);

    // Mocking
    when(kmsClient.encrypt(any())).thenReturn(encryptResult);
    when(coreConstants.kmsKeyId()).thenReturn("kmsKeyId");

    // Act
    String encryptedToken = awsKms.encryptToken(token);

    // Assert
    assertEquals(Base64.getEncoder().encodeToString(encryptedData.array()), encryptedToken);
    verify(kmsClient, times(1)).encrypt(any(EncryptRequest.class));
  }

  @Test
  public void testInvalidEncryptToken() {
    // Arrange
    String token = null;

    // Mocking
    when(kmsClient.encrypt(any())).thenReturn(null);

    // Act & Assert
    assertNull(awsKms.encryptToken(token));
    verify(kmsClient, times(0)).encrypt(any(EncryptRequest.class));
  }

  @Test
  public void testValidDecryptToken() {
    // Arrange
    String encryptedToken = Base64.getEncoder().encodeToString("encryptedData".getBytes());
    ByteBuffer decryptedData = ByteBuffer.wrap("decryptedData".getBytes());
    DecryptResult decryptResult = new DecryptResult().withPlaintext(decryptedData);

    // Mocking
    when(kmsClient.decrypt(any())).thenReturn(decryptResult);
    when(coreConstants.kmsKeyId()).thenReturn("kmsKeyId");

    // Act
    String decryptedToken = awsKms.decryptToken(encryptedToken);

    // Assert
    assertEquals(new String(decryptedData.array()), decryptedToken);
    verify(kmsClient, times(1)).decrypt(any(DecryptRequest.class));
  }

  @Test
  public void testInvalidDecryptToken() {
    // Arrange
    String encryptedToken = "invalidToken";

    // Mocking
    when(kmsClient.decrypt(any())).thenReturn(new DecryptResult());

    // Act
    String decryptedToken = awsKms.decryptToken(encryptedToken);

    // Assert
    assertNull(decryptedToken);
    verify(kmsClient, times(1)).decrypt(any(DecryptRequest.class));
  }
}