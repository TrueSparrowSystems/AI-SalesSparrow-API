package com.salessparrow.api.lib.salesforce;

import org.springframework.stereotype.Component;

@Component
public class SalesforceLib {
  
  public String getAccountsQuery(String q) {
    if (q == "") {
      return "SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 20";
    } 
    return "SELECT Id, Name FROM Account WHERE Name LIKE '%25"+q+"%25' ORDER BY LastModifiedDate DESC LIMIT 20";
  }

  public String getNoteCreationUrl() {
    return "/services/data/v58.0/sobjects/ContentNot";
  }

  public String getNoteAttachmentUrl() {
    return "/services/data/v58.0/sobjects/ContentDocumentLink";
  }
}
