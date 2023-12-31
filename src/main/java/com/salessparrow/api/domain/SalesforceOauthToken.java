package com.salessparrow.api.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SalesforceOauthToken model.
 *
 * Cached data is serialized into bytes and stored in cache and deserialize when
 * retrieved. Hence, the class must implement Serializable.
 */
@Data
@NoArgsConstructor
@DynamoDBTable(tableName = "salesforce_oauth_tokens")
public class SalesforceOauthToken implements Serializable {

	public enum Status {

		ACTIVE(1), DELETED(2);

		private final int value;

		private static final Map<Integer, Status> map = new HashMap<>();

		static {
			for (Status status : Status.values()) {
				map.put(status.value, status);
			}
		}

		Status(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Status valueOf(int value) {
			return map.get(value);
		}

	}

	@DynamoDBHashKey(attributeName = "external_user_id")
	private String externalUserId;

	@DynamoDBAttribute(attributeName = "identity_url")
	private String identityUrl;

	@DynamoDBAttribute(attributeName = "access_token")
	private String accessToken;

	@DynamoDBAttribute(attributeName = "refresh_token")
	private String refreshToken;

	@DynamoDBAttribute(attributeName = "signature")
	private String signature;

	@DynamoDBAttribute(attributeName = "id_token")
	private String idToken;

	@DynamoDBAttribute(attributeName = "instance_url")
	private String instanceUrl;

	@DynamoDBTypeConverted(converter = StatusEnumConverter.class)
	@DynamoDBAttribute(attributeName = "status")
	private Status status;

	@DynamoDBAttribute(attributeName = "issued_at")
	private Long issuedAt;

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
			return status.getValue();
		}

		@Override
		public Status unconvert(Integer value) {
			return Status.valueOf(value);
		}

	}

}
