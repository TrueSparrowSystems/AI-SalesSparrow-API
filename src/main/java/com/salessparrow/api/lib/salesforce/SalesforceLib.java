package com.salessparrow.api.lib.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SalesforceLib {
  
  public String getAccountsUrl(String q) {
    if (q == "") {
      return "/services/data/v58.0/query/?q=SELECT Id, Name FROM Account ORDER BY LastModifiedDate DESC LIMIT 2";
    } 
    return "/services/data/v58.0/query/?q=SELECT Id, Name FROM Account WHERE Name LIKE '%25"+q+"%25' ORDER BY LastModifiedDate DESC LIMIT 2";
  }
}
