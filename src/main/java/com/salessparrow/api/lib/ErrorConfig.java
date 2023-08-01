package com.salessparrow.api.lib;

public class ErrorConfig {
  private String http_code;
  private String code;
  private String message;

  public String getHttp_code() {
    return http_code;
  }

  public void setHttp_code(String http_code) {
    this.http_code = http_code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "ErrorInfo{" +
        "http_code='" + http_code + '\'' +
        ", code='" + code + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}