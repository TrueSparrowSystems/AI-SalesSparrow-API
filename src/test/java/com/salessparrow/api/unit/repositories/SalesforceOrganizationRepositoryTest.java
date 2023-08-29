package com.salessparrow.api.unit.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.config.DynamoDBConfiguration;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;

@SpringBootTest
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class SalesforceOrganizationRepositoryTest {

    @Autowired
    private Setup setup;

    @Autowired
    private Cleanup cleanup;

    @Autowired
    private Common common;

    @Autowired
    private LoadFixture loadFixture;

    private DynamoDBMapper mockDynamoDBMapper;
    
    private SalesforceOrganizationRepository realSalesforceOrganizationRepository;

    private SalesforceOrganizationRepository mockSalesforceOrganizationRepository;

    @BeforeEach
    public void setUp() throws DynamobeeException, IOException {
        setup.perform();   
        // Use your Spring bean here
        DynamoDBMapper dynamoDBMapper = new DynamoDBConfiguration().dynamoDBMapper();     
        this.realSalesforceOrganizationRepository = new SalesforceOrganizationRepository(dynamoDBMapper);

        this.mockDynamoDBMapper = mock(DynamoDBMapper.class); // Mocked instance
        this.mockSalesforceOrganizationRepository = new SalesforceOrganizationRepository(mockDynamoDBMapper);
    }

    @AfterEach
    public void tearDown() {
        cleanup.perform();
    }

    /**
     * Test Valid Save Db Query
     */
    @Test
    public void testValidSaveSalesforceOrganization() {
        //Valid Save Db Query
        SalesforceOrganization salesforceOrganizationValid = new SalesforceOrganization();
        salesforceOrganizationValid.setExternalOrganizationId("externalUserId-1");
        SalesforceOrganization salesforceOrganizationResp = this.realSalesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganizationValid);
        assertEquals(salesforceOrganizationValid.getExternalOrganizationId(), salesforceOrganizationResp.getExternalOrganizationId());
    }

    /**
     * Test Invalid Save Db Query
     */
    @Test
    public void testInvalidSaveSalesforceOrganization() {
        // Invalid Save Db Query without partition key
        SalesforceOrganization salesforceOrganizationInvalid = new SalesforceOrganization();
        salesforceOrganizationInvalid.setExternalOrganizationId("externalUserId-2");

        // Mock the behavior to throw an exception when save is called
        doThrow(new AmazonDynamoDBException("mock db save error"))
        .when(mockDynamoDBMapper)
        .save(salesforceOrganizationInvalid);

        // Test if CustomException is thrown with the expected error code
        CustomException thrownException = assertThrows(
            CustomException.class, 
            () -> mockSalesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganizationInvalid)
        );
        // Validate the error identifier to be a 500 error
        assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
    }    

    /**
     * Test Valid Get Db Query
     */
    @Test
    public void testValidGetSalesforceOrganizationByExternalOrganizationId() throws Exception{
        String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
        FixtureData fixtureData = common.loadFixture("classpath:fixtures/unit/repositories/salesforceOrganizationRepository.json", currentFunctionName);
        loadFixture.perform(fixtureData);
        
        //Valid Get Db Query
        SalesforceOrganization salesforceOrganizationResp = this.realSalesforceOrganizationRepository.getSalesforceOrganizationByExternalOrganizationId("000Org-id");
        assertEquals("000Org-id", salesforceOrganizationResp.getExternalOrganizationId());
    }

    /**
     * Test Invalid Get Db Query
     */
    @Test
    public void testInvalidGetSalesforceOrganizationByExternalOrganizationId() throws Exception{
        String testExternalOrganizationId = "externalUserId-2";

        // Mock the behavior to throw an exception when load is called
        when(mockDynamoDBMapper.load(SalesforceOrganization.class, testExternalOrganizationId)).thenThrow(
            new AmazonDynamoDBException("mock db get error"));

        // Mock Invalid Get Db Query
        // Test if CustomException is thrown with the expected error code
        CustomException thrownException = assertThrows(
            CustomException.class, 
            () -> mockSalesforceOrganizationRepository.getSalesforceOrganizationByExternalOrganizationId(testExternalOrganizationId)
        );
        // Validate the error identifier to be a 500 error
        assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
    }    
}
