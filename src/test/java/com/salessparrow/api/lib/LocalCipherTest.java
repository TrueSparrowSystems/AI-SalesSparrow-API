package com.salessparrow.api.lib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.Cipher;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalCipherTest {

  @InjectMocks
  private LocalCipher localCipher;

  @Mock
  private Cipher cipher;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testEncryptAndDecrypt() {
    String salt = localCipher.generateRandomSalt();
    String stringToEncrypt = "Hello, world!";

    String encrypted = localCipher.encrypt(salt, stringToEncrypt);
    String decrypted = localCipher.decrypt(salt, encrypted);

    assertEquals(decrypted, stringToEncrypt);
  }
}
