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
			Date parsedDate = sdf.parse(value);
			String dateString = sdf.format(parsedDate);
			return dateString.equals(value.toString());
		}
		catch (Exception ex) {
			return false;
		}
	}

}