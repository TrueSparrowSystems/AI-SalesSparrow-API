package com.salessparrow.api.lib;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.lib.errorLib.ErrorResponseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ErrorEmailService {

	Logger logger = LoggerFactory.getLogger(ErrorEmailService.class);

	@Autowired
	private AmazonSimpleEmailService sesClient;

	/**
	 * Send error email
	 * @param contextString - context string for subject. helps in easy identification of
	 * error
	 * @param errorObj - error object - contains error code, message, http code, internal
	 * error identifier
	 * @param stackTraceElements - stack trace elements
	 * @return void
	 */
	public void sendErrorMail(String contextString, ErrorResponseObject errorObj,
			StackTraceElement[] stackTraceElements) {
		logger.info("Sending error email for context: " + contextString);

		String requestId = MDC.get("trackingId");

		String body = "Tracking id:" + requestId + "\n";
		body += "http_code=" + errorObj.getHttpCode() + "\n";
		body += "code=" + errorObj.getCode() + "\n";
		body += "message=" + errorObj.getMessage() + "\n";
		body += "internal_error_identifier=" + errorObj.getInternalErrorIdentifier() + "\n";
		body += "params_error=" + errorObj.getParamErrors() + "\n\n\n";

		for (StackTraceElement stackTraceElement : stackTraceElements) {
			body += stackTraceElement.toString() + "\n";
		}

		String subject = "SalesSparrow::" + CoreConstants.environment() + "::Error-" + contextString + "-"
				+ errorObj.getMessage();

		// Send email only if not in dev environment
		if (!CoreConstants.isDevEnvironment() && !CoreConstants.isTestEnvironment()
				&& !CoreConstants.isLocalTestEnvironment()) {
			sendEmail(CoreConstants.errorEmailFrom(), CoreConstants.errorEmailTo(), subject, body);
		}
		else {
			logger.info("Skip email for development.\n\n subject {} \n body {}", subject, body);
		}

	}

	/**
	 * Send mail async using SES. Handles exception if any
	 * @param from - source email address
	 * @param to - destination email address
	 * @param subject - subject of email
	 * @param body - body of email
	 * @return void
	 */
	@Async
	public void sendEmail(String from, String to, String subject, String body) {
		logger.info("send SES Email");
		try {
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(
						new Message().withBody(new Body().withText(new Content().withCharset("UTF-8").withData(body)))
							.withSubject(new Content().withCharset("UTF-8").withData(subject)))
				.withSource(from);

			sesClient.sendEmail(request);
		}
		catch (Exception e) {
			logger.error("Error sending email {} subject:{} body:{}", e, subject, body);
		}
	}

}
