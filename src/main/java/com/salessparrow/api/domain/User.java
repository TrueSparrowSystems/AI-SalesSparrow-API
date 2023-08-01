package com.salessparrow.api.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class User implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  @Column(name = "cookie_token")
  private String cookieToken;

  @Column(name = "encryption_salt")
  private String encryptionSalt;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  // @Override
  public String toString() {
    return "User{" + "id=" + id + ", email='" + email + '\'' + ", password='" + password + '\'' + ", cookieToken='"
        + cookieToken + '\'' + ", encryptionSalt='" + encryptionSalt + '\'' + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt + '}';
  }

  public User(String email, String password, String cookieToken, String encryptionSalt, Long createdAt,
      Long updatedAt) {
    this.email = email;
    this.password = password;
    this.cookieToken = cookieToken;
    this.encryptionSalt = encryptionSalt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCookieToken() {
    return cookieToken;
  }

  public void setCookieToken(String cookieToken) {
    this.cookieToken = cookieToken;
  }

  public String getEncryptionSalt() {
    return encryptionSalt;
  }

  public void setEncryptionSalt(String encryptionSalt) {
    this.encryptionSalt = encryptionSalt;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }

  // @Override
  // public String toString() {
  // return "User{" +
  // "id=" + id +
  // ", email='" + email + '\'' +
  // // Include other fields you want to display in the log output
  // '}';
  // }
}
