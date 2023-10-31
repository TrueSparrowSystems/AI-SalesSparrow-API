package com.salessparrow.api.dto.entities;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when describing account.
 */
@Data
public class DescribeAccountFieldEntity {

	private String label;

	private String name;

	private String length;

	private String type;

	private String defaultValue;

	private PicklistValues[] picklistValues;

	@Data
	public class PicklistValues {

		private String label;

		private String value;

		private String active;

	}

}
