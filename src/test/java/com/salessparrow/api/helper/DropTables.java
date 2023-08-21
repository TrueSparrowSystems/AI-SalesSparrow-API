package com.salessparrow.api.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.salessparrow.api.config.CoreConstants;

/**
 * This class is used to drop the tables.
 */
@TestConfiguration
public class DropTables {
  Logger logger = LoggerFactory.getLogger(DropTables.class);

  @Autowired
  private AmazonDynamoDB dynamoDB;

  /**
   * This method is used to drop the tables.
   */
  public void perform() {
    if (!CoreConstants.environment().equals("test")) {
      throw new RuntimeException("Cannot drop tables in non test environment");
    }

    List<String> tableList = getAllTableList();
    String envPrefix = CoreConstants.environment() + "_";

    for (String tableName : tableList) {
      if (tableName.startsWith(envPrefix)) {
        dynamoDB.deleteTable(tableName);
      }
    }
  }

  /**
   * This method is used to get all the table list.
   * 
   * @return List of table names
   */
  private List<String> getAllTableList() {
    ListTablesRequest listTablesRequest = new ListTablesRequest();
    ListTablesResult listTablesResult = dynamoDB.listTables(listTablesRequest);
    
    return listTablesResult.getTableNames();
  }
}
