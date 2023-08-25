package com.salessparrow.api.lib.salesforce.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.Util;

/**
 * SalesforceQueries is a class for building the Salesforce queries.
 */
@Component
public class SalesforceQueryBuilder {
  public String formatStringForSoqlQueries(String input){
    // escaping special characters of SOQL queries Like ( ', ", \, %, _)
    input = Util.escapeSpecialChars(input);

    // encoding the input to UrlEncoder
    input = Util.urlEncoder(input);

    return input;
  }
  
  /**
   * Get the list of accounts for a given searchTerm
   * 
   * @param searchTerm
   * 
   * @return String
   */
  public String getAccountsQuery(String searchTerm) {
    if (searchTerm == "") {
      return "SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 20";
    } 
    searchTerm = formatStringForSoqlQueries(searchTerm);
    
    return "SELECT Id, Name FROM Account WHERE Name LIKE '%25"+searchTerm+"%25' ORDER BY LastModifiedDate DESC LIMIT 20";
  }

  /**
   * Get the list of notes for a given account
   * 
   * @param accountId
   * @return String
   */
  public String getContentDocumentIdUrl(String accountId) {
    accountId = Util.urlEncoder(accountId);

    return "SELECT ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId = '"
        + accountId + "'";
  }

  /**
   * Get the list of notes for a given account
   * 
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

      String documentId = Util.urlEncoder(documentIds.get(i));
      queryBuilder.append("'").append(documentId).append("'");
    }
    queryBuilder.append(") ORDER BY LastModifiedDate DESC LIMIT 5");

    return queryBuilder.toString();
  }

  public String getNoteDetailsUrl(String noteId){
    noteId = Util.urlEncoder(noteId);

    return "SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id = '" + noteId + "'";
  }

  public String getCrmOrganizationUsersQuery(String searchTerm) {
    if (searchTerm == "") {
      return "SELECT Id, Name FROM User ORDER BY LastModifiedDate DESC LIMIT 20";
    } 
    searchTerm = formatStringForSoqlQueries(searchTerm);

    return "SELECT Id, Name FROM User WHERE Name LIKE '%25"+searchTerm+"%25' ORDER BY LastModifiedDate DESC LIMIT 20";
  }

}
