package com.salessparrow.api.changelogs;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.github.dynamobee.changeset.ChangeLog;
import com.github.dynamobee.changeset.ChangeSet;

@ChangeLog
public class DatabaseChangelog {

  @ChangeSet(order = "001", id = "001", author = "testAuthor")
  public void createSalesforceOrganizationsTable(AmazonDynamoDB db) {
    String tableName = "salesforce_organizations";
    System.out.println("Creating table: "  + tableName);

    List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
    attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("S"));

    List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));

    ProvisionedThroughput pt = new ProvisionedThroughput(1L, 1L);
    db.createTable(attributeDefinitions, tableName, keySchema, pt);

    System.out.println("Done creating table: "  + tableName);
  }
}