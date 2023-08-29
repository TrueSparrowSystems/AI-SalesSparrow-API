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
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.errorLib.ErrorObject;
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

    private SalesforceOrganizationRepository salesforceOrganizationRepository;

    @BeforeEach
    public void setUp() throws DynamobeeException, IOException {
        setup.perform();
        this.salesforceOrganizationRepository =  new SalesforceOrganizationRepository(dynamoDBMapper);
    }

    @AfterEach
    public void tearDown() {
        cleanup.perform();
    }

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

        @Test
        public void testSaveSalesforceOrganization() {
            //Valid Save Db Query
            SalesforceOrganization salesforceOrganizationValid = new SalesforceOrganization();
            salesforceOrganizationValid.setExternalOrganizationId("externalUserId-1");
            SalesforceOrganization salesforceOrganizationResp = this.salesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganizationValid);
            assertEquals(salesforceOrganizationValid.getExternalOrganizationId(), salesforceOrganizationResp.getExternalOrganizationId());

            // Invalid Save Db Query without partition key
            SalesforceOrganization salesforceOrganizationInvalid = new SalesforceOrganization();
            salesforceOrganizationInvalid.setExternalOrganizationId("externalUserId-2");

            // mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceOrganizationRepository mockSalesforceOrganizationRepository = new SalesforceOrganizationRepository(dynamoDBMapper);

            // Mock the behavior to throw an exception when save is called
            doThrow(new CustomException(new ErrorObject("test:r_sor_tssfo_1", "something_went_wrong", "mock db save error")))
            .when(dynamoDBMapper)
            .save(salesforceOrganizationInvalid);

            // Test if CustomException is thrown with the expected error code
            CustomException thrownException = assertThrows(
                CustomException.class, 
                () -> mockSalesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganizationInvalid)
            );
            // Validate the error identifier to be a 500 error
            assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
        }

        @Test
        public void testGetSalesforceOrganizationByExternalOrganizationId() throws Exception{{
            String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
            FixtureData fixtureData = common.loadFixture("classpath:fixtures/unit/repositories/salesforceOrganizationRepository.json", currentFunctionName);
            loadFixture.perform(fixtureData);
            
            //Valid Get Db Query
            SalesforceOrganization salesforceOrganizationResp = this.salesforceOrganizationRepository.getSalesforceOrganizationByExternalOrganizationId("000Org-id");
            assertEquals("000Org-id", salesforceOrganizationResp.getExternalOrganizationId());

            String testExternalOrganizationId = "externalUserId-2";

            //  mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceOrganizationRepository mockSalesforceOrganizationRepository = new SalesforceOrganizationRepository(dynamoDBMapper);

            // Mock the behavior to throw an exception when load is called
            when(dynamoDBMapper.load(SalesforceOrganization.class, testExternalOrganizationId)).thenThrow(
                new CustomException(new ErrorObject("test:r_sor_gsotbsfui_1",
              "something_went_wrong","mock db query error")));

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
}
