package com.salessparrow.api.lib.salesforce;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * SalesforceQueries is a class for building the Salesforce queries.
 */
@Component
public class SalesforceQueries {
  
  public String getAccountsQuery(String q) {
    if (q == "") {
      return "SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 20";
    } 
    return "SELECT Id, Name FROM Account WHERE Name LIKE '%25"+q+"%25' ORDER BY LastModifiedDate DESC LIMIT 20";
  }

  /**
   * Get the list of notes for a given account
   * 
   * @param accountId
   * @return String
   */
  public String getContentDocumentIdUrl(String accountId) {
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
      queryBuilder.append("'").append(documentIds.get(i)).append("'");
    }
    queryBuilder.append(") ORDER BY LastModifiedDate DESC LIMIT 5");

    return queryBuilder.toString();
  }

  public String getNoteDetailsUrl(String noteId){
    return "SELECT Id, Title, TextPreview, CreatedBy.Name, LastModifiedDate FROM ContentNote WHERE Id = '" + noteId + "'";
  }

}
