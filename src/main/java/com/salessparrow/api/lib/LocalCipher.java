package com.salessparrow.api.lib;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.salessparrow.api.exception.CustomException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Class for local cipher to encrypt and decrypt client keys.
 */
@Component
public class LocalCipher {

  private final String algorithm = "AES";
  private final String encryptionMode = "AES/ECB/PKCS5Padding";

  /**
   * Encrypt the input string.
   *
   * @param salt   The salt for encryption.
   * @param string The string to be encrypted.
   * @return The encrypted string in hexadecimal format.
   */
  public String encrypt(String salt, String string) {
    byte[] encryptedBytes = null;
    try {
      Cipher encrypt = Cipher.getInstance(encryptionMode);
      SecretKeySpec secretKey = generateSecretKey(salt);
      encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
      encryptedBytes = encrypt.doFinal(string.getBytes(StandardCharsets.UTF_8));   
    } catch (Exception e) {
      throw new CustomException(
          "l_lc_e_1",
          "something_went_wrong",
          e.getMessage());
    }
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * Decrypt the input encrypted string.
   *
   * @param salt            The salt for decryption.
   * @param encryptedString The encrypted string in hexadecimal format.
   * @return The decrypted string.
   */
  public String decrypt(String salt, String encryptedString) {
    byte[] decryptedBytes = null;
    try {
      Cipher decrypt = Cipher.getInstance(encryptionMode);
      SecretKeySpec secretKey = generateSecretKey(salt);
      decrypt.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] decodedBytes = Base64.getDecoder().decode(encryptedString);
      decryptedBytes = decrypt.doFinal(decodedBytes);
    } catch (Exception e) {
      throw new CustomException(
          "l_lc_d_1",
          "something_went_wrong",
          e.getMessage());
    }

    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }

  /**
   * Generate random IV.
   *
   * @param number The number of bytes for the IV.
   * @return The random IV in hexadecimal format.
   */
  public String generateRandomIv(int number) {
      byte[] ivBytes = new byte[number];
      new SecureRandom().nextBytes(ivBytes);
      return bytesToHex(ivBytes).substring(0, number * 2);
  }

  /**
   * Generate random salt.
   *
   * @return The random salt in hexadecimal format.
   */
  public String generateRandomSalt() {
      byte[] saltBytes = new byte[16];
      new SecureRandom().nextBytes(saltBytes);
      return bytesToHex(saltBytes);
  }

  private SecretKeySpec generateSecretKey(String salt) throws NoSuchAlgorithmException {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      byte[] keyBytes = sha.digest(salt.getBytes(StandardCharsets.UTF_8));
      byte[] key = new byte[16];
      System.arraycopy(keyBytes, 0, key, 0, 16);
      return new SecretKeySpec(key, algorithm);
  }

  private String bytesToHex(byte[] hash) {
      StringBuilder hexString = new StringBuilder(2 * hash.length);
      for (byte b : hash) {
          String hex = Integer.toHexString(0xff & b);
          if (hex.length() == 1) {
              hexString.append('0');
          }
          hexString.append(hex);
      }
      return hexString.toString();
  }
}

