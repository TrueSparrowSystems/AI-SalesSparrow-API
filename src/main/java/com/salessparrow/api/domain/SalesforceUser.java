package com.salessparrow.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.salessparrow.api.lib.globalConstants.UserConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SalesforceUser model.
 * 
 * Cached data is serialized into bytes and stored in cache and deserialize when retrieved.
 * Hence, the class must implement Serializable.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "salesforce_users")
public class SalesforceUser implements User, Serializable {

  public enum Status {
    ACTIVE(1),
    DELETED(2);

    private final int value;

    Status(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  @DynamoDBHashKey(attributeName = "external_user_id")
  private String externalUserId;

  @DynamoDBAttribute(attributeName = "identity_url")
  private String identityUrl;

  @DynamoDBAttribute(attributeName = "external_organization_id")
  private String externalOrganizationId;

  @DynamoDBAttribute(attributeName = "name")
  private String name;

  @DynamoDBAttribute(attributeName = "email")
  private String email;

  @DynamoDBAttribute(attributeName = "user_kind")
  private String userKind;

  @DynamoDBAttribute(attributeName = "cookie_token")
  private String cookieToken;

  @DynamoDBAttribute(attributeName = "encryption_salt")
  private String encryptionSalt;

  @DynamoDBTypeConverted(converter = StatusEnumConverter.class)
  @DynamoDBAttribute(attributeName = "status")
  private Status status;

  @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
  @DynamoDBAttribute(attributeName = "created_at")
  private Date createdAt;

  @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
  @DynamoDBAttribute(attributeName = "updated_at")
  private Date updatedAt;

  /**
   * Converts Status enum to Integer and vice versa.
   */
  public static class StatusEnumConverter implements DynamoDBTypeConverter<Integer, Status> {
    @Override
    public Integer convert(Status status) {
      return status.ordinal();
    }

    @Override
    public Status unconvert(Integer value) {
      return Status.values()[value];
    }
  }

  public String getId(String externalUserId) {
    return UserConstants.SALESFORCE_USER_KIND + "-" + externalUserId;
  }
}