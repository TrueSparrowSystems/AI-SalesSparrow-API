package com.salessparrow.api.lib.salesforce.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.salessparrow.api.dto.entities.DescribeAccountFieldEntity;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;

/**
 * ValidateSalesforceAccountFields is a class that validates the fields of an account.
 */
@Component
public class ValidateSalesforceAccountFields {

	@Autowired
	SalesforceConstants salesforceConstants;

	/**
	 * Validate the body of the request to create/update an account.
	 * @param requestAccountBody
	 * @param describeAccountDto
	 */
	public void validateAccountRequestBody(Map<String, String> requestAccountBody,
			DescribeAccountFormatterDto describeAccountDto) {
		for (Map.Entry<String, String> entry : requestAccountBody.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (!describeAccountDto.getFields().containsKey(key)) {
				String paramErrorIdentifier = "invalid_" + key;
				throw new CustomException(new ParamErrorObject("l_s_h_vsaf_varb_1",
						salesforceConstants.InvalidAccountFieldMessage(), Arrays.asList(paramErrorIdentifier)));
			}
			else if (value == null || value.isEmpty()) {
				String fieldLabel = describeAccountDto.getFields().get(key).getLabel();
				String paramErrorIdentifier = "invalid_" + fieldLabel;

				throw new CustomException(new ParamErrorObject("l_s_h_vsaf_varb_2",
						salesforceConstants.InvalidAccountValueMessage(), Arrays.asList(paramErrorIdentifier)));
			}
			else {
				DescribeAccountFieldEntity describeAccountFieldEntity = describeAccountDto.getFields().get(key);
				String fieldLabel = describeAccountFieldEntity.getLabel();
				Boolean isValid = isValidValue(value, describeAccountFieldEntity);
				if (!isValid) {
					String paramErrorIdentifier = "invalid_" + fieldLabel;
					throw new CustomException(new ParamErrorObject("l_s_h_vsaf_varb_3",
							salesforceConstants.InvalidAccountValueMessage(), Arrays.asList(paramErrorIdentifier)));
				}
			}
		}
	}

	/**
	 * Validate the value of a field.
	 * @param value
	 * @param describeAccountFieldEntity
	 * @return boolean
	 */
	public boolean isValidValue(String value, DescribeAccountFieldEntity describeAccountFieldEntity) {

		switch (describeAccountFieldEntity.getType()) {
			case "double":
			case "currency":
			case "percent":
				// TODO - Check for precision and scale
				try {
					Double.parseDouble(value);
					return true;
				}
				catch (NumberFormatException e) {
					return false;
				}
			case "date":
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				try {
					LocalDate.parse(value, formatter);
					return true;
				}
				catch (DateTimeParseException e) {
					return false;
				}

			case "datetime":
				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

				try {
					LocalDate.parse(value, formatter2);
					return true;
				}
				catch (DateTimeParseException e) {
					return false;
				}

			case "time":
				DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm:ss");

				try {
					LocalTime.parse(value, formatter3);
					return true;
				}
				catch (DateTimeParseException e) {
					return false;
				}

			case "boolean":
				if (!value.equals("true") && !value.equals("false")) {
					return false;
				}
				else {
					return true;
				}

			case "picklist":
				for (DescribeAccountFieldEntity.PicklistValues picklist : describeAccountFieldEntity
					.getPicklistValues()) {
					if (picklist.getValue().equals(value) && picklist.getActive()) {
						return true;
					}
				}
				return false;

			case "multipicklist":
				String[] splitValues = value.split(";");
				for (String splitValue : splitValues) {
					boolean isValid = false;
					for (DescribeAccountFieldEntity.PicklistValues picklist : describeAccountFieldEntity
						.getPicklistValues()) {
						if (picklist.getValue().equals(splitValue) && picklist.getActive()) {
							isValid = true;
							break;
						}
					}
					if (!isValid) {
						return false;
					}
				}

			default:
				return true;
		}
	}

}
