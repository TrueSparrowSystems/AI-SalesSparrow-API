package com.salessparrow.api.fixtures.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;

@SpringBootTest
@WebAppConfiguration
public class SalesforceOrganizationFixture {

    @Autowired
    private SalesforceOrganizationRepository salesforceOrganizationRepository;

    @Autowired
    private AmazonDynamoDB dynamoDB;

    SalesforceOrganization salesforceOrganization;

    @BeforeEach
    public void setUp() {
        salesforceOrganization = new SalesforceOrganization();
        salesforceOrganization.setId("asdf");
        salesforceOrganization.setStatus(SalesforceOrganization.Status.ACTIVE);
        salesforceOrganization = salesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganization);
    }

    @AfterEach
    public void tearDown() {
        List<String> tableList = getAllTableList();

        for (String tableName : tableList) {
            System.out.println("Deleting table " + tableName);
            dynamoDB.deleteTable(tableName);
        }
    }

    private List<String> getAllTableList() {
        ListTablesRequest listTablesRequest = new ListTablesRequest();
        ListTablesResult listTablesResult = dynamoDB.listTables(listTablesRequest);
        return listTablesResult.getTableNames();
    }

    @Test
    public void testGetSalesforceOrganizationByExternalOrganizationId() {
        SalesforceOrganization retrievedOrg = salesforceOrganizationRepository.getById("asdf");
        
        assertEquals(retrievedOrg.getId(), "asdf");
        assertEquals(retrievedOrg.getStatus(), SalesforceOrganization.Status.ACTIVE);
    }
}
