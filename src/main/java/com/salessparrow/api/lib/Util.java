package com.salessparrow.api.lib;

import java.time.Instant;
import java.time.ZoneOffset;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Class for utility functions.
 *
 */
@Component
public class Util {

	/**
	 * Get JsonNode from json string
	 * @param jsonString
	 * @return JsonNode
	 */
	public JsonNode getJsonNode(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(jsonString);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_u_gjn_1", "something_went_wrong", e.getMessage()));
		}
		return jsonNode;
	}

	/**
	 * Retrieves a string representation of all request headers. For security, the value
	 * of the "authorization, cookie, password" header is obfuscated.
	 * @param request - The HTTP request containing the headers to be logged.
	 * @return String - A string representation of the headers in the format
	 * "{headerName:headerValue, ...}".
	 */
	public static String generateHeaderLogString(HttpServletRequest request) {
		StringBuilder headerBuilder = new StringBuilder("{");
		request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
			// Add any other secret headers here that you don't want logged.
			if (headerName.equals("authorization") || headerName.equals("cookie") || headerName.equals("password")) {
				headerBuilder.append(headerName).append(":**********, ");
			}
			else {
				headerBuilder.append(headerName).append(":").append(request.getHeader(headerName)).append(", ");
			}
		});
		headerBuilder.append("}");

		return headerBuilder.toString();
	}

	/**
	 * Encode plain text to base64
	 * @param plainText - String to be encoded
	 * @return String - Encoded string
	 */
	public static String base64Encode(String plainText) {
		String encodedText = null;

		try {
			encodedText = java.util.Base64.getEncoder().encodeToString(plainText.getBytes());
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_u_b64e_1", "something_went_wrong", e.getMessage()));
		}

		return encodedText;
	}

	/**
	 * Decode base64 encoded text
	 * @param encodedText - String to be decoded
	 * @return String - Decoded string
	 */
	public static String base64Decode(String encodedText) {
		String decodedText = null;

		try {
			byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedText);
			decodedText = new String(decodedBytes);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_u_b64d_1", "something_went_wrong", e.getMessage()));
		}

		return decodedText;
	}

	/**
	 * Get current time in date format
	 * @return Date
	 */
	public static Date getCurrentTimeInDateFormat() {
		Instant currentTimestamp = Instant.now();
		return Date.from(currentTimestamp.atOffset(ZoneOffset.UTC).toInstant());
	}

	/**
	 * Escape special characters in a string for use in a regular expression. SOQL is
	 * important from security point of view. Refer
	 * https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/sforce_api_calls_soql_select_quotedstringescapes.htm
	 * for more details.
	 * @param input
	 * @return String
	 */
	public static String escapeSpecialChars(String input) {
		if (input == null || input == "")
			return input;

		String[] specialChars = { "\\", "%", "'", "\"", "_" };

		for (String specialChar : specialChars) {
			input = input.replace(specialChar, "\\" + specialChar);
		}

		return input;
	}

	/**
	 * URL encode a string
	 * @param input
	 * @return String
	 */
	public static String urlEncoder(String input) {
		if (input == null || input == "")
			return input;

		try {
			return URLEncoder.encode(input, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new CustomException(new ErrorObject("u_u_ue_1", "something_went_wrong", e.getMessage()));
		}
	}

	/**
	 * Get date format from datetime
	 * @param date
	 * @return String
	 */
	public String getDateFormatFromDatetime(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(date);
		}
		return null;
	}

	/**
	 * Unescape special characters in a string intended for plaintext contexts only.
	 *
	 * This function is intended for use only with strings that will be rendered as
	 * plaintext. It is NOT suitable for HTML strings, XML strings, or any other context
	 * where special characters may have syntactic meaning.
	 *
	 * Current Implementation: - The ampersand ("&amp;") is unescaped to "&"
	 *
	 * Future versions may include additional un-escaping rules as needed.
	 * @param input The original string containing escaped special characters.
	 * @return A new string where certain special characters have been unescaped.
	 */
	public static String unEscapeSpecialCharactersForPlainText(String input) {
		return input.replace("&amp;", "&");
	}

	public static String replaceNewLineWithBreak(String input) {
		return input.replace("\n", "<br>");
	}

	/**
	 * Get a trimmed string of a given length.
	 * @param input
	 * @param length
	 * @return String
	 */
	public static String getTrimmedString(String input, Integer length) {
		if (input == null) {
			return null;
		}

		if (input.length() < length) {
			return input;
		}

		return input.substring(0, length);
	}

}
