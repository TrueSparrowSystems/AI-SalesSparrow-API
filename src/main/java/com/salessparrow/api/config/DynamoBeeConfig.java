package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.dynamobee.Dynamobee;
import com.salessparrow.api.lib.globalConstants.config.dynamoBeeConfigConstants;

@Configuration
public class DynamoBeeConfig {
  @Autowired
  private AmazonDynamoDB db;

  @Autowired
  private dynamoBeeConfigConstants dynamoBeeConfigConstants;

  @Bean
  public Dynamobee dynamobee(){
  Dynamobee runner = new Dynamobee(db);
  
  runner.setChangeLogsScanPackage(dynamoBeeConfigConstants.getChangeLogScanPackage()).setChangelogTableName(dynamoBeeConfigConstants.getChangelogTableName());
  
  return runner;
}
}
