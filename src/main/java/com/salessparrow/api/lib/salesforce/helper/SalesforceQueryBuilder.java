package com.salessparrow.api.lib.salesforce.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.Util;

/**
 * SalesforceQueries is a class for building the Salesforce queries.
 */
@Component
public class SalesforceQueryBuilder {

	/**
	 * Get the list of accounts for a given searchTerm
	 * @param searchTerm
	 * @return String
	 */
	public String getAccountsQuery(String searchTerm) {
		searchTerm = Util.escapeSpecialChars(searchTerm);

		String query = "";
		if (searchTerm == "") {
			query = "SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 20";
		}
		else {
			query = "SELECT Id, Name FROM Account WHERE Name LIKE '%" + searchTerm
					+ "%' ORDER BY LastModifiedDate DESC LIMIT 20";
		}

		return Util.urlEncoder(query);
	}

	/**
	 * Get the list of tasks for a given account
	 * @param accountId
	 * @return String
	 */
	public String getAccountTasksQuery(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util.urlEncoder(
				"SELECT Id, Description, ActivityDate, CreatedBy.Name, Owner.Name, LastModifiedDate FROM Task WHERE WhatId='"
						+ accountId + "' ORDER BY LastModifiedDate DESC LIMIT 5");
	}

	/**
	 * Get the list of notes for a given account
	 * @param accountId
	 * @return String
	 */
	public String getContentDocumentIdUrl(String accountId) {
		accountId = Util.escapeSpecialChars(accountId);

		return Util
			.urlEncoder("SELECT ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId = '" + accountId + "'");
	}

	/**
	 * Get the list of notes for a given account
	 * @param documentIds
	 * @return String
	 */
	public String getNoteListIdUrl(List<String> documentIds) {
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id IN (");

		for (int i = 0; i < documentIds.size(); i++) {
			if (i > 0) {
				queryBuilder.append(", ");
			}

			String documentId = Util.escapeSpecialChars(documentIds.get(i));
			queryBuilder.append("'").append(documentId).append("'");
		}
		queryBuilder.append(") ORDER BY LastModifiedDate DESC LIMIT 5");

		return Util.urlEncoder(queryBuilder.toString());
	}

	public String getNoteDetailsUrl(String noteId) {
		noteId = Util.escapeSpecialChars(noteId);

		return Util
			.urlEncoder("SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id = '"
					+ noteId + "'");
	}

	public String getCrmOrganizationUsersQuery(String searchTerm) {
		searchTerm = Util.escapeSpecialChars(searchTerm);
		String query = "";

		if (searchTerm == "") {
			query = "SELECT Id, Name FROM User ORDER BY LastModifiedDate DESC LIMIT 20";
		}
		else {
			query = "SELECT Id, Name FROM User WHERE Name LIKE '%" + searchTerm
					+ "%' ORDER BY LastModifiedDate DESC LIMIT 20";
		}

		return Util.urlEncoder(query);
	}

}
