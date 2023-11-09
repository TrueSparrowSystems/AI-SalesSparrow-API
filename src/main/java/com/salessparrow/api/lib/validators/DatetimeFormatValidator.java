package com.salessparrow.api.lib.validators;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.salessparrow.api.lib.customAnnotations.ValidDatetimeFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validate datetime format with regular expression
 *
 */
public class DatetimeFormatValidator implements ConstraintValidator<ValidDatetimeFormat, String> {

	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * Validate datetime format with regular expression
	 * @param value datetime address for validation
	 * @return true valid date format, false invalid date format
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		sdf.setLenient(false);

		try {
			// Attempt to parse the input date string
			Date parsedDate = sdf.parse(value);

			// Check if the parsed date is not null
			if (parsedDate != null) {
				// Format the parsed date back into a string
				String formattedDate = sdf.format(parsedDate);

				// Compare the formatted date with the input value
				return formattedDate.equals(value);
			}
			else {
				return false;
			}
		}
		catch (Exception ex) {
			return false;
		}
	}

}