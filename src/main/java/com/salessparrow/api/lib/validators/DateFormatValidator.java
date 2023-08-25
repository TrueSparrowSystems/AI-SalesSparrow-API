package com.salessparrow.api.lib.validators;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.salessparrow.api.lib.customAnnotations.ValidDateFormat;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validate date format with regular expression
 * 
 */
public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Validate date format with regular expression
     * 
     * @param date date address for validation
     * 
     * @return true valid date format, false invalid date format
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            Date parsedDate = sdf.parse(value);
            String dateString = sdf.format(parsedDate);
            return dateString.equals(value.toString());
        } catch (Exception ex) {
            return false;
        }
  }
}