package com.salessparrow.api.dto.formatter;

import com.salessparrow.api.dto.entities.DescribeAccountFieldEntity;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when describing account.
 */
@Data
public class DescribeAccountFormatterDto {

	private DescribeAccountFieldEntity[] fields;

}
